package com.ocg.Client;

import com.ocg.ChainInfo;

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
    public Set<ClientCard> overlaycards = new HashSet<>();
    public Vector<ClientCard> summonable_cards = new Vector<>() ;
    public Vector<ClientCard> spsummonable_cards = new Vector<>();
    public Vector<ClientCard> msetablecards = new Vector<>();
    public Vector<ClientCard> ssetablecards = new Vector<>();
    public Vector<ClientCard> spsummonable = new Vector<>();
    public Vector<ClientCard> reposable = new Vector<>();
    public Vector<ClientCard> activatable = new Vector<>();

    public Vector<ClientCard> attackable = new Vector<>();
    public Vector<ClientCard> conticards = new Vector<>();
    public Vector<Map<Integer, Integer>> activatabledescs = new Vector<>();
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
        }
    }

    public void Initial(int player, int deckc, int extrac) {
        ClientCard pcard;
        for(int i=0;i<deckc;i++){
            pcard = new ClientCard();
            pcard.owner= player;
            pcard.controller = player;
            pcard.location = LOCATION_DECK;
            pcard.sequence = i;
            pcard.position = POS_FACEDOWN_DEFENSE;
            deck[player].add(pcard);
            GetCardLocation(pcard);
        }
        for(int i=0;i<extrac;i++){
            pcard = new ClientCard();
            pcard.owner= player;
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
}
