package com.ocg;


import com.ocg.Client.DuelClient;
import com.ocg.Server.SimpleServer;
import com.ocg.core.OCGDll;
import com.ocg.dataController.DataManager;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Main {

    public static Game mainGame;
    public static DuelPlayer YuSei;
    static DuelPlayer Jack;
    static int menu_card = 2; //TODO ClientCard
    public static SingleDuel duel_mode ;

    public static void main(String[] args) throws Exception {

        if (81 == OCGDll.INSTANCE.jna_test_multi(9, 9)) System.out.println("JNA 连接成功");
        System.out.println("Hello YGO!");
        mainGame = new Game();
        DataManager dm = new DataManager();

        duel_mode = new SingleDuel(false);
//        YuSei.LoadDefault();
//        Jack.LoadDefault();

//        duel_mode.UpdateDeck(YuSei);
//        duel_mode.UpdateDeck(Jack);
//        duel_mode.StartDuel();
//        duel_mode.TPResult();
        // 开启服务
        SimpleServer.RunSocketServer(mainGame,duel_mode);
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