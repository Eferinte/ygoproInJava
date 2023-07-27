package com.ocg.Client;

import com.ocg.ChainInfo;
import com.ocg.Moment.Client.LogicClient;
import com.ocg.dataController.DataManager;
import com.ocg.utils.Convertor;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import static com.ocg.Constants.*;

public class ClientField {
    public ArrayList<ClientCard>[] deck = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    public ArrayList<ClientCard>[] hand = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
    public ClientCard[][] mzone = {{null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}};
    public ClientCard[][] szone = {{null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}};
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
    public boolean select_mode;
    public boolean select_cancelable;
    public boolean select_panalmode;
    public boolean select_ready;
    public int announce_count;
    public int select_counter_count;
    public int select_counter_type;
    public ArrayList<ClientCard> selectable_cards = new ArrayList<>();
    public ArrayList<ClientCard> selected_cards = new ArrayList<>();
    public Set<ClientCard> selectsum_cards  = new HashSet<>();
    public ArrayList<ClientCard> selectsum_all  = new ArrayList<>();
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
//            mzone[p].setSize(7);
//            szone[p].setSize(8);
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
            case LOCATION_DECK -> {
                list = deck[controller];
                break;
            }
            case LOCATION_HAND -> {
                list = hand[controller];
                break;
            }
            case LOCATION_MZONE -> {
                list = new ArrayList<>(Arrays.asList(mzone[controller]));
                break;
            }
            case LOCATION_SZONE -> {
                list = new ArrayList<>(Arrays.asList(szone[controller]));
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
                list = new ArrayList<>(Arrays.asList(mzone[controller]));
                break;
            }
            case LOCATION_SZONE -> {
                list = new ArrayList<>(Arrays.asList(szone[controller]));
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
                mzone[controller][sequence] = null;
            }
            case LOCATION_SZONE -> {
                szone[controller][sequence] = null;
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
                break;
            }
            case LOCATION_HAND -> {
                hand[controller].add(pcard);
                pcard.sequence = hand[controller].size() - 1;
                break;
            }
            case LOCATION_MZONE -> {
                mzone[controller][sequence] = pcard;
                break;
            }
            case LOCATION_SZONE -> {
                szone[controller][sequence] = pcard;
                break;
            }
            case LOCATION_GRAVE -> {
                grave[controller].add(pcard);
                pcard.sequence = grave[controller].size() - 1;
                break;
            }
            case LOCATION_REMOVED -> {
                remove[controller].add(pcard);
                pcard.sequence = remove[controller].size() - 1;
                break;
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
                break;
            }
        }
    }

    public void clear() {
        for (int i = 0; i < 2; i++) {
            deck[i].clear();
            hand[i].clear();
            mzone[i]=new ClientCard[]{null,null,null,null,null,null,null};
            szone[i]=new ClientCard[]{null,null,null,null,null,null,null,null};
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
                list = new ArrayList<>(Arrays.asList(mzone[controller]));
            }
            case LOCATION_SZONE -> {
                list = new ArrayList<>(Arrays.asList(szone[controller]));
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
    public void setResponseSelectedCards(){
        byte[] respBuf = new byte[64];
        respBuf[0] = (byte)selectable_cards.size();
        for(int i=0;i< selectable_cards.size();i++){
            respBuf[i+1] = (byte)selectable_cards.get(i).select_seq;
        }
        LogicClient.setResponseB(respBuf,selected_cards.size()+1);
    }



}
