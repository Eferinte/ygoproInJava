package com.ocg.Client;

import com.ocg.dataController.DataManager;
import com.ocg.utils.BitReader;
import com.ocg.utils.BitWriter;
import com.ocg.utils.MutateInt;

import java.util.HashMap;

import static com.ocg.Constants.*;
import static com.ocg.Constants.PHASE_END;
import static com.ocg.Game.mainGame;

public class DuelClient {
    public static void HandleSTOCPacketLan(byte[] data,int len){
        BitReader buffer = new BitReader(data,0);
        byte pktType = (byte)buffer.readInt8();
        switch (pktType){
            case STOC_GAME_MSG -> {
                ClientAnalyze(data,buffer.getPosition(),len-1);
            }
            case STOC_DECK_COUNT -> {
                mainGame.gMutex = true;
                String a = BitReader.getBit(data,1,16);
                int deckc = buffer.readInt16();
                int extrac = buffer.readInt16();
                int sidec = buffer.readInt16();
                mainGame.dField.Initial(0,deckc,extrac);
                 deckc = buffer.readInt16();
                 extrac = buffer.readInt16();
                 sidec = buffer.readInt16();
                mainGame.dField.Initial(1,deckc,extrac);
            }
            case STOC_DUEL_START -> {
                mainGame.gMutex = true;
                mainGame.dField.Clear();
                mainGame.dInfo.isStarted=true;
                mainGame.dInfo.isFinished=false;
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
        int msgType = buffer.readUInt8();
        switch (msgType) {
            case MSG_START -> {
                System.out.println("start");
            }
            case MSG_SELECT_IDLECMD -> {
                buffer.readInt8();
                int code, desc, count, con, loc, seq;
                ClientCard pcard;
                mainGame.dField.summonable_cards.clear();
                count = buffer.readInt8();
                for (int i=0;i<count;i++){
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.GetCard(con,loc,seq);
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
                    pcard = mainGame.dField.GetCard(con,loc,seq);
                    mainGame.dField.spsummonable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_SPSUMMON;
                    if(pcard.location == LOCATION_DECK){
                        pcard.SetCode(code,mainGame);
                        mainGame.dField.deck_act = true;
                    } else if (pcard.location == LOCATION_GRAVE) {
                        mainGame.dField.deck_act = true;
                    } else if (pcard.location == LOCATION_REMOVED) {
                        mainGame.dField.deck_act = true;
                    }else if (pcard.location == LOCATION_EXTRA) {
                        mainGame.dField.deck_act = true;
                    }else{
                        if((pcard.location == LOCATION_SZONE) && (pcard.sequence == 0) && ((pcard.type & TYPE_PENDULUM) !=0) && (pcard.equipTarget==null) ){
                            mainGame.dField.pzone_act[pcard.controller] = true;
                        }
                    }
                }
                mainGame.dField.reposable_cards.clear();
                count = buffer.readInt8();
                for(int i=0;i<count;i++){
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.GetCard(con,loc,seq);
                    mainGame.dField.reposable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_REPOS;
                }
                mainGame.dField.msetable_cards.clear();
                count = buffer.readInt8();
                for(int i=0;i<count;i++){
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.GetCard(con,loc,seq);
                    mainGame.dField.reposable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_MSET;
                }
                mainGame.dField.ssetable_cards.clear();
                count = buffer.readInt8();
                for(int i=0;i<count;i++){
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    pcard = mainGame.dField.GetCard(con,loc,seq);
                    mainGame.dField.reposable_cards.add(pcard);
                    pcard.cmdFlag |= COMMAND_SSET;
                }
                mainGame.dField.activatable_cards.clear();
                mainGame.dField.activatable_descs.clear();
                mainGame.dField.conti_cards.clear();
                count = buffer.readInt8();
                for (int i=0;i<count;i++){
                    code = buffer.readInt32();
                    con = mainGame.LocalPlayer(buffer.readInt8());
                    loc = buffer.readInt8();
                    seq = buffer.readInt8();
                    desc = buffer.readInt32();
                    pcard = mainGame.dField.GetCard(con,loc,seq);
                    int flag = 0;
                    // TODO ???
                    if((code & 0x80000000)!=0){
                        flag = EDESC_OPERATION;
                        code = 0x7fffffff;
                    }
                    mainGame.dField.activatable_cards.add(pcard);
                    mainGame.dField.activatable_descs.add(new HashMap<>(desc,flag));
                    if(flag == EDESC_OPERATION){
                        pcard.chain_code = code;
                        mainGame.dField.conti_cards.add(pcard);
                        mainGame.dField.conti_act = true;
                    }else{
                        pcard.cmdFlag |= COMMAND_ACTIVATE;
                        if(pcard.controller ==0){
                            if(pcard.location == LOCATION_GRAVE)
                                mainGame.dField.grave_act = true;
                            else if(pcard.location == LOCATION_REMOVED)
                                mainGame.dField.remove_act = true;
                            else if(pcard.location == LOCATION_EXTRA)
                                mainGame.dField.extra_act = true;
                        }
                    }
                }
                if(buffer.readInt8()!=0){
                    // TODO 接口化
                    System.out.println("显示BattlePhase按钮");
                }
                if(buffer.readInt8()!=0){
                    // TODO 接口化
                    System.out.println("显示EndPhase按钮");
                }
                if(buffer.readInt8()!=0){
                    // TODO 接口化
                    System.out.println("允许切洗手卡");
                }else {
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
                return 1;
            }
            case MSG_DRAW -> {
                int player = buffer.readInt8();
                int count = buffer.readInt8();
                ClientCard pcard;
                for (int i = 0; i < count; i++) {
                    int code = buffer.readInt32();
                    pcard = mainGame.dField.GetCard(player, LOCATION_DECK,mainGame.dField.deck[player].size()-1-i);
                    if(!mainGame.dField.deck_reversed || code !=0){
                        pcard.SetCode(code & 0x7fffffff,mainGame);
                    }
                }
                for(int i=0;i<count;i++){
                    mainGame.gMutex = true;
                    pcard = mainGame.dField.GetCard(player,LOCATION_DECK,mainGame.dField.deck[player].size()-1);
                    mainGame.dField.deck[player].remove(mainGame.dField.deck[player].size()-1);
                    mainGame.dField.AddCard(pcard, player, LOCATION_HAND, 0);
//                    ???
//                    for (int j=0;j<mainGame.dField.hand[player].size();j++){
                    mainGame.dField.MoveCard(mainGame.dField.hand[player].get(i),10);
//                    }
                    mainGame.gMutex = false;
                }
                if(player ==0){
                    System.out.println("我方抽了"+count+"张卡");
                }else System.out.println("对方抽了"+count+"张卡");
                return 1;
            }
        }
        return 1;
    }

}
