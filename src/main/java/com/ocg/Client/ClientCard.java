package com.ocg.Client;

import com.ocg.Game;
import com.ocg.utils.BitReader;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import static com.ocg.Constants.*;
import static com.ocg.Main.mainGame;

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
    public String atkstring;
    public String defstring;
    public String lvstring;
    public String linkstring;
    public String lscstring;
    public String rscstring;
    // 新增属性
    public String uuid;

    public ClientCard() {
        uuid = UUID.randomUUID().toString();
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
        atkstring = "";
        defstring = "";
        lvstring = "";
        linkstring = "0";
        rscstring = "";
        lscstring = "";
        overlayTarget = null;
        equipTarget = null;
    }

    public void SetCode(int code) {
        if (location == LOCATION_HAND && this.code != code) mainGame.dField.MoveCard(this, 5);
        this.code = code;
    }

    public void UpdateInfo(byte[] buf) {
        BitReader buffer = new BitReader(buf);
        int flag = buffer.readInt32();
        if (flag == 0) return;
        int pdata;
        if ((flag & QUERY_CODE) != 0) {
            pdata = buffer.readInt32();
            // TODO unsigned 必要吗?
            if ((location == LOCATION_HAND) && (pdata != code)) {
                code = pdata;
                mainGame.dField.MoveCard(this, 5);
            } else code = pdata;
        }
        if ((flag & QUERY_POSITION) != 0) {
            pdata = (buffer.readInt32() >> 24) & 0xff;
            // TODO (u8)?
            if (((location & (LOCATION_EXTRA | LOCATION_REMOVED)) != 0) && (pdata != position)) {
                position = pdata;
                mainGame.dField.MoveCard(this, 1);
            } else position = pdata;
        }
        if ((flag & QUERY_ALIAS) != 0) {
            alias = buffer.readInt32();
        }
        if ((flag & QUERY_TYPE) != 0) {
            type = buffer.readInt32();
        }
        if ((flag & QUERY_LEVEL) != 0) {
            pdata = buffer.readInt32();
            // TODO 此处unsigned必要吗
            if (level != pdata) {
                level = pdata;
                lvstring = "L" + level;
            }
            alias = buffer.readInt32();
        }
        if ((flag & QUERY_RANK) != 0) {
            pdata = buffer.readInt32();
            if ((pdata != 0 && (rank != pdata))) {
                rank = pdata;
                lvstring = "R" + rank;
            }
        }
        if ((flag & QUERY_ATTRIBUTE) != 0) {
            attribute = buffer.readInt32();
        }
        if ((flag & QUERY_RACE) != 0) {
            race = buffer.readInt32();
        }
        if ((flag & QUERY_ATTACK) != 0) {
            attack = buffer.readInt32();
            if (attack < 0) {
                // TODO 待测试
                atkstring = "?0";
            } else atkstring = String.valueOf(attack);
        }
        if ((flag & QUERY_DEFENSE) != 0) {
            defense = buffer.readInt32();
            if ((type & TYPE_LINK) != 0) {
                defstring = "-0";
            } else if (defense < 0) {
                defstring = "?0";
            }
            defstring = String.valueOf(defense);
        }
        if((flag & QUERY_BASE_ATTACK)!=0){
            base_attack = buffer.readInt32();
        }
        if((flag & QUERY_BASE_DEFENSE)!=0){
            base_defense = buffer.readInt32();
        }
        if((flag & QUERY_REASON)!=0){
            buffer.step(4);
        }
        if((flag & QUERY_EQUIP_CARD)!=0){
            int c = buffer.readInt8();
            int l = buffer.readInt8();
            int s = buffer.readInt8();
            buffer.step();
            ClientCard ecard = mainGame.dField.GetCard(mainGame.LocalPlayer(c),l,s);
            equipTarget = ecard;
            ecard.equipped.add(this);
        }
        if((flag & QUERY_TARGET_CARD)!=0){
            int count = buffer.readInt32();
            for(int i=0;i<count;i++){
                int c = buffer.readInt8();
                int l = buffer.readInt8();
                int s = buffer.readInt8();
                buffer.step();
                ClientCard tcard = mainGame.dField.GetCard(mainGame.LocalPlayer(c),l,s);
                cardTarget.add(tcard);
                tcard.ownerTarget.add(this);

            }
        }
        if((flag&QUERY_OVERLAY_CARD)!=0){
            int count = buffer.readInt32();
            for(int i=0;i<count;i++){
                overlayed.get(i).SetCode(buffer.readInt32());
            }
        }
        if((flag & QUERY_COUNTERS)!=0){
            int count = buffer.readInt32();
            for(int i=0;i<count;i++){
                int ctype = buffer.readInt16();
                int ccount = buffer.readInt16();
                counters.put(ctype,ccount);
            }
        }
        if((flag & QUERY_OWNER)!=0){
            owner = buffer.readInt32();
        }
        if((flag & QUERY_STATUS)!=0){
            status = buffer.readInt32();
        }
        if((flag & QUERY_LSCALE)!=0){
            lscale = buffer.readInt32();
            lscstring = String.valueOf(lscale);
        }

        if((flag & QUERY_RSCALE)!=0){
            rscale = buffer.readInt32();
            rscstring = String.valueOf(rscale);
        }
        if((flag & QUERY_LINK)!=0){
            pdata = buffer.readInt32();
            if(link != pdata){
                link = pdata;
            }
            linkstring = "L-"+link;
            pdata = buffer.readInt32();
            if(link_marker != pdata){
                link_marker = pdata;
            }
        }
    }
}
