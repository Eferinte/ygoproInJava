package com.ocg.dataController;

public class CardString {
    public String name;
    public String text;
    public String[] desc = new String[16];

    public CardString(String name, String text, String[] desc) {
        this.name = name;
        this.text = text;
        this.desc = desc;
    }
}
