package com.ocg.Client;

import com.ocg.NetServer;
import com.ocg.Server.MessageFormatter;
import com.ocg.SingleDuel;
import com.ocg.utils.BitReader;
import com.ocg.utils.BitWriter;
import org.java_websocket.WebSocket;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.ocg.Constants.*;
import static com.ocg.Constants.PHASE_END;
import static com.ocg.Main.*;

public class DuelClient {
    public static byte[] response_buf = new byte[64];
    public static byte response_length = 0;
    public static byte[] duel_client_read = new byte[0x2000];
    public static byte[] duel_client_write = new byte[0x2000];


    public static void main(String[] args) {

        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 8888);
            Scanner in = new Scanner(System.in);
            while (true) {
                String buffer = "";
                if (in.hasNextLine()) {
                    buffer = in.nextLine();
                }
                socket.getOutputStream().write((buffer + "over").getBytes());
                socket.getOutputStream().flush();

                //流模式下只要不关闭，就可以反复持有，获取数据。  输入流还是那个。
                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[1024];
                int len;
                StringBuilder sb = new StringBuilder();
                //一次交互完成后，while循环过来，在此阻塞，即监听
                while ((len = inputStream.read(bytes)) != -1) {
                    sb.append(new String(bytes, 0, len));
                    //单次交互结束标识，跳出监听
                    if (new String(bytes, 0, len).indexOf("over") >= 0) {
                        break;
                    }
                }
                System.out.println("[server]: " + sb.toString().substring(0, sb.toString().length() - 4).replaceAll("\n", "\n[server]: "));
//                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void HandleSTOCPacketLan(byte[] data, int len) {
        BitReader buffer = new BitReader(data, 0);
        byte pktType = (byte) buffer.readInt8();
        switch (pktType) {
            case STOC_GAME_MSG -> {
                ClientAnalyze(data, buffer.getPosition(), len - 1);
            }
            case STOC_DECK_COUNT -> {
                mainGame.gMutex = true;
                String a = BitReader.getBit(data, 1, 16);
                int deckc = buffer.readInt16();
                int extrac = buffer.readInt16();
                int sidec = buffer.readInt16();
                mainGame.dField.initial(0, deckc, extrac);
                deckc = buffer.readInt16();
                extrac = buffer.readInt16();
                sidec = buffer.readInt16();
                mainGame.dField.initial(1, deckc, extrac);
            }
            case STOC_DUEL_START -> {
                mainGame.gMutex = true;
                mainGame.dField.clear();
                mainGame.dInfo.isStarted = true;
                mainGame.dInfo.isFinished = false;
                mainGame.dInfo.lp[0] = 0;
                mainGame.dInfo.lp[1] = 0;
                mainGame.dInfo.turn = 0;
                mainGame.dInfo.time_left[0] = 0;
                mainGame.dInfo.time_left[1] = 0;
                mainGame.dInfo.time_player = 2;
                mainGame.dInfo.isReplaySwapped = false;
                mainGame.is_building = false;
            }
        }
    }

    public static int ClientAnalyze(byte[] msgbuffer, int offset, int length) {
        BitReader buffer = new BitReader(msgbuffer, offset);
        mainGame.dInfo.curMsg = buffer.readUInt8();
        switch (mainGame.dInfo.curMsg) {
            case MSG_START -> {
                System.out.println("start");
            }
            case MSG_UPDATE_DATA -> {
                int player = mainGame.LocalPlayer(buffer.readInt8());
                int location = buffer.readInt8();
                mainGame.dField.updateFieldCard(player,location,buffer.getBuffer());
            }
            case MSG_SELECT_IDLECMD -> {
                /**
                 * 计算cmdFlag
                 */
                buffer.readInt8();
                int code, desc, count, con, loc, seq;
                ClientCard pcard;
                mainGame.dField.summonable_cards.clear();
                count = buffer.readInt8();
                for (int i = 0; i < count; i++) {
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.summonable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_SUMMON;
                }
                mainGame.dField.spsummonable_cards.clear();
                count = buffer.readInt8();
                for (int i = 0; i < count; i++) {
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.spsummonable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_SPSUMMON;
                    if (pcard.location == LOCATION_DECK) {
                        pcard.setCode(code);
                        mainGame.dField.deck_act = true;
                    } else if (pcard.location == LOCATION_GRAVE) {
                        mainGame.dField.grave_act = true;
                    } else if (pcard.location == LOCATION_REMOVED) {
                        mainGame.dField.remove_act = true;
                    } else if (pcard.location == LOCATION_EXTRA) {
                        mainGame.dField.extra_act = true;
                    } else {
                        if ((pcard.location == LOCATION_SZONE) && (pcard.sequence == 0) && ((pcard.type & TYPE_PENDULUM) != 0) && (pcard.equipTarget == null)) {
                            mainGame.dField.pzone_act[pcard.controller] = true;
                        }
                    }
                }
                mainGame.dField.reposable_cards.clear();
                count = buffer.readInt8();
                for (int i = 0; i < count; i++) {
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.reposable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_REPOS;
                }
                mainGame.dField.msetable_cards.clear();
                count = buffer.readInt8();
                for (int i = 0; i < count; i++) {
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.reposable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_MSET;
                }
                mainGame.dField.ssetable_cards.clear();
                count = buffer.readInt8();
                for (int i = 0; i < count; i++) {
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.reposable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_SSET;
                }
                mainGame.dField.activatable_cards.clear();
                mainGame.dField.activatable_descs.clear();
                mainGame.dField.conti_cards.clear();
                count = buffer.readInt8();
                for (int i = 0; i < count; i++) {
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    desc = buffer.readInt32();
                    pcard = mainGame.dField.getCard(con, loc, seq);
                    int flag = 0;
                    // TODO ???
                    if ((code & 0x80000000) != 0) {
                        flag = EDESC_OPERATION;
                        code = 0x7fffffff;
                    }
                    mainGame.dField.activatable_cards.add(pcard);
                    mainGame.dField.activatable_descs.add(new HashMap<>(Map.of(desc, flag)));
                    if (flag == EDESC_OPERATION) {
                        pcard.chain_code = code;
                        mainGame.dField.conti_cards.add(pcard);
                        mainGame.dField.conti_act = true;
                    } else {
                        pcard.cmdFlag |= COMMAND_ACTIVATE;
                        if (pcard.controller == 0) {
                            if (pcard.location == LOCATION_GRAVE)
                                mainGame.dField.grave_act = true;
                            else if (pcard.location == LOCATION_REMOVED)
                                mainGame.dField.remove_act = true;
                            else if (pcard.location == LOCATION_EXTRA)
                                mainGame.dField.extra_act = true;
                        }
                    }
                }
                System.out.println(mainGame.dField);
                if (buffer.readInt8() != 0) {
                    // TODO 接口化
                    System.out.println("显示BattlePhase按钮");
                }
                if (buffer.readInt8() != 0) {
                    // TODO 接口化
                    System.out.println("显示EndPhase按钮");
                }
                if (buffer.readInt8() != 0) {
                    // TODO 接口化
                    System.out.println("允许切洗手卡");
                } else {
                    // TODO 接口化
                    System.out.println("禁止切洗手卡");
                }
                return 0;
            }
            case MSG_NEW_TURN -> {
                // TODO 接口化
                System.out.println("new turn");
                return 1;
            }
            case MSG_NEW_PHASE -> {
                int phase = buffer.readUInt8();
                // TODO 接口化
                switch (phase) {
                    case PHASE_DRAW -> {
                        System.out.println("DRAW");
                        server.BroadcastToDuelist("DRAW", MessageFormatter.MessageType.DECLARE);
                    }
                    case PHASE_STANDBY -> {
                        System.out.println("STANDBY");
                        server.BroadcastToDuelist("STANDBY", MessageFormatter.MessageType.DECLARE);
                    }
                    case PHASE_MAIN1 -> {
                        System.out.println("M1");
                        server.BroadcastToDuelist("M1", MessageFormatter.MessageType.DECLARE);
                    }
                    case PHASE_BATTLE_START -> {

                        System.out.println("BATTLE_START");
                        server.BroadcastToDuelist("BATTLE", MessageFormatter.MessageType.DECLARE);
                    }
                    case PHASE_MAIN2 -> {
                        System.out.println("M2");
                        server.BroadcastToDuelist("M2", MessageFormatter.MessageType.DECLARE);
                    }
                    case PHASE_END -> {
                        System.out.println("END");
                        server.BroadcastToDuelist("END", MessageFormatter.MessageType.DECLARE);
                    }
                }
                return 1;
            }
            case MSG_DRAW -> {
                int player = buffer.readInt8();
                int count = buffer.readInt8();
                WebSocket conn = SingleDuel.players[player].conn;
                ClientCard pcard;
                for (int i = 0; i < count; i++) {
                    int code = buffer.readInt32();
                    pcard = mainGame.dField.getCard(player, LOCATION_DECK, mainGame.dField.deck[player].size() - 1 - i);
                    if (!mainGame.dField.deck_reversed || code != 0) {
                        pcard.setCode(code & 0x7fffffff);
                    }
                }
                for (int i = 0; i < count; i++) {
                    mainGame.gMutex = true;
                    pcard = mainGame.dField.getCard(player, LOCATION_DECK, mainGame.dField.deck[player].size() - 1);
                    mainGame.dField.deck[player].remove(mainGame.dField.deck[player].size() - 1);
                    mainGame.dField.addCard(pcard, player, LOCATION_HAND, 0);
//                    ???
//                    for (int j=0;j<mainGame.dField.hand[player].size();j++){
                    mainGame.dField.moveCard(mainGame.dField.hand[player].get(i), 10);
//                    }
                    mainGame.gMutex = false;
                }
                if (player == 0) {
                    System.out.println("我方抽了" + count + "张卡");
                } else System.out.println("对方抽了" + count + "张卡");
                // 通信
                server.Broadcast(null, MessageFormatter.MessageType.GAME_MSG, null, "DRAW");
                return 1;
            }
        }
        return 1;
    }

    public static void SetResponseI(int respI) {
        BitWriter bw = new BitWriter(response_buf, 0);
        bw.writeInt32(respI);
        response_length = 4;
    }

    public static void SendResponse() {
        switch (mainGame.dInfo.curMsg) {
            case MSG_SELECT_IDLECMD -> {
                mainGame.dField.clearCommandFlag();
                // 隐藏cmd按钮
                break;
            }
        }
        mainGame.dInfo.time_player = 2;//TODO ???
        SendBufferToServer(CTOS_RESPONSE, response_buf, response_length);

    }

    static void SendPacketToServer(byte proto) {
        byte[] p = duel_client_write;
        BitWriter bw = new BitWriter(p, 0);
        bw.writeInt8(proto);
    }

    public static void SendBufferToServer(byte proto, byte[] buffer, int response_length) {
        byte[] p = duel_client_write;
        BitWriter bw = new BitWriter(p, 0);
        bw.writeInt8(proto);
        //memcpy
        for (int i = 0; i < response_length; i++) {
            p[i + 1] = buffer[i];
        }
        //TODO 事件触发-监听-响应
        NetServer.HandleCTOSPacket(duel_mode.pplayer[0], p, response_length);
    }
    public void StartClient(int ip,short port,boolean create_game){
        
    }

}
