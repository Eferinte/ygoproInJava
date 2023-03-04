package com.ocg;

public class HostInfo {
    public int start_lp;
    public int start_hand;
    public int draw_count;
    public short time_limit;

    public HostInfo( int start_lp, int start_hand, int draw_count, short time_limit) {
        this.start_lp = start_lp;
        this.start_hand = start_hand;
        this.draw_count = draw_count;
        this.time_limit = time_limit;
    }
    public HostInfo(){
        this.start_lp = 8000;
        this.start_hand = 5;
        this.draw_count = 1;
        this.time_limit = 600;
    }

}
