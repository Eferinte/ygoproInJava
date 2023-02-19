package com.ocg.dataController;

import com.sun.jna.Structure;

public class CardData {
    public int code;
    public int alias;
    public long setcode;
    public int type;
    public int level;
    public int attribute;
    public int race;
    public int attack;
    public int defense;
    public int lscale;
    public int rscale;
    public int link_marker;

    public CardData(int code, int alias, long setcode, int type, int level, int attribute, int race, int attack, int defense, int lscale, int rscale, int link_marker) {
        this.code = code;
        this.alias = alias;
        this.setcode = setcode;
        this.type = type;
        this.level = level;
        this.attribute = attribute;
        this.race = race;
        this.attack = attack;
        this.defense = defense;
        this.lscale = lscale;
        this.rscale = rscale;
        this.link_marker = link_marker;
    }
}
