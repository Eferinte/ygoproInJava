package com.ocg;


import com.ocg.core.OCGDll;
import com.ocg.dataController.DataManager;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;


// target=  1100111110001101011010111
// debug=   11010111000110101001111100000001

public class Main {
    public static void main(String[] args) {
        if (81 == OCGDll.INSTANCE.jna_test_multi(9, 9)) System.out.println("JNA 连接成功");
        System.out.println("Hello YGO!");
        Game mainGame = new Game();
        DataManager dm = new DataManager();

        SingleDuel singleDuel = new SingleDuel(false);
        DuelPlayer YuSei = new DuelPlayer();
        DuelPlayer Jack = new DuelPlayer();
        YuSei.LoadDefault();
        Jack.LoadDefault();
        singleDuel.UpdateDeck(YuSei);
        singleDuel.UpdateDeck(Jack);
        singleDuel.StartDuel();
        singleDuel.TPResult();
    }
}