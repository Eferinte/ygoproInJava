package com.ocg;

import com.ocg.Client.ClientField;
import com.ocg.Client.DuelInfo;

public class Game {
    public DuelInfo dInfo = new DuelInfo();
    public ClientField dField = new ClientField();
    public boolean mutex; //使用信号量？
    boolean Initialize() {
        return true;
    }

    public static Game mainGame = new Game();
    public int LocalPlayer(int player){
        return dInfo.isFirst ? player : 1 - player;
    }
}
