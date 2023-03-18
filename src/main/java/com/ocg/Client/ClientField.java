package com.ocg.Client;

import com.ocg.ChainInfo;
import com.ocg.dataController.DataManager;

import java.util.*;

import static com.ocg.Constants.*;

public class ClientField {
    public Vector<ClientCard>[] deck = new Vector[2];
    public Vector<ClientCard>[] hand = new Vector[2];
    public Vector<ClientCard>[] mzone = new Vector[]{new Vector<>(), new Vector<>()};
    public Vector<ClientCard>[] szone = new Vector[]{new Vector<>(), new Vector<>()};
    public Vector<ClientCard>[] grave = new Vector[2];
    public Vector<ClientCard>[] remove = new Vector[2];
    public Vector<ClientCard>[] extra = new Vector[2];
    public Set<ClientCard> overlay_cards = new HashSet<>();
    public Vector<ClientCard> summonable_cards = new Vector<>();
    public Vector<ClientCard> spsummonable_cards = new Vector<>();
    public Vector<ClientCard> msetable_cards = new Vector<>();
    public Vector<ClientCard> ssetable_cards = new Vector<>();
    public Vector<ClientCard> reposable_cards = new Vector<>();
    public Vector<ClientCard> activatable_cards = new Vector<>();

    public Vector<ClientCard> attackable_cards = new Vector<>();
    public Vector<ClientCard> conti_cards = new Vector<>();
    public Vector<Map<Integer, Integer>> activatable_descs = new Vector<>();
    public Vector<Integer> select_options = new Vector<>();
    public Vector<Integer> select_options_index = new Vector<>();
    public Vector<ChainInfo> chains = new Vector<>();
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
    public boolean select_panalmode;
    public boolean select_ready;
    public int announce_count;
    public int select_counter_count;
    public int select_counter_type;
    public Vector<ClientCard> selectable_cards;
    public Vector<ClientCard> selected_cards;
    public Set<ClientCard> selectsum_cards;
    public Vector<ClientCard> selectsum_all;
    public Vector<Integer> declare_opcodes;
    public Vector<ClientCard> display_cards;
    public Vector<Integer> sort_list;
    public Map<Integer, Integer> player_desc_hint = new HashMap<>(2);
    public boolean grave_act;
    public boolean remove_act;
    public boolean deck_act;
    public boolean extra_act;
    public boolean[] pzone_act = new boolean[2];
    public boolean conti_act;
    public boolean chain_forced;
    public ChainInfo current_chain;
    public boolean last_chain;
    public boolean deck_reversed;
    public boolean conti_selecting;
    public boolean cant_check_grave;

    public Vector<Integer> ancard;
    public int hovered_controler;
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
        hovered_controler = 0;
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
            mzone[p].setSize(7);
            szone[p].setSize(8);
            deck[p]=new Vector<>();
            hand[p]=new Vector<>();
        }
    }

    public void Initial(int player, int deckc, int extrac) {
        ClientCard pcard;
        for (int i = 0; i < deckc; i++) {
            pcard = new ClientCard();
            pcard.owner = player;
            pcard.controller = player;
            pcard.location = LOCATION_DECK;
            pcard.sequence = i;
            pcard.position = POS_FACEDOWN_DEFENSE;
            deck[player].add(pcard);
            GetCardLocation(pcard);
        }
        for (int i = 0; i < extrac; i++) {
            pcard = new ClientCard();
            pcard.owner = player;
            pcard.controller = player;
            pcard.location = LOCATION_EXTRA;
            pcard.sequence = i;
            pcard.position = POS_FACEDOWN_DEFENSE;
            deck[player].add(pcard);
            GetCardLocation(pcard);
        }
    }

    public void GetCardLocation(ClientCard pcard) {
    }

    public ClientCard GetCard(int controller, int location, int sequence) {
        Vector<ClientCard> list = null;
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
            return null;
        }
        if (sequence >= list.size()) return null;
        return list.get(sequence);
    }

    public ClientCard GetCard(int controller, int location, int sequence, int sub_seq) {
        Vector<ClientCard> list = null;
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

    public void MoveCard(ClientCard pcard, int frame) {
        String name = "";
        if(DataManager.GetDesc(pcard.code)!=null){
            name = DataManager.GetDesc(pcard.code).name;
        }
        System.out.println("move card " + pcard.code + " to frame " + frame);
    }

    public void AddCard(ClientCard pcard, int controller, int location, int sequence) {
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
                mzone[controller].set(sequence, pcard);
                break;
            }
            case LOCATION_SZONE -> {
                szone[controller].set(sequence, pcard);
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
                        System.out.println("将card " + DataManager.GetDesc(pcard.code) + " 加入额外卡组");
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
    public void Clear(){

    }
}
