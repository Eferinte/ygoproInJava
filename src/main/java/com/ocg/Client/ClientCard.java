package com.ocg.Client;

import com.ocg.Game;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import static com.ocg.Constants.LOCATION_HAND;

public class ClientCard {
    public boolean is_moving;
    public boolean is_fading;
    public boolean is_hovered;
    public boolean is_selectable;
    public boolean is_selected;
    public boolean is_showequip;
    public boolean is_showtarget;
    public boolean is_showchaintarget;
    public boolean is_highlighting;
    public boolean is_reversed;
    public int code;
    public int chain_code;
    public int alias;
    public int type;
    public int level;
    public int rank;
    public int link;
    public int attribute;
    public int race;
    public int attack;
    public int defense;
    public int base_attack;
    public int base_defense;
    public int lscale;
    public int rscale;
    public int link_marker;
    public int reason;
    public int select_seq;
    public int owner;
    public int controller;
    public int location;
    public int sequence;
    public int position;
    public int status;
    public int cHint;
    public int chValue;
    public int opParam;
    public int symbol;
    public int cmdFlag;
    public ClientCard overlayTarget;
    public Vector<ClientCard> overlayed;
    public ClientCard equipTarget;
    public Set<ClientCard> equipped;
    public Set<ClientCard> cardTarget;
    public Set<ClientCard> ownerTarget;
    public Map<Integer, Integer> counters;
    public Map<Integer, Integer> desc_hints;
    public String[] atkstring = new String[16];
    public String[] defstring = new String[16];
    public String[] lvstring = new String[16];
    public String[] linkstring = new String[16];
    public String[] lscstring = new String[16];
    public String[] rscstring = new String[16];

    public ClientCard() {
        is_moving = false;
        is_fading = false;
        is_hovered = false;
        is_selectable = false;
        is_selected = false;
        is_showequip = false;
        is_showtarget = false;
        is_showchaintarget = false;
        is_highlighting = false;
        status = 0;
        is_reversed = false;
        cmdFlag = 0;
        code = 0;
        chain_code = 0;
        location = 0;
        type = 0;
        alias = 0;
        level = 0;
        rank = 0;
        link = 0;
        race = 0;
        attribute = 0;
        attack = 0;
        defense = 0;
        base_attack = 0;
        base_defense = 0;
        lscale = 0;
        rscale = 0;
        link_marker = 0;
        position = 0;
        cHint = 0;
        chValue = 0;
        atkstring[0] = "";
        defstring[0] = "";
        lvstring[0] = "";
        linkstring[0] = "0";
        rscstring[0] = "";
        lscstring[0] = "";
        overlayTarget = null;
        equipTarget = null;
    }

    public void SetCode(int code, Game mainGame) {
        if (location == LOCATION_HAND && this.code != code) mainGame.dField.MoveCard(this, 5);
        this.code = code;
    }
}
