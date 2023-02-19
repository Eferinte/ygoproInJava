package com.ocg;

public class HostInfo {
    public int lflist;
    public char rule;
    public char mode;
    public char duel_rule;
    public boolean no_check_deck;
    public boolean no_shuffle_deck;
    public int start_lp;
    public char start_hand;
    public char draw_count;
    public short time_limit;

    public HostInfo(int lflist, char rule, char mode, char duel_rule, boolean no_check_deck, boolean no_shuffle_deck, int start_lp, char start_hand, char draw_count, short time_limit) {
        this.lflist = lflist;
        this.rule = rule;
        this.mode = mode;
        this.duel_rule = duel_rule;
        this.no_check_deck = no_check_deck;
        this.no_shuffle_deck = no_shuffle_deck;
        this.start_lp = start_lp;
        this.start_hand = start_hand;
        this.draw_count = draw_count;
        this.time_limit = time_limit;
    }
}
