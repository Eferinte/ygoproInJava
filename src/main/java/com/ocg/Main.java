package com.ocg;


import com.ocg.Client.DuelClient;
import com.ocg.Client.EventHandler;
import com.ocg.Server.SimpleWebSocketServer;
import com.ocg.core.OCGDll;
import com.ocg.dataController.DataManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class Main {

    public static Game mainGame;
    static int menu_card = 2; //TODO ClientCard
    public static SingleDuel duel_mode ;
    public static SimpleWebSocketServer server;

    public static void main(String[] args) throws Exception {

        if (81 == OCGDll.INSTANCE.jna_test_multi(9, 9)) System.out.println("JNA 连接成功");
        mainGame = new Game();
        DataManager dm = new DataManager();

        duel_mode = new SingleDuel(false);
        server = new SimpleWebSocketServer(9999,mainGame,duel_mode);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
//            String response ;
            String response = "???";
            t.sendResponseHeaders(200,response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("try summon ");
            for(int i=0;i<mainGame.dField.summonable_cards.size();i++){
                if(i==menu_card){
                    DuelClient.SetResponseI(i<<16);
                    DuelClient.SendResponse();//TODO curMsg赋值
                    break;
                }
            }
        }
    }
    static class HandleCheckGame implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "summon success";

            t.sendResponseHeaders(200,response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("try summon ");
            for(int i=0;i<mainGame.dField.summonable_cards.size();i++){
                if(i==menu_card){
                    DuelClient.SetResponseI(i<<16);
                    DuelClient.SendResponse();//TODO curMsg赋值
                    break;
                }
            }
        }
    }

}