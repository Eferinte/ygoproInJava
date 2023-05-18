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
    public abstract void RefreshMzone(int player,int flag,int use_cache);
    public abstract void RefreshSzone(int player,int flag,int use_cache);
    public abstract void RefreshHand(int player,int flag,int use_cache);
    public abstract void RefreshGrave(int player,int flag,int use_cache);
    public abstract void RefreshExtra(int player,int flag,int use_cache);

}
