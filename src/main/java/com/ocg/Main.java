package com.ocg;


import com.ocg.Client.DuelClient;
import com.ocg.Client.EventHandler;
import com.ocg.Moment.Moment;
import com.ocg.Server.SimpleWebSocketServer;
import com.ocg.core.OCGDll;
import com.ocg.dataController.DataManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static Game mainGame;
    static int menu_card = 2; //TODO ClientCard
    public static SingleDuel duel_mode;
    public static SimpleWebSocketServer server;

    public static void main(String[] args) throws Exception {

        if (81 == OCGDll.INSTANCE.jna_test_multi(9, 9)) System.out.println("JNA 连接成功");

//        mainGame = new Game();
        DataManager dm = new DataManager();
        duel_mode = new SingleDuel(false);
        server = new SimpleWebSocketServer(9999, mainGame, duel_mode);
        server.start();
        System.out.println("server opened");
    }
}