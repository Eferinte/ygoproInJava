package com.ocg;

import com.ocg.core.CallbackImpls.CardReaderImpl;
import com.ocg.core.CallbackImpls.MessageHandleImpl;
import com.ocg.core.OCGDll;
import com.ocg.utils.BitReader;
import com.ocg.utils.BitWriter;
import com.ocg.utils.MutateInt;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;

import static com.ocg.Constants.*;

public class SingleDuel extends DuelMode {

    public static DuelPlayer[] players = new DuelPlayer[2];
    public DuelPlayer[] pplayer = new DuelPlayer[2];
    protected boolean[] ready = new boolean[2];
    protected Deck[] pdeck = new Deck[2];
    protected int[] deck_error = new int[2];
    protected byte[] hand_result = new byte[2];
    protected int last_response;
    protected byte tp_player;
    protected byte match_result;
    protected short[] time_limit = new short[2];
    protected short time_elapsed;

    public SingleDuel(boolean is_match) {

    }

    public String JoinGame(DuelPlayer dp) {
        dp.game = this;
        if (players[0] == null) {
            players[0] = dp;
            return "加入成功";
        } else if (players[1] == null) {
            players[1] = dp;
            return "加入成功";
        }
        return "加入失败，房间人数已满";
    }

    public void StartDuel() {
        NetServer.SendBufferToPlayer(players[0], STOC_DUEL_START, 0, 0, null);
        NetServer.SendBufferToPlayer(players[1], STOC_DUEL_START, 0, 0, null);
//        for (int i = 0; i < 2; i++) {
        byte[] deck_buffer = new byte[12];
        BitWriter deck_writer = new BitWriter(deck_buffer, 0);
        deck_writer.writeInt16(pdeck[0].main.size());
        deck_writer.writeInt16(pdeck[0].extra.size());
        deck_writer.writeInt16(pdeck[0].side.size());
        deck_writer.writeInt16(pdeck[1].main.size());
        deck_writer.writeInt16(pdeck[1].extra.size());
        deck_writer.writeInt16(pdeck[1].side.size());
        // TODO NetServer应当是负责将数据传输给两个客户端，但这里先转到本地
        NetServer.SendBufferToPlayer(null, STOC_DECK_COUNT, 0, 12, deck_buffer);
//        }
        NetServer.SendBufferToPlayer(players[0], STOC_SELECT_HAND, 0, 0, null);
        hand_result[0] = 0;
        hand_result[1] = 0;
        players[0].state = CTOS_HAND_RESULT; // CTOS_HAND_RESULT
        players[1].state = CTOS_HAND_RESULT; // CTOS_HAND_RESULT
        duel_stage = DUEL_STAGE_FINGER; // DUEL_STAGE_FINGER 猜拳
    }

    ;

    public void TPResult() {
        duel_stage = Constants.DUEL_STAGE_DUELING;
        boolean swapped = false;
        pplayer[0] = players[0];
        pplayer[1] = players[1];
//        Collections.shuffle(pdeck[0].main_code);
//        Collections.shuffle(pdeck[1].main_code);
        time_limit[0] = host_info.time_limit;
        time_limit[1] = host_info.time_limit;
        OCGDll.INSTANCE.set_message_handler(new MessageHandleImpl());
        OCGDll.INSTANCE.set_card_reader(new CardReaderImpl());
        pduel = OCGDll.INSTANCE.create_duel(39);
        OCGDll.INSTANCE.set_player_info(pduel, 0, host_info.start_lp, host_info.start_hand, host_info.draw_count);
        OCGDll.INSTANCE.set_player_info(pduel, 1, host_info.start_lp, host_info.start_hand, host_info.draw_count);
        for (int i = pdeck[0].getMain_code().size() - 1; i >= 0; i--) {
            OCGDll.INSTANCE.new_card(pduel, pdeck[0].getMain_code().get(i), 0, 0, 1, 0, 8);
        }
        for (int i = pdeck[0].getExtra_code().size() - 1; i >= 0; i--) {
            OCGDll.INSTANCE.new_card(pduel, pdeck[0].getExtra_code().get(i), 0, 0, 40, 0, 8);
        }
        for (int i = pdeck[1].getMain_code().size() - 1; i >= 0; i--) {
            OCGDll.INSTANCE.new_card(pduel, pdeck[1].getMain_code().get(i), 1, 1, 1, 0, 8);
        }
        for (int i = pdeck[1].getExtra_code().size() - 1; i >= 0; i--) {
            OCGDll.INSTANCE.new_card(pduel, pdeck[1].getExtra_code().get(i), 1, 1, 40, 0, 8);
        }
        OCGDll.INSTANCE.start_duel(pduel, 0x10);
        Process();
    }


    public void Process() {
        // 如何处理unsigned
        byte[] engineBuffer = new byte[0x1000];
        long engFlag = 0, engLen = 0;
        int stop = 0;
        while (stop == 0) {
            if (engFlag == 2) {
                break;
            }
            long result = (long) OCGDll.INSTANCE.process(pduel) & 0xffffffff;
            engLen = result & 0xffff;
            engFlag = result >> 16;
            if (engLen > 0) {
                OCGDll.INSTANCE.get_message(pduel, engineBuffer);
                stop = Analyze(engineBuffer, engLen);
            }
        }
    }

    void WaitForResponse(int playerId) {
        last_response = playerId;
        byte msg = MSG_WAITING;
        NetServer.SendPacketToPlayer(players[1 - playerId], STOC_GAME_MSG, msg);
        System.out.println("wait for palyer" + playerId);
    }


    // pbufw是干嘛的？
    public int Analyze(byte[] msgbuffer, long len) {
        int offset = 0, pbufw = 0;
        BitReader buffer = new BitReader(msgbuffer, 0);
        int player, count, type;
        while (buffer.getPosition() < (int) len) {
            offset = buffer.getPosition();
            int engType = buffer.readUInt8();
            switch (engType) {
                case MSG_RETRY -> {
                    WaitForResponse(last_response);
                    NetServer.SendBufferToPlayer(players[last_response], STOC_GAME_MSG, offset, buffer.getPosition() - offset);
                    return 1;
                }
                case MSG_HINT -> {
                    type = buffer.readInt8();
                    player = buffer.readInt8();
                    buffer.readInt32();
                    switch (type) {
                        case 1, 2, 3, 5 -> { // 发给自己
                            NetServer.SendBufferToPlayer(players[player], STOC_GAME_MSG, offset, buffer.getPosition() - offset);
                            break;
                        }
                        case 4, 6, 7, 8, 9, 11 -> { // 发给其他人
                            NetServer.SendBufferToPlayer(players[1 - player], STOC_GAME_MSG, offset, buffer.getPosition() - offset);
                            break;
                        }
                        case 10 -> { // 全局消息
                            NetServer.SendBufferToPlayer(players[player], STOC_GAME_MSG, offset, buffer.getPosition() - offset);
                            NetServer.SendBufferToPlayer(players[1 - player], STOC_GAME_MSG, offset, buffer.getPosition() - offset);
                        }
                    }
                    break;
                }
                case MSG_SELECT_IDLECMD -> {
                    player = buffer.readInt8();
                    count = buffer.readInt8();
                    buffer.step(count * 7);
                    count = buffer.readInt8();
                    buffer.step(count * 7);
                    count = buffer.readInt8();
                    buffer.step(count * 7);
                    count = buffer.readInt8();
                    buffer.step(count * 7);
                    count = buffer.readInt8();
                    buffer.step(count * 7);
                    count = buffer.readInt8();
                    buffer.step(count * 11 + 3);
                    // TODO refresh
                    RefreshHand(0);
                    WaitForResponse(player);
                    NetServer.SendBufferToPlayer(players[player], (byte) 1, offset, buffer.getPosition() - offset, msgbuffer);
                    return 1;
                }
                case MSG_NEW_TURN -> {
                    buffer.step();
                    NetServer.SendBufferToPlayer(players[0], (byte) 1, offset, buffer.getPosition() - offset, msgbuffer);
                }
                case MSG_NEW_PHASE -> {
                    buffer.step(2);
                    NetServer.SendBufferToPlayer(players[0], (byte) 1, offset, buffer.getPosition() - offset, msgbuffer);
                    NetServer.SendBufferToPlayer(players[1], (byte) 1, offset, buffer.getPosition() - offset, msgbuffer);
                }
                case MSG_DRAW -> {
                    player = buffer.readInt8();
                    count = buffer.readInt8();
                    pbufw = buffer.getPosition();
                    buffer.step(count * 4);

                    NetServer.SendBufferToPlayer(players[player], (byte) 1, offset, buffer.getPosition() - offset, msgbuffer);

                    for (int i = 0; i < count; ++i) {
                        if ((msgbuffer[pbufw + 3] & 0x80) == 0) {
                            msgbuffer[pbufw] = 0;
                            msgbuffer[pbufw + 1] = 0;
                            msgbuffer[pbufw + 2] = 0;
                            msgbuffer[pbufw + 3] = 0;
                        }
                        pbufw += 4;
                    }
                    break;
                }
            }
        }

        return 0;
    }

    public void UpdateDeck() {
        //LoadDeck
        pdeck[0] = players[0].use_deck;
        pdeck[1] = players[1].use_deck;
    }

    public void GetResponse(DuelPlayer dp, byte[] pdata, int len) {
        byte[] resb = new byte[64];
        for (int i = 0; i < len; i++) {
            resb[i] = pdata[i];
        }
        OCGDll.INSTANCE.set_responseb(pduel, resb);
        players[dp.type].state = (byte) 0xff;
        //TODO timelimit
        Process();
    }

    @Override
    public void RefreshMzone(int player, int flag, int use_cache) {
    }

    @Override
    public void RefreshSzone(int player, int flag, int use_cache) {

    }

    @Override
    public void RefreshHand(int player, int flag, int use_cache) {
        byte[] core_query_buffer = new byte[0x1997];
        int len = OCGDll.INSTANCE.query_field_card(pduel, player, LOCATION_HAND, flag | QUERY_POSITION, core_query_buffer, use_cache);
        ByteBuffer query_buffer = ByteBuffer.allocate(0x2000);
        query_buffer.put((byte) MSG_UPDATE_DATA);
        query_buffer.put((byte) player);
        query_buffer.put(LOCATION_HAND);
        query_buffer.put(core_query_buffer);
        NetServer.SendBufferToPlayer(players[player], STOC_GAME_MSG, 0, len + 3, query_buffer.array());
        MutateInt qlen = new MutateInt();
        while (qlen.getValue() < len) {
            int slen = BitReader.ReadInt32(core_query_buffer, qlen);
        }
    }

    public void RefreshHand(int player) {
        byte[] query_buffer = new byte[0x2000];
        byte[] core_query_buffer = new byte[0x1997];
        int len = OCGDll.INSTANCE.query_field_card(pduel, player, LOCATION_HAND, 0 | QUERY_POSITION | QUERY_CODE, core_query_buffer, 0);
        query_buffer[0] = MSG_UPDATE_DATA;
        query_buffer[1] = (byte) player;
        query_buffer[2] = LOCATION_HAND;
        // 复制core_buffer到buffer中
        for (int i = 0; i < len; i++) {
            query_buffer[i + 3] = core_query_buffer[0];
        }
        NetServer.SendBufferToPlayer(players[player], STOC_GAME_MSG, 0, len + 3, query_buffer);
        MutateInt qlen = new MutateInt();
        // 将不可视的card数据置空
        while (qlen.getValue() < len) {
            int slen = BitReader.ReadInt32(core_query_buffer, qlen);
            int qflag = BitReader.ReadInt32(core_query_buffer, qlen);
            if ((qflag & QUERY_CODE) != 0) {
                qlen.step(4);
            }
            int position = (BitReader.ReadInt32(core_query_buffer, qlen) >> 24) & 0xff;
            if ((position & POS_FACEUP) == 0) {
                Arrays.fill(core_query_buffer, 3, slen, (byte) 0);
            }
            qlen.set(slen);
        }
        // 复制core_buffer到buffer中
        for (int i = 0; i < len; i++) {
            query_buffer[i + 3] = core_query_buffer[0];
        }
        NetServer.SendBufferToPlayer(players[1 - player], STOC_GAME_MSG, 0, len + 3, query_buffer);
    }

    @Override
    public void RefreshGrave(int player, int flag, int use_cache) {

    }

    @Override
    public void RefreshExtra(int player, int flag, int use_cache) {

    }


}
