package com.ocg;

import com.ocg.core.CallbackImpls.CardReaderImpl;
import com.ocg.core.CallbackImpls.MessageHandleImpl;
import com.ocg.core.OCGDll;
import com.ocg.utils.BitReader;
import com.ocg.utils.MutateInt;

import static com.ocg.Constants.*;

public class SingleDuel extends DuelMode {

    protected DuelPlayer[] players = new DuelPlayer[2];
    protected DuelPlayer[] pplayer = new DuelPlayer[2];
    protected boolean[] ready = new boolean[2];
    protected Deck[] pdeck = new Deck[2];
    protected int[] deck_error = new int[2];
    protected byte[] hand_result = new byte[2];
    protected byte last_response;
    protected byte tp_player;
    protected byte match_result;
    protected short[] time_limit = new short[2];
    protected short time_elapsed;

    public SingleDuel(boolean is_match) {

    }

    public boolean JoinGame(DuelPlayer dp) {
        if (players[0] == null) {
            players[0] = dp;
            pdeck[0] = dp.use_deck;
            return true;
        }
        if (players[1] == null) {
            players[1] = dp;
            pdeck[1] = dp.use_deck;
            return true;
        }
        return false;
    }

    public void StartDuel() {
        hand_result[0] = 0;
        hand_result[1] = 0;
        players[0].state = 3; // CTOS_HAND_RESULT
        players[1].state = 3; // CTOS_HAND_RESULT
        duel_stage = 1; // DUEL_STAGE_FINGER 猜拳
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
        for (int i = pdeck[0].main_code.size() - 1; i >= 0; i--) {
            OCGDll.INSTANCE.new_card(pduel, pdeck[0].main_code.get(i), 0, 0, 1, 0, 8);
        }
        for (int i = pdeck[0].extra_code.size() - 1; i >= 0; i--) {
            OCGDll.INSTANCE.new_card(pduel, pdeck[0].extra_code.get(i), 0, 0, 40, 0, 8);
        }
        for (int i = pdeck[1].main_code.size() - 1; i >= 0; i--) {
            OCGDll.INSTANCE.new_card(pduel, pdeck[1].main_code.get(i), 1, 1, 1, 0, 8);
        }
        for (int i = pdeck[1].extra_code.size() - 1; i >= 0; i--) {
            OCGDll.INSTANCE.new_card(pduel, pdeck[1].extra_code.get(i), 1, 1, 40, 0, 8);
        }
        OCGDll.INSTANCE.start_duel(pduel, 0x10);
        Process();
    }


    void Process() {
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
    void WaitForResponse(int playerId){
        System.out.println("wait for palyer"+playerId);
    }


    // pbufw是干嘛的？
    int Analyze(byte[] msgbuffer, long len) {
        int offset=0, pbufw= 0;
        BitReader buffer = new BitReader(msgbuffer, 0);
        int player, count, type;
        while (buffer.getPosition() < (int) len) {
            offset = buffer.getPosition();
            int engType = buffer.readUInt8();
            switch (engType) {
                case MSG_SELECT_IDLECMD -> {
                    player = buffer.readInt8();
                    count = buffer.readInt8();
                    buffer.step(count*7);
                    count = buffer.readInt8();
                    buffer.step(count*7);
                    count = buffer.readInt8();
                    buffer.step(count*7);
                    count = buffer.readInt8();
                    buffer.step(count*7);
                    count =buffer.readInt8();
                    count = buffer.readInt8();
                    count = buffer.readInt8();
                    buffer.step(count*11+3);
                    WaitForResponse(player);
                    Net.SendBufferToPlayer(players[player],(byte)1,offset,buffer.getPosition()-offset,msgbuffer);
                    return 1;
                }
                case MSG_NEW_TURN -> {
                    buffer.step();
                    Net.SendBufferToPlayer(players[0],(byte)1,offset,buffer.getPosition()-offset,msgbuffer);
                }
                case MSG_NEW_PHASE -> {
                    buffer.step(2);
                    Net.SendBufferToPlayer(players[0],(byte)1,offset,buffer.getPosition()-offset,msgbuffer);
                    Net.SendBufferToPlayer(players[1],(byte)1,offset,buffer.getPosition()-offset,msgbuffer);
                }
                case MSG_DRAW -> {
                    player = buffer.readInt8();
                    count = buffer.readInt8();
                    pbufw = buffer.getPosition();
                    buffer.step(count * 4);

                    Net.SendBufferToPlayer(players[player],(byte)1,offset,buffer.getPosition()-offset,msgbuffer);

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


}
