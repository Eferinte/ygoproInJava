package com.ocg.dataController;

public class CardDataC extends CardData {
    public int ot;
    public int category;


    public CardDataC(int code, int ot, int alias, long setcode, int type, int attack, int defense, int level, int race, int attribute, int category) {

        super(code, alias, setcode, type, level, attribute, race, attack, defense);
        this.ot = ot;
        this.category = category;
    }
}
