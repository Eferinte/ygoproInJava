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
        for (int i=0;i<2;++i){
            players[i] = new DuelPlayer();
            ready[i] = false;
        }
    }
    public void StartDuel(DuelPlayer dp){
        hand_result[0] = 0; // 先后手
        hand_result[1] = 0;
        players[0].state = 3; // CTOS_HAND_RESULT
        players[1].state = 3; // CTOS_HAND_RESULT
        duel_stage = 1; // DUEL_STAGE_FINGER 猜拳
    };

    public void TPResult(DuelPlayer dp){
        duel_stage = Constants.DUEL_STAGE_DUELING;
        boolean swapped = false;
        pplayer[0] = players[0];
        pplayer[1] = players[1];
        if((dp.type == 1) || (dp.type == 0)) {
            DuelPlayer p = players[0];
            players[0] = players[1];
            players[1] = p;
            players[0].type = 0;
            players[1].type = 1;
            Deck d = pdeck[0];
            pdeck[0] = pdeck[1];
            pdeck[1] = d;
            swapped = true;
        }
        dp.state = Constants.CTOS_RESPONSE;
        Collections.shuffle(pdeck[0].main);
        time_limit[0] = host_info.time_limit;
        time_limit[1] = host_info.time_limit;
        OCGDll.INSTANCE.set_message_handler(new MessageHandleImpl());
        OCGDll.INSTANCE.set_card_reader(new CardReaderImpl());
        pduel = OCGDll.INSTANCE.create_duel(39);
        OCGDll.INSTANCE.set_player_info(pduel,0, host_info.start_lp, host_info.start_hand,host_info.draw_count);
        OCGDll.INSTANCE.set_player_info(pduel,1, host_info.start_lp, host_info.start_hand,host_info.draw_count);
        for (int i = pdeck[0].main.size(); i >=0;i--){
            OCGDll.INSTANCE.new_card(pduel, pdeck[0].main.get(i).code, (byte) 0, (byte) 0, Constants.LOCATION_DECK, (byte) 0,Constants.POS_FACEDOWN_DEFENSE);
        }
        for (int i = pdeck[0].extra.size(); i >=0;i--){
            OCGDll.INSTANCE.new_card(pduel, pdeck[0].extra.get(i).code, (byte) 0, (byte) 0, Constants.LOCATION_EXTRA, (byte) 0,Constants.POS_FACEDOWN_DEFENSE);
        }
        for (int i = pdeck[1].main.size(); i >=0;i--){
            OCGDll.INSTANCE.new_card(pduel, pdeck[1].main.get(i).code, (byte) 0, (byte) 0, Constants.LOCATION_DECK, (byte) 0,Constants.POS_FACEDOWN_DEFENSE);
        }
        for (int i = pdeck[1].extra.size(); i >=0;i--){
            OCGDll.INSTANCE.new_card(pduel, pdeck[1].extra.get(i).code, (byte) 0, (byte) 0, Constants.LOCATION_EXTRA, (byte) 0,Constants.POS_FACEDOWN_DEFENSE);
        }
        OCGDll.INSTANCE.start_duel(pduel, 0x10);
        Process();
    }

    /**
     * 线程循环
     */
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
