package com.ocg;

public abstract class DuelMode {
    public int duel_stage;
    public static long pduel;
    public HostInfo host_info;
    public DuelMode(){
        host_info = new HostInfo();
    }
    public abstract void GetResponse(DuelPlayer dp, byte[]pdata, int len);
    public abstract void UpdateDeck();
    public abstract void StartDuel();
    public abstract void TPResult();

}
