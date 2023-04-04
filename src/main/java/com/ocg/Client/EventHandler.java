package com.ocg.Client;

import com.ocg.Game;

import static com.ocg.Constants.BUTTON_CMD_SUMMON;
import static com.ocg.Main.mainGame;

public class EventHandler {
    public static void gameEventHandler(int id){
        ClientField field =  mainGame.dField;
        switch (id){
            case BUTTON_CMD_SUMMON->{
               field.HideMenu();
                if(field.menu_card == null)
                    break;
                for(long i = 0; i < field.summonable_cards.size(); ++i) {
                    if(field.summonable_cards.get((int)i) == field.menu_card) {
                        field.ClearCommandFlag();
                        DuelClient.SetResponseI((int)i << 16);
                        DuelClient.SendResponse();
                        break;
                    }
                }
                break;
            }
        }
    }
}
