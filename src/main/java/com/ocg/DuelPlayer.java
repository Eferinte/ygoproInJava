package com.ocg;

import com.ocg.dataController.DeckReader;

public class DuelPlayer {
    short[] name = new short[20];
    Deck use_deck;
    byte type;
    byte state;
    DuelPlayer(){
        type = 0;
        state = 0;
    }
    public boolean LoadDefault() {
        use_deck = DeckReader.ReadYDK("D:\\MyCardLibrary\\ygopro\\deck\\闪刀.ydk");
        return true;
    }


}
