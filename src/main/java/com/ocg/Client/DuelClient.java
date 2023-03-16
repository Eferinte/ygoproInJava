package com.ocg.Client;

import com.ocg.dataController.DataManager;
import com.ocg.utils.BitReader;
import com.ocg.utils.MutateInt;

import static com.ocg.Constants.*;
import static com.ocg.Constants.PHASE_END;
import static com.ocg.Game.mainGame;

public class DuelClient {
    public static void ClientAnalyze(byte[] msgbuffer, int offset, int length) {
        BitReader buffer = new BitReader(msgbuffer, offset);
        int msgType = buffer.readUInt8();
        switch (msgType) {
            case MSG_SELECT_IDLECMD -> {
                buffer.readInt8();
                int code, desc, count, con, loc, seq;
                ClientCard pcard;
                mainGame.dField.summonable_cards.clear();
                count = buffer.readInt8();
                for (int i = 0; i < count; i++) {
                    code = buffer.readInt8();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    loc = buffer.readInt8();
                }

            }
            case MSG_NEW_TURN -> {
                System.out.println("new turn");
            }
            case MSG_NEW_PHASE -> {
                int phase = buffer.readUInt8();
                switch (phase) {
                    case PHASE_DRAW -> {
                        System.out.println("DRAW");
                    }
                    case PHASE_STANDBY -> {
                        System.out.println("STANDBY");
                    }
                    case PHASE_MAIN1 -> {
                        System.out.println("M1");
                    }
                    case PHASE_BATTLE_START -> {

                        System.out.println("BATTLE_START");
                    }
                    case PHASE_MAIN2 -> {
                        System.out.println("M2");
                    }
                    case PHASE_END -> {
                        System.out.println("END");
                    }
                }
            }
            case MSG_DRAW -> {
                int player = buffer.readInt8();
                int count = buffer.readInt8();

                System.out.println("playerId =" + player);
                System.out.println("draw count =" + count);
                for (int i = 0; i < count; i++) {
                    int code = buffer.readInt32();
                    System.out.println("card code =" + DataManager.GetDesc(code).name);
                }
            }
        }
    }

}
