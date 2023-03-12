package com.ocg;


import com.ocg.CallbackImpls.CardReaderImpl;
import com.ocg.CallbackImpls.MessageHandleImpl;
import com.ocg.dataController.DataManager;
import com.ocg.dataController.DeckReader;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;


// target=  1100111110001101011010111
// debug=   11010111000110101001111100000001

public class Main {
    public static void main(String[] args) {

        if (81 == OCGDll.INSTANCE.jna_test_multi(9, 9)) System.out.println("JNA 连接成功");
        System.out.println("Hello YGO!");
        DataManager dm = new DataManager();
        Pointer test = new Memory(100);
        OCGDll.INSTANCE.jna_test_log(test);
        System.out.println(test.getString(0));
//        OCGDll.INSTANCE.set_message_handler(new MessageHandleImpl());
//        OCGDll.INSTANCE.set_card_reader(new CardReaderImpl());
//        long pduel = OCGDll.INSTANCE.create_duel(39);
//        OCGDll.INSTANCE.set_player_info(pduel, 0, 8000, 5, 1);
//        OCGDll.INSTANCE.set_player_info(pduel, 1, 8000, 5, 1);
//        OCGDll.INSTANCE.new_card(pduel, 8491308, (byte) 0, (byte) 0, Constants.LOCATION_DECK, (byte) 0, Constants.POS_FACEDOWN_DEFENSE);
        Pointer str = new Memory(1024);
//        str.setString(0,"test");
//        OCGDll.INSTANCE.logger(str);

        SingleDuel singleDuel = new SingleDuel(false);
        DuelPlayer YuSei = new DuelPlayer();
        DuelPlayer Jack = new DuelPlayer();
        YuSei.LoadDefault();
        Jack.LoadDefault();
        singleDuel.JoinGame(YuSei);
        singleDuel.JoinGame(Jack);
        singleDuel.StartDuel();
        singleDuel.TPResult();
    }
}