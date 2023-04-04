package com.ocg.Client;

public class DuelInfo {
    public boolean isStarted;
    public boolean isFinished;
    public boolean isReplay;
    public boolean isReplaySkiping;
    public boolean isFirst;
    public boolean isTag;
    public boolean isSingleMode;
    public boolean is_shuffling;
    public boolean[] tag_player = new boolean[2];
    public int[] lp = new int[2];
    public int start_lp;
    public int duel_rule;
    public int turn;
    public int curMsg;
    public String hostname;
    public String clientname;
    public String hostname_tag;
    public String clientname_tag;
    public String[] strLP = new String[2];
    public char player_type;
    public char time_player;
    public short time_limit;
    public short[] time_left = new short[2];
    public boolean isReplaySwapped;
}
