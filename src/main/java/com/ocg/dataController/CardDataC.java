package com.ocg.dataController;

public class CardDataC extends CardData {
    public int ot;
    public int category;


    public CardDataC(int code, int alias, long setcode, int type, int level, int attribute, int race, int attack, int defense, int lscale, int rscale, int link_marker, int ot, int category) {

        super(code, alias, setcode, type, level, attribute, race, attack, defense, lscale, rscale, link_marker);
        this.ot = ot;
        this.category = category;
    }
}
