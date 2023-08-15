package com.ocg.Client;

import com.ocg.ChainInfo;
import com.ocg.Moment.Client.ClientInterface;
import com.ocg.Moment.Client.LogicClient;
import com.ocg.Moment.Client.SelectOption;
import com.ocg.dataController.DataManager;
import com.ocg.utils.Convertor;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import static com.ocg.Constants.*;

public class ClientField {
    public ArrayList<ClientCard>[] deck = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    public ArrayList<ClientCard>[] hand = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    //    public ClientCard[][] mzone = {{null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}};
//    public ClientCard[][] szone = {{null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}};
    public ArrayList<ClientCard>[] mzone = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    public ArrayList<ClientCard>[] szone = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    public ArrayList<ClientCard>[] grave = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    public ArrayList<ClientCard>[] remove = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    public ArrayList<ClientCard>[] extra = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    public Set<ClientCard> overlay_cards = new HashSet<>();
    public ArrayList<ClientCard> summonable_cards = new ArrayList<>();
    public ArrayList<ClientCard> spsummonable_cards = new ArrayList<>();
    public ArrayList<ClientCard> msetable_cards = new ArrayList<>();
    public ArrayList<ClientCard> ssetable_cards = new ArrayList<>();
    public ArrayList<ClientCard> reposable_cards = new ArrayList<>();
    public ArrayList<ClientCard> activatable_cards = new ArrayList<>();

    public ArrayList<ClientCard> attackable_cards = new ArrayList<>();
    public ArrayList<ClientCard> conti_cards = new ArrayList<>();
    public ArrayList<Map<Integer, Integer>> activatable_descs = new ArrayList<>();
    public ArrayList<Integer> select_options = new ArrayList<>();
    public ArrayList<Integer> select_options_index = new ArrayList<>();
    public ArrayList<ChainInfo> chains = new ArrayList<>();
    public int[] extra_p_count = new int[2];

    public long selected_option;
    public ClientCard attacker;
    public ClientCard attack_target;
    public int disable_field;
    public int selectable_field;
    public int selected_field;
    public int select_min;
    public int select_max;
    public int must_select_count;
    public int select_sumval;
    public int select_mode;
    public boolean select_cancelable;
    public boolean select_panalmode;
    public boolean select_ready;
    public int announce_count;
    public int select_counter_count;
    public int select_counter_type;
    public ArrayList<ClientCard> selectable_cards = new ArrayList<>();
    public ArrayList<ClientCard> selected_cards = new ArrayList<>();
    public Set<ClientCard> selectsum_cards = new HashSet<>();
    public ArrayList<ClientCard> selectsum_all = new ArrayList<>();
    public ArrayList<Integer> declare_opcodes = new ArrayList<>();
    public ArrayList<ClientCard> display_cards = new ArrayList<>();
    public ArrayList<Integer> sort_list = new ArrayList<>();
    public Map<Integer, Integer>[] player_desc_hint = new Map[]{new HashMap(), new HashMap()};
    public boolean grave_act;
    public boolean remove_act;
    public boolean deck_act;
    public boolean extra_act;
    public boolean[] pzone_act = new boolean[2];
    public boolean conti_act;
    public boolean chain_forced;
    public ChainInfo current_chain = new ChainInfo();
    public boolean last_chain;
    public boolean deck_reversed;
    public boolean conti_selecting;
    public boolean cant_check_grave;

    public Vector<Integer> ancard;
    public int hovered_controller;
    public int hovered_location;
    public long hovered_sequence;
    public int command_controller;
    public int command_location;
    public long command_sequence;
    public ClientCard hovered_card;
    public int hovered_player;
    public ClientCard clicked_card;
    public ClientCard command_card;
    public ClientCard highlighting_card;
    public ClientCard menu_card;
    public int list_command;


    public ClientField() {
        hovered_card = null;
        clicked_card = null;
        highlighting_card = null;
        menu_card = null;
        hovered_controller = 0;
        hovered_location = 0;
        hovered_sequence = 0;
        selectable_field = 0;
        selected_field = 0;
        deck_act = false;
        grave_act = false;
        remove_act = false;
        extra_act = false;
        pzone_act[0] = false;
        pzone_act[1] = false;
        conti_act = false;
        deck_reversed = false;
        conti_selecting = false;
        cant_check_grave = false;
        for (int p = 0; p < 2; ++p) {
            mzone[p] = new ArrayList<>();
            szone[p] = new ArrayList<>();
            for (int i = 0; i < 7; i++) mzone[p].add(null);
            for (int i = 0; i < 8; i++) szone[p].add(null);
            deck[p] = new ArrayList<>();
            hand[p] = new ArrayList<>();
            extra[p] = new ArrayList<>();
        }
    }

    public void initial(int player, int deckc, int extrac) {
        ClientCard pcard;
        for (int i = 0; i < deckc; i++) {
            pcard = new ClientCard();
            pcard.owner = player;
            pcard.controller = player;
            pcard.location = LOCATION_DECK;
            pcard.sequence = i;
            pcard.position = POS_FACEDOWN_DEFENSE;
            deck[player].add(pcard);
            getCardLocation(pcard);
        }
        for (int i = 0; i < extrac; i++) {
            pcard = new ClientCard();
            pcard.owner = player;
            pcard.controller = player;
            pcard.location = LOCATION_EXTRA;
            pcard.sequence = i;
            pcard.position = POS_FACEDOWN_DEFENSE;
            extra[player].add(pcard);
            getCardLocation(pcard);
        }
    }

    public void getCardLocation(ClientCard pcard) {
    }

    public ClientCard getCard(int controller, int location, int sequence) {
        ArrayList<ClientCard> list = null;
        boolean is_xyz = (location & LOCATION_OVERLAY) != 0;
        location &= 0x7f;
        switch (location) {
            case LOCATION_DECK -> list = deck[controller];
            case LOCATION_HAND -> list = hand[controller];
            case LOCATION_MZONE -> list = mzone[controller];
            case LOCATION_SZONE -> list = szone[controller];
            case LOCATION_GRAVE -> list = grave[controller];
            case LOCATION_REMOVED -> list = remove[controller];
            case LOCATION_EXTRA -> list = extra[controller];
        }
        if (list == null) return null;
        if (is_xyz) {
            return null;
        }
        if (sequence >= list.size()) return null;
        return list.get(sequence);
    }

    public ClientCard getCard(int controller, int location, int sequence, int sub_seq) {
        ArrayList<ClientCard> list = null;
        boolean is_xyz = (location & LOCATION_OVERLAY) != 0;
        location &= 0x7f;
        switch (location) {
            case LOCATION_DECK -> {
                list = deck[controller];
                break;
            }
            case LOCATION_HAND -> {
                list = hand[controller];
                break;
            }
            case LOCATION_MZONE -> {
                list = mzone[controller];
                break;
            }
            case LOCATION_SZONE -> {
                list = szone[controller];
                break;
            }
            case LOCATION_GRAVE -> {
                list = grave[controller];
                break;
            }
            case LOCATION_REMOVED -> {
                list = remove[controller];
                break;
            }
            case LOCATION_EXTRA -> {
                list = extra[controller];
                break;
            }
        }
        if (list == null) return null;
        if (is_xyz) {
            if (sequence >= list.size()) return null;
            ClientCard scard = list.get(sequence);
            if (scard != null && (scard.overlayed.size() > sub_seq)) return scard.overlayed.get(sub_seq);
            return null;
        }
        if (sequence >= list.size()) return null;
        return list.get(sequence);
    }

    public void moveCard(ClientCard pcard, int frame) {
        String name = "";
        if (DataManager.getCardDesc(pcard.code) != null) {
            name = DataManager.getCardDesc(pcard.code).name;
        }
        System.out.println("move card " + pcard.code + " to frame " + frame);
    }

    public void removeCard(int controller, int location, int sequence) {
        switch (location) {
            case LOCATION_DECK -> {
                deck[controller].remove(sequence);
                for (int i = sequence; i < deck[controller].size(); i++) {
                    deck[controller].get(i).sequence--;
                }
            }
            case LOCATION_HAND -> {
                hand[controller].remove(sequence);
                for (int i = sequence; i < hand[controller].size(); i++) {
                    hand[controller].get(i).sequence--;
                }
            }
            case LOCATION_MZONE -> {
                mzone[controller].set(sequence, null);
            }
            case LOCATION_SZONE -> {
                szone[controller].set(sequence, null);
            }
            case LOCATION_GRAVE -> {
                grave[controller].remove(sequence);
                for (int i = sequence; i < grave[controller].size(); i++) {
                    grave[controller].get(i).sequence--;
                }
            }
            case LOCATION_REMOVED -> {
                remove[controller].remove(sequence);
                for (int i = sequence; i < remove[controller].size(); i++) {
                    remove[controller].get(i).sequence--;
                }
            }
            case LOCATION_EXTRA -> {
                if ((extra[controller].get(sequence).position & POS_FACEUP) != 0) {
                    extra_p_count[controller]--;
                }
                extra[controller].remove(sequence);
                for (int i = sequence; i < extra[controller].size(); i++) {
                    extra[controller].get(i).sequence--;
                }
            }
        }
    }

    public void addCard(ClientCard pcard, int controller, int location, int sequence) {
        pcard.controller = controller;
        pcard.location = location;
        pcard.sequence = sequence;
        switch (location) {
            case LOCATION_DECK -> {
                if (sequence != 0 || deck[controller].size() == 0) {
                    deck[controller].add(pcard);
                    pcard.sequence = deck[controller].size() - 1;
                } else {
                    deck[controller].add(null);
                    for (int i = deck[controller].size() - 1; i > 0; i--) {
                        deck[controller].set(i, deck[controller].get(i - 1));
                        deck[controller].get(i).sequence++;
                    }
                    deck[controller].set(0, pcard);
                    pcard.sequence = 0;
                }
                pcard.is_reversed = false;
            }
            case LOCATION_HAND -> {
                hand[controller].add(pcard);
                pcard.sequence = hand[controller].size() - 1;
            }
            case LOCATION_MZONE -> {
                mzone[controller].set(sequence, pcard);
            }
            case LOCATION_SZONE -> {
                szone[controller].set(sequence, pcard);
            }
            case LOCATION_GRAVE -> {
                grave[controller].add(pcard);
                pcard.sequence = grave[controller].size() - 1;
            }
            case LOCATION_REMOVED -> {
                remove[controller].add(pcard);
                pcard.sequence = remove[controller].size() - 1;
            }
            case LOCATION_EXTRA -> {
                if (extra_p_count[controller] == 0 || (pcard.position & POS_FACEUP) != 0) {
                    extra[controller].add(pcard);
                    pcard.sequence = extra[controller].size() - 1;
                } else {
                    extra[controller].add(null);
                    int p = extra[controller].size() - extra_p_count[controller] - 1;
                    for (int i = extra[controller].size() - 1; i > p; i--) {
                        extra[controller].set(i, extra[controller].get(i - 1));
                        extra[controller].get(i).sequence++;
                        //TODO 接口化
                        System.out.println("将card " + DataManager.getCardDesc(pcard.code) + " 加入额外卡组");
                    }
                    extra[controller].set(p, pcard);
                    pcard.sequence = p;
                }
                if ((pcard.position & POS_FACEUP) != 0) {
                    extra_p_count[controller]++;
                }
            }
        }
    }

    public void clear() {
        for (int i = 0; i < 2; i++) {
            deck[i].clear();
            hand[i].clear();
            mzone[i].clear();
            szone[i].clear();
            for (int q = 0; q < 7; q++) mzone[i].add(null);
            for (int q = 0; q < 8; q++) szone[i].add(null);
            grave[i].clear();
            remove[i].clear();
            extra[i].clear();
            extra_p_count[i] = 0;
            player_desc_hint[i].clear();
            pzone_act[i] = false;
        }
        overlay_cards.clear();
        chains.clear();
        activatable_cards.clear();
        summonable_cards.clear();
        spsummonable_cards.clear();
        msetable_cards.clear();
        ssetable_cards.clear();
        reposable_cards.clear();
        attackable_cards.clear();
        disable_field = 0;
        hovered_card = null;
        clicked_card = null;
        highlighting_card = null;
        menu_card = null;
        hovered_controller = 0;
        hovered_location = 0;
        hovered_sequence = 0;
        deck_act = false;
        grave_act = false;
        remove_act = false;
        extra_act = false;
        conti_act = false;
        deck_reversed = false;
        cant_check_grave = false;
    }

    public void hideMenu() {

    }

    public void showMenu() {
    }

    public void clearCommandFlag() {
        for (ClientCard card : activatable_cards) {
            card.cmdFlag = 0;
        }
        for (ClientCard card : summonable_cards) {
            card.cmdFlag = 0;
        }
        for (ClientCard card : spsummonable_cards) {
            card.cmdFlag = 0;
        }
        for (ClientCard card : msetable_cards) {
            card.cmdFlag = 0;
        }
        for (ClientCard card : ssetable_cards) {
            card.cmdFlag = 0;
        }
        for (ClientCard card : reposable_cards) {
            card.cmdFlag = 0;
        }
        for (ClientCard card : attackable_cards) {
            card.cmdFlag = 0;
        }
        conti_cards.clear();
        deck_act = false;
        extra_act = false;
        grave_act = false;
        remove_act = false;
        pzone_act[0] = false;
        pzone_act[1] = false;
        conti_act = false;
    }

    public void clearSelect() {
        for (ClientCard card : selectable_cards) {
            card.is_selectable = false;
            card.is_selected = false;
        }
        for (ClientCard card : selected_cards) {
            card.is_selectable = false;
            card.is_selected = false;
        }
        for (ClientCard card : selectsum_all) {
            card.is_selectable = false;
            card.is_selected = false;
        }
        for (ClientCard card : selectsum_cards) {
            card.is_selectable = false;
            card.is_selected = false;
        }
    }

    public void clearChainSelect() {
        for (ClientCard card : activatable_cards) {
            card.cmdFlag = 0;
            card.chain_code = 0;
            card.is_selectable = false;
            card.is_selected = false;
        }
        conti_cards.clear();
        deck_act = false;
        extra_act = false;
        grave_act = false;
        remove_act = false;
        conti_act = false;
    }

    public void updateFieldCard(int controller, int location, byte[] data) {
        ArrayList<ClientCard> list = null;
        switch (location) {
            case LOCATION_DECK -> {
                list = deck[controller];
            }
            case LOCATION_HAND -> {
                list = hand[controller];
            }
            case LOCATION_MZONE -> {
                list = mzone[controller];
            }
            case LOCATION_SZONE -> {
                list = szone[controller];
            }
            case LOCATION_GRAVE -> {
                list = grave[controller];
            }
            case LOCATION_REMOVED -> {
                list = remove[controller];
            }
            case LOCATION_EXTRA -> {
                list = extra[controller];
            }
        }
        if (list == null || list.size() == 0) return;
        int len;
        ByteBuffer buffer = ByteBuffer.allocate(data.length).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(data);
        buffer.flip();
        for (ClientCard card : list) {
            try {
                len = buffer.getInt();
                if (len > 8) card.updateInfo(Convertor.getRestBytes(buffer, len - 4));
            } catch (BufferUnderflowException e) {
                System.out.println(e);
                System.out.println(buffer);
            }
        }
    }

    public void updateCard(int controller, int location, int sequence, byte[] data) {
        ClientCard pCard = getCard(controller, location, sequence);
        if (pCard != null) {
            pCard.updateInfo(data);
        }
    }

    public void setResponseSelectedCards() {
        byte[] respBuf = new byte[64];
        respBuf[0] = (byte) selected_cards.size();
        for (int i = 0; i < selected_cards.size(); i++) {
            respBuf[i + 1] = (byte) selected_cards.get(i).select_seq;
        }
        LogicClient.setResponseB(respBuf, selected_cards.size() + 1);
    }

    public void showSelectSum(LogicClient logicClient, ClientInterface clientMove) {
        ArrayList<SelectOption> opts = new ArrayList<>();
        while (true) {
            if (checkSelectSum()) break;
            if ((selectsum_cards.size() == 0 || selectable_cards.size() == 0)) break;
            // select
            for (int i = 0; i < selectable_cards.size(); i++) {
                ClientCard card = selectable_cards.get(i);
                if (card.is_selected) opts.add(new SelectOption(
                        DataManager.getCardDesc(card.code).name + "(已选择)",
                        i
                ));
                else opts.add(new SelectOption(
                        DataManager.getCardDesc(card.code).name,
                        i
                ));
            }
            SelectOption ans = clientMove.select(opts);
            ClientCard card = selectable_cards.get(ans.value);
            card.is_selected = true;
            selected_cards.add(card);
        }
        // send
        setResponseSelectedCards();
        try {
            logicClient.sendResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean checkSelectSum() {
        Set<ClientCard> sel_able = new HashSet<>();
        for (ClientCard card : selectsum_all) {
            card.is_selectable = false;
            card.is_selected = false;
            sel_able.add(card);
        }
        for (int i = 0; i < selected_cards.size(); i++) {
            ClientCard card = selected_cards.get(i);
            if (i < must_select_count)
                card.is_selectable = false;
            else
                card.is_selectable = true;
            card.is_selected = true;
            sel_able.remove(card);
        }
        selectsum_cards.clear();
        if (select_mode == 0) {
            boolean ret = check_sel_sum_s(sel_able, 0, select_sumval);
            selectable_cards.clear();
            for (ClientCard card : selectsum_cards) {
                card.is_selectable = true;
                selectable_cards.add(card);
            }
            return ret;
        } else {
            int mm = -1, mx = -1, max = 0, sumc = 0;
            boolean ret = false;
            for (ClientCard card : selected_cards) {
                int op1 = card.opParam & 0xffff;
                int op2 = card.opParam >> 16;
                int opMin = (op2 > 0 && op1 > op2) ? op2 : op1;
                int opMax = (op2 > op1) ? op2 : op1;
                if (mm == -1 || opMin < mm) mm = opMin;
                if (mm == -1 || opMin < mx) mm = opMax;
                sumc += opMin;
                max += opMax;
            }
            if (select_sumval <= sumc) return true;
            if (select_sumval <= max && (select_sumval > (max - mx))) ret = true;
            Iterator<ClientCard> it = sel_able.iterator();
            while (it.hasNext()) {
                ClientCard card = it.next();
                int op1 = card.opParam & 0xffff;
                int op2 = card.opParam >> 16;
                int m = op1;
                int sums = sumc;
                sums += m;
                int ms = mm;
                if (ms == -1 || m < ms) ms = m;
                if (sums >= select_sumval) {
                    if (sums - ms < select_sumval)
                        selectsum_cards.add(card);
                } else {
                    sel_able.remove(card);
                    Iterator<ClientCard> it1 = sel_able.iterator();
                    if (check_min(it1, select_sumval - sums, select_sumval - sums + ms - 1))
                        selectsum_cards.add(card);
                }
                if (op2 == 0) continue;
                m = op2;
                sums = sumc;
                sums += m;
                ms = mm;
                if (ms == -1 || m < ms) ms = m;
                if (sums >= select_sumval) {
                    if (sums - ms < select_sumval)
                        selectsum_cards.add(card);
                } else {
                    sel_able.remove(card);
                    Iterator<ClientCard> it2 = sel_able.iterator();
                    if (check_min(it2, select_sumval - sums, select_sumval - sums + ms - 1))
                        selectsum_cards.add(card);
                }
            }
            selected_cards.clear();
            for (ClientCard card : selectsum_cards) {
                card.is_selectable = true;
                selectable_cards.add(card);
            }
            return ret;
        }
    }

    boolean check_min(Iterator<ClientCard> it, int min, int max) {
        if (!it.hasNext()) return false;
        ClientCard card = it.next();
        int op1 = card.opParam & 0xffff;
        int op2 = card.opParam >> 16;
        int m = (op2 > 0 && op1 > op2) ? op2 : op1;
        if (m >= min && m < max) return true;
        return (min > m && check_min(it, min - m, max - m)) ||
                check_min(it, min, max);
    }

    boolean check_sel_sum_s(Set<ClientCard> left, int index, int acc) {
        if (acc < 0) return false;
        if (index == selected_cards.size()) {
            if (acc == 0) {
                int count = selected_cards.size() - must_select_count;
                return count >= select_min && count <= select_max;
            }
            check_sel_sum_t(left, acc);
            return false;
        }
        int l = selected_cards.get(index).opParam;
        int l1 = l & 0xffff;
        int l2 = l >> 16;
        boolean res1 = false, res2 = false;
        res1 = check_sel_sum_s(left, index + 1, acc - l1);
        if (l2 > 0)
            res2 = check_sel_sum_s(left, index + 1, acc - l2);
        return res1 || res2;
    }

    void check_sel_sum_t(Set<ClientCard> left, int acc) {
        int count = selected_cards.size() + 1 - must_select_count;
        Iterator<ClientCard> it = left.iterator();
        while (it.hasNext()) {
            ClientCard card = it.next();
            if (selectsum_cards.contains(card)) continue;
            Set<ClientCard> testList = new HashSet<>(left);
            testList.remove(card);
            int l = card.opParam;
            int l1 = l & 0xffff;
            int l2 = l >> 16;
            if (check_sum(testList.iterator(), acc - l1, count) || (l2 > 0 && check_sum(left.iterator(), acc - l2, count))) {
                selectsum_cards.add(card);
            }
        }
    }

    boolean check_sum(Iterator<ClientCard> it, int acc, int count) {
        if (acc == 0) return count >= select_min && count <= select_max;
        if (acc < 0 || !it.hasNext()) return false;
        ClientCard card = it.next();
        int l = card.opParam;
        int l1 = l & 0xffff;
        int l2 = l >> 16;
        if ((l1 == acc || (l2 > 0 && l2 == acc)) && (count + 1 >= select_min) && (count + 1 <= select_max)) {
            return true;
        }
        return (acc > l1)
                && check_sum(it, acc - l1, count + 1)
                || (l2 > 0 && acc > l2 && check_sum(it, acc - l2, count + 1))
                || check_sum(it, acc, count);
    }
    public boolean checkSelectTribute(){
        Set<ClientCard> selAble = new HashSet<>();
        for(ClientCard card : selectsum_all){
            card.is_selectable = false;
            card.is_selected = false;
            selAble.add(card);
        }
        for(ClientCard card: selected_cards){
            card.is_selectable = true;
            card.is_selected = true;
            selAble.remove(card);
        }
        selectsum_cards.clear();
        boolean ret = check_sel_sum_trib_s(selAble,0,0);
        selectable_cards.clear();
        for(ClientCard card : selectsum_cards){
            card.is_selectable = true;
            selectable_cards.add(card);
        }
        return ret;
    }
    boolean check_sel_sum_trib_s(Set<ClientCard> left, int index, int acc) {
        if (acc > select_max) return false;
        if (index == selected_cards.size()) {
            check_sel_sum_trib_t(left,acc);
            return acc >= select_min && acc <= select_max;
        }
        int l = selected_cards.get(index).opParam;
        int l1 = l & 0xffff;
        int l2 = l >> 16;
        boolean res1 = false, res2 = false;
        res1 = check_sel_sum_trib_s(left, index + 1, acc + l1);
        if (l2 > 0)
            res2 = check_sel_sum_trib_s(left, index + 1, acc + l2);
        return res1 || res2;
    }
    void check_sel_sum_trib_t(Set<ClientCard> left,int acc){
        for(ClientCard card: left){
            if(selectsum_cards.contains(card)) continue;
            Set<ClientCard> testList = new HashSet<>(left);
            testList.remove(card);
            int l = card.opParam;
            int l1 = l & 0xffff;
            int l2 = l >> 16;
            if(check_sum_trib(testList.iterator(),acc + l1) ||
                    (l2 > 0 && check_sum_trib(testList.iterator(),acc+l2))
            ){
                selectsum_cards.add(card);
            }
        }
    }
    boolean check_sum_trib(Iterator<ClientCard> index,int acc){
        if(acc >= select_min && acc <= select_max) return true;
        if(acc >= select_max || !index.hasNext()) return false;
        ClientCard card = index.next();
        int l = card.opParam;
        int l1 = l & 0xffff;
        int l2 = l >> 16;
        if((acc + l1 >- select_min && acc + l1 <= select_max) || (acc + l2 >= select_min && acc +l2 <= select_max))
            return true;
        return check_sum_trib(index,acc + l1) || check_sum_trib(index,acc + l2) || check_sum_trib(index,acc);
    }
    public void showSelectCard(){
        // TODO
    }
}
