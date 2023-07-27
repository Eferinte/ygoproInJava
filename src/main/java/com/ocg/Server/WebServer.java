package com.ocg.Server;

import com.ocg.Game;
import com.ocg.Moment.Server.LogicServer;
import com.ocg.SingleDuel;
import com.ocg.core.OCGDll;
import com.ocg.dataController.DataManager;

public class WebServer {

    public static Game mainGame;
    static int menu_card = 2; //TODO ClientCard
    public static SingleDuel duel_mode;
    public static LogicServer server;

    public static void main(String[] args) {
        if (81 == OCGDll.INSTANCE.jna_test_multi(9, 9)) System.out.println("JNA 连接成功");
//        mainGame = new Game();
        DataManager dm = new DataManager();
        duel_mode = new SingleDuel(false);
        System.out.println("opened");
    }
}
