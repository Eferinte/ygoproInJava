package com.ocg.Client;

import com.ocg.dataController.DataManager;
import com.ocg.utils.ConstantDict.Dictionary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import static com.ocg.Constants.*;
import static com.ocg.Moment.Client.LogicClient.mainGame;

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
    public Vector<ClientCard> overlayed = new Vector<>();
    public ClientCard equipTarget;
    public Set<ClientCard> equipped = new HashSet<>();
    public Set<ClientCard> cardTarget = new HashSet<>();
    public Set<ClientCard> ownerTarget = new HashSet<>();
    public Map<Integer, Integer> counters = new HashMap<>();
    public Map<Integer, Integer> desc_hints = new HashMap<>();
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

    public void setCode(int code) {
        if (location == LOCATION_HAND && this.code != code) mainGame.dField.moveCard(this, 5);
        this.code = code;
    }

    public void updateInfo(byte[] buf) {
        ByteBuffer buffer = ByteBuffer.allocate(buf.length).order(ByteOrder.LITTLE_ENDIAN).put(buf).flip();
        int flag = buffer.getInt();
        if (flag == 0) return;
        int pdata;
        if ((flag & QUERY_CODE) != 0) {
            pdata = buffer.getInt();
            // TODO unsigned 必要吗?
            if ((location == LOCATION_HAND) && (pdata != code)) {
                code = pdata;
                mainGame.dField.moveCard(this, 5);
            } else code = pdata;
        }
        if ((flag & QUERY_POSITION) != 0) {
            pdata = (buffer.getInt() >> 24) & 0xff;
            // TODO (u8)?
            if (((location & (LOCATION_EXTRA | LOCATION_REMOVED)) != 0) && (pdata != position)) {
                position = pdata;
                mainGame.dField.moveCard(this, 1);
            } else position = pdata;
        }
        if ((flag & QUERY_ALIAS) != 0) {
            alias = buffer.getInt();
        }
        if ((flag & QUERY_TYPE) != 0) {
            type = buffer.getInt();
        }
        if ((flag & QUERY_LEVEL) != 0) {
            pdata = buffer.getInt();
            if (level != pdata) {
                level = pdata;
                lvstring = "L" + level;
            }
        }
        if ((flag & QUERY_RANK) != 0) {
            pdata = buffer.getInt();
            if ((pdata != 0 && (rank != pdata))) {
                rank = pdata;
                lvstring = "R" + rank;
            }
        }
        if ((flag & QUERY_ATTRIBUTE) != 0) {
            attribute = buffer.getInt();
        }
        if ((flag & QUERY_RACE) != 0) {
            race = buffer.getInt();
        }
        if ((flag & QUERY_ATTACK) != 0) {
            attack = buffer.getInt();
            if (attack < 0) {
                // TODO 待测试
                atkstring = "?0";
            } else atkstring = String.valueOf(attack);
        }
        if ((flag & QUERY_DEFENSE) != 0) {
            defense = buffer.getInt();
            if ((type & TYPE_LINK) != 0) {
                defstring = "-0";
            } else if (defense < 0) {
                defstring = "?0";
            }
            defstring = String.valueOf(defense);
        }
        if((flag & QUERY_BASE_ATTACK)!=0){
            base_attack = buffer.getInt();
        }
        if((flag & QUERY_BASE_DEFENSE)!=0){
            base_defense = buffer.getInt();
        }
        if((flag & QUERY_REASON)!=0){
            reason = buffer.getInt();
        }
        if((flag & QUERY_REASON_CARD)!=0){
            buffer.getInt();
        }
        if((flag & QUERY_EQUIP_CARD)!=0){
            int c = buffer.getInt();
            int l = buffer.getInt();
            int s = buffer.getInt();
            buffer.get();
            ClientCard ecard = mainGame.dField.getCard(mainGame.LocalPlayer(c),l,s);
            equipTarget = ecard;
            ecard.equipped.add(this);
        }
        if((flag & QUERY_TARGET_CARD)!=0){
            int count = buffer.getInt();
            for(int i=0;i<count;i++){
                int c = buffer.getInt();
                int l = buffer.getInt();
                int s = buffer.getInt();
                buffer.get();
                ClientCard tcard = mainGame.dField.getCard(mainGame.LocalPlayer(c),l,s);
                cardTarget.add(tcard);
                tcard.ownerTarget.add(this);

            }
        }
        if((flag&QUERY_OVERLAY_CARD)!=0){
            int count = buffer.getInt();
            for(int i=0;i<count;i++){
                overlayed.get(i).setCode(buffer.getInt());
            }
        }
        if((flag & QUERY_COUNTERS)!=0){
            int count = buffer.getInt();
            for(int i=0;i<count;i++){
                int ctype = buffer.getInt();
                int ccount = buffer.getInt();
                counters.put(ctype,ccount);
            }
        }
        if((flag & QUERY_OWNER)!=0){
            owner = buffer.getInt();
        }
        if((flag & QUERY_STATUS)!=0){
            status = buffer.getInt();
        }
        if((flag & QUERY_LSCALE)!=0){
            lscale = buffer.getInt();
            lscstring = String.valueOf(lscale);
        }
        if((flag & QUERY_RSCALE)!=0){
            rscale = buffer.getInt();
            rscstring = String.valueOf(rscale);
        }
        if((flag & QUERY_LINK)!=0){
            pdata = buffer.getInt();
            if(link != pdata){
                link = pdata;
            }
            linkstring = "L-"+link;
            pdata = buffer.getInt();
            if(link_marker != pdata){
                link_marker = pdata;
            }
        }
    }
    public void clearTarget(){
        for(ClientCard clientCard:cardTarget){
            clientCard.is_showtarget = false;
            clientCard.ownerTarget.remove(this);
        }
        for(ClientCard clientCard:ownerTarget){
            clientCard.is_showtarget = false;
            clientCard.cardTarget.remove(this);
        }
        cardTarget.clear();
        ownerTarget.clear();
    }

    @Override
    public String toString() {
        return String.valueOf(DataManager.getCardDesc(this.code)+"("+ Dictionary.getLocationDesc(location) +")");
    }
}
