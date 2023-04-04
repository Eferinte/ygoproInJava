package com.ocg;

import com.ocg.dataController.DeckReader;

public class DuelPlayer {
    public String name;
    public String ip;
    Deck use_deck;
    byte type;
    byte state;
    DuelMode game;
    public DuelPlayer(){
        type = 0;
        state = 0;
    }
    public DuelPlayer(String name){
        type = 0;
        state = 0;
        this.name = name;
    }
    public DuelPlayer(String name,String ip){
        type = 0;
        state = 0;
        this.name = name;
        this.ip = ip;
    }
    public boolean LoadDefault() {
        use_deck = DeckReader.ReadYDK("D:\\MyCardLibrary\\ygopro\\deck\\闪刀.ydk");
        use_deck.LoadCardData();
        return true;
    }


}
