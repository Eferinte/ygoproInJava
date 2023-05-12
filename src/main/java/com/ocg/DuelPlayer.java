package com.ocg;

import com.ocg.dataController.DeckReader;
import org.java_websocket.WebSocket;

import java.util.UUID;

public class DuelPlayer {
    public String name;
    public String id; //TODO 泛型化
    public Deck use_deck;
    public WebSocket conn;
    byte type;
    byte state;
    DuelMode game;
    public DuelPlayer(String name,String id,WebSocket conn){
        type = 0;
        state = 0;
        this.name = name;
        this.id = id;
        this.conn = conn;
    }
    public DuelPlayer(String name,String id,Deck deck){
        this.name = name;
        this.id = id;
        this.use_deck = deck;
    }
    public boolean LoadDefault() {
        use_deck = DeckReader.ReadYDK("D:\\MyCardLibrary\\ygopro\\deck\\闪刀.ydk");
        use_deck.LoadCardData();
        return true;
    }

    public DuelPlayer getNetPacket(){
        return new DuelPlayer(this.name,this.id,this.use_deck);
    }


}
