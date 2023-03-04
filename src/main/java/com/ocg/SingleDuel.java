package com.ocg;

import com.ocg.CallbackImpls.CardReaderImpl;
import com.ocg.CallbackImpls.MessageHandleImpl;
import com.sun.jna.ptr.ByteByReference;

import java.util.Collections;

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
    protected short [] time_limit = new short[2];
    protected short time_elapsed;

    public SingleDuel(boolean is_match){

    }

    public boolean JoinGame(DuelPlayer dp){
        if(players[0] == null) {
            players[0] = dp;
            pdeck[0] = dp.use_deck;
            return true;
        }
        if(players[1] == null) {
            players[1] = dp;
            pdeck[1] = dp.use_deck;
            return true;
        }
        return false;
    }

    public void StartDuel(){
        hand_result[0] = 0;
        hand_result[1] = 0;
        players[0].state = 3; // CTOS_HAND_RESULT
        players[1].state = 3; // CTOS_HAND_RESULT
        duel_stage = 1; // DUEL_STAGE_FINGER 猜拳
    };

    public void TPResult(){
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
        System.out.println("[SingleDuel]: pduel = " + pduel);
        OCGDll.INSTANCE.set_player_info(pduel,0, host_info.start_lp, host_info.start_hand,host_info.draw_count);
        OCGDll.INSTANCE.set_player_info(pduel,1, host_info.start_lp, host_info.start_hand,host_info.draw_count);
        for (int i = pdeck[0].main_code.size()-1; i >=0;i--){
            OCGDll.INSTANCE.new_card(pduel, pdeck[0].main_code.get(i), 0, 0, 1, 0,8);
        }
        for (int i = pdeck[0].extra_code.size()-1; i >=0;i--){
            OCGDll.INSTANCE.new_card(pduel, pdeck[0].extra_code.get(i), 0 ,0,40, 0,8);
        }
//        for (int i = pdeck[1].main_code.size()-1; i >=0;i--){
//            OCGDll.INSTANCE.new_card(pduel, pdeck[1].main_code.get(i), (byte) 0, (byte) 0, Constants.LOCATION_DECK, (byte) 0,Constants.POS_FACEDOWN_DEFENSE);
//        }
//        for (int i = pdeck[1].extra_code.size()-1; i >=0;i--){
//            OCGDll.INSTANCE.new_card(pduel, pdeck[1].extra_code.get(i), (byte) 0, (byte) 0, Constants.LOCATION_EXTRA, (byte) 0,Constants.POS_FACEDOWN_DEFENSE);
//        }
        OCGDll.INSTANCE.start_duel(pduel, 0x10);
        Process();
    }


    void Process(){
        byte[] engineBuffer = new byte[0x1000];
        int engFlag = 0, engLen = 0;
        int stop = 0;
        while(stop != 0){
            if(engFlag == 2){
                break;
            }
            int result = OCGDll.INSTANCE.process(pduel);
            engLen = result & 0xffff;
            engFlag = result >> 16;
            if(engLen > 0){
                OCGDll.INSTANCE.get_message(pduel, engineBuffer);
                stop = Analyze(engineBuffer, engLen);
            }
        }
    }

    int Analyze(byte[] msgbuffer, int len){
        return 0;
    }


}
