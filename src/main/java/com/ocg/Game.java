package com.ocg;

import com.ocg.Client.ClientCard;
import com.ocg.Client.ClientField;
import com.ocg.Client.DuelInfo;
import com.ocg.dataController.DataManager;


public class Game {
    private static Game instance = null;
    public boolean is_attacking;
    public boolean is_building;
    public boolean is_siding;
    public DuelInfo dInfo = new DuelInfo();
    public ClientField dField = new ClientField();
    public boolean gMutex; //使用信号量？
    boolean Initialize() {
        return true;
    }

    public static Game getInstance(){
        if(instance == null){
            return new Game();
        }
        return instance;
    }
    public int LocalPlayer(int player){
        return dInfo.isFirst ? player : 1 - player;
    }

}
