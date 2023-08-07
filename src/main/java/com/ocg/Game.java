package com.ocg;

import com.ocg.Client.ClientCard;
import com.ocg.Client.ClientField;
import com.ocg.Client.DuelInfo;
import com.ocg.Moment.Client.LogicClient;
import com.ocg.dataController.DataManager;


public class Game {
    public boolean is_attacking;
    public boolean is_building;
    public boolean is_siding;
    public boolean always_chain;
    public boolean ignore_chain;
    public boolean chain_when_avail;
    public DuelInfo dInfo = new DuelInfo();
    public ClientField dField = new ClientField();
    public boolean gMutex; //使用信号量？

    public boolean btnBP = false;
    public boolean btnM2 = false;
    public boolean btnEP = false;



    boolean Initialize() {
        return true;
    }


    public int LocalPlayer(int player){
        return dInfo.isFirst ? player : 1 - player;
    }

}
