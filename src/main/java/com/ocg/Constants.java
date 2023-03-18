package com.ocg;

public interface Constants {
    public static final byte DUEL_STAGE_BEGIN = 0;
    public static final byte DUEL_STAGE_FINGER = 1;
    public static final byte DUEL_STAGE_FIRSTGO = 2;
    public static final byte DUEL_STAGE_DUELING = 3;
    public static final byte DUEL_STAGE_SIDING = 4;
    public static final byte DUEL_STAGE_END = 5;

    public static final byte CTOS_RESPONSE = 0x1;
    public static final byte CTOS_UPDATE_DECK = 0x2;
    public static final byte CTOS_HAND_RESULT = 0x3;
    public static final byte CTOS_TP_RESULT = 0x4;
    public static final byte CTOS_PLAYER_INFO = 0x10;
    public static final byte CTOS_CREATE_GAME = 0x11;
    public static final byte CTOS_JOIN_GAME = 0x12;
    public static final byte CTOS_LEAVE_GAME = 0x13;
    public static final byte CTOS_SURRENDER = 0x14;
    public static final byte CTOS_TIME_CONFIRM = 0x15;
    public static final byte CTOS_CHAT = 0x16;


    public static final byte STOC_GAME_MSG = 0x1;
    public static final byte STOC_ERROR_MSG = 0x2;
    public static final byte STOC_SELECT_HAND = 0x3;
    public static final byte STOC_SELECT_TP = 0x4;
    public static final byte STOC_HAND_RESULT = 0x5;
    public static final byte STOC_TP_RESULT = 0x6;
    public static final byte STOC_CHANGE_SIDE = 0x7;
    public static final byte STOC_WAITING_SIDE = 0x8;
    public static final byte STOC_DECK_COUNT = 0x9;
    public static final byte STOC_CREATE_GAME = 0x11;
    public static final byte STOC_JOIN_GAME = 0x12;
    public static final byte STOC_TYPE_CHANGE = 0x13;
    public static final byte STOC_LEAVE_GAME = 0x14;
    public static final byte STOC_DUEL_START = 0x15;
    public static final byte STOC_DUEL_END = 0x16;
    public static final byte STOC_REPLAY = 0x17;
    public static final byte STOC_TIME_LIMIT = 0x18;
    public static final byte STOC_CHAT = 0x19;
    public static final byte STOC_HS_PLAYER_ENTER = 0x20;
    public static final byte STOC_HS_PLAYER_CHANGE = 0x21;
    public static final byte STOC_HS_WATCH_CHANGE = 0x22;


    // Locations
    public static final byte LOCATION_DECK = 0x01;
    public static final byte LOCATION_HAND = 0x02;
    public static final byte LOCATION_MZONE = 0x04;
    public static final byte LOCATION_SZONE = 0x08;
    public static final byte LOCATION_GRAVE = 0x10;
    public static final byte LOCATION_REMOVED = 0x20;
    public static final byte LOCATION_EXTRA = 0x40;
    public static final int LOCATION_OVERLAY = 0x80;
    public static final byte LOCATION_ONFIELD = 0x0c;
    public static final int LOCATION_FZONE = 0x100;
    public static final int LOCATION_PZONE = 0x200;

    // Positions

    public static final byte POS_FACEUP_ATTACK = 0x1;
    public static final byte POS_FACEDOWN_ATTACK = 0x2;
    public static final byte POS_FACEUP_DEFENSE = 0x4;
    public static final byte POS_FACEDOWN_DEFENSE = 0x8;
    public static final byte POS_FACEUP = 0x5;
    public static final byte POS_FACEDOWN = 0xa;
    public static final byte POS_ATTACK = 0x3;
    public static final byte POS_DEFENSE = 0xc;

    //Types
    public static final byte TYPE_MONSTER = 0x1;        //
    public static final byte TYPE_SPELL = 0x2;       //
    public static final byte TYPE_TRAP = 0x4;    //
    public static final byte TYPE_NORMAL = 0x10;    //
    public static final byte TYPE_EFFECT = 0x20;    //
    public static final byte TYPE_FUSION = 0x40;
    ;    //
    public static final int TYPE_RITUAL = 0x80;    //
    public static final int TYPE_TRAPMONSTER = 0x100;//
    public static final int TYPE_SPIRIT = 0x200;        //
    public static final int TYPE_UNION = 0x400;    //
    public static final int TYPE_DUAL = 0x800;    //
    public static final int TYPE_TUNER = 0x1000;//
    public static final int TYPE_SYNCHRO = 0x2000;    //
    public static final int TYPE_TOKEN = 0x4000;    //
    public static final int TYPE_QUICKPLAY = 0x10000;    //
    public static final int TYPE_CONTINUOUS = 0x20000;    //
    public static final int TYPE_EQUIP = 0x40000;    //
    public static final int TYPE_FIELD = 0x80000;    //
    public static final int TYPE_COUNTER = 0x100000;    //
    public static final int TYPE_FLIP = 0x200000;//
    public static final int TYPE_TOON = 0x400000;//
    public static final int TYPE_XYZ = 0x800000;    //
    public static final int TYPE_PENDULUM = 0x1000000;    //
    public static final int TYPE_SPSUMMON = 0x2000000;    //
    public static final int TYPE_LINK = 0x4000000;    //

    //Messages
    public static final int MSG_RETRY = 1;
    public static final int MSG_HINT = 2;
    public static final int MSG_WAITING = 3;
    public static final int MSG_START = 4;
    public static final int MSG_WIN = 5;
    public static final int MSG_UPDATE_DATA = 6;
    public static final int MSG_UPDATE_CARD = 7;
    public static final int MSG_REQUEST_DECK = 8;
    public static final int MSG_SELECT_BATTLECMD = 10;
    public static final int MSG_SELECT_IDLECMD = 11;
    public static final int MSG_SELECT_EFFECTYN = 12;
    public static final int MSG_SELECT_YESNO = 13;
    public static final int MSG_SELECT_OPTION = 14;
    public static final int MSG_SELECT_CARD = 15;
    public static final int MSG_SELECT_CHAIN = 16;
    public static final int MSG_SELECT_PLACE = 18;
    public static final int MSG_SELECT_POSITION = 19;
    public static final int MSG_SELECT_TRIBUTE = 20;
    //public static final int MSG_SORT_CHAIN			21;
    public static final int MSG_SELECT_COUNTER = 22;
    public static final int MSG_SELECT_SUM = 23;
    public static final int MSG_SELECT_DISFIELD = 24;
    public static final int MSG_SORT_CARD = 25;
    public static final int MSG_SELECT_UNSELECT_CARD = 26;
    public static final int MSG_CONFIRM_DECKTOP = 30;
    public static final int MSG_CONFIRM_CARDS = 31;
    public static final int MSG_SHUFFLE_DECK = 32;
    public static final int MSG_SHUFFLE_HAND = 33;
    public static final int MSG_REFRESH_DECK = 34;
    public static final int MSG_SWAP_GRAVE_DECK = 35;
    public static final int MSG_SHUFFLE_SET_CARD = 36;
    public static final int MSG_REVERSE_DECK = 37;
    public static final int MSG_DECK_TOP = 38;
    public static final int MSG_SHUFFLE_EXTRA = 39;
    public static final int MSG_NEW_TURN = 40;
    public static final int MSG_NEW_PHASE = 41;
    public static final int MSG_CONFIRM_EXTRATOP = 42;
    public static final int MSG_MOVE = 50;
    public static final int MSG_POS_CHANGE = 53;
    public static final int MSG_SET = 54;
    public static final int MSG_SWAP = 55;
    public static final int MSG_FIELD_DISABLED = 56;
    public static final int MSG_SUMMONING = 60;
    public static final int MSG_SUMMONED = 61;
    public static final int MSG_SPSUMMONING = 62;
    public static final int MSG_SPSUMMONED = 63;
    public static final int MSG_FLIPSUMMONING = 64;
    public static final int MSG_FLIPSUMMONED = 65;
    public static final int MSG_CHAINING = 70;
    public static final int MSG_CHAINED = 71;
    public static final int MSG_CHAIN_SOLVING = 72;
    public static final int MSG_CHAIN_SOLVED = 73;
    public static final int MSG_CHAIN_END = 74;
    public static final int MSG_CHAIN_NEGATED = 75;
    public static final int MSG_CHAIN_DISABLED = 76;
    public static final int MSG_CARD_SELECTED = 80;
    public static final int MSG_RANDOM_SELECTED = 81;
    public static final int MSG_BECOME_TARGET = 83;
    public static final int MSG_DRAW = 90;
    public static final int MSG_DAMAGE = 91;
    public static final int MSG_RECOVER = 92;
    public static final int MSG_EQUIP = 93;
    public static final int MSG_LPUPDATE = 94;
    public static final int MSG_UNEQUIP = 95;
    public static final int MSG_CARD_TARGET = 96;
    public static final int MSG_CANCEL_TARGET = 97;
    public static final int MSG_PAY_LPCOST = 100;
    public static final int MSG_ADD_COUNTER = 101;
    public static final int MSG_REMOVE_COUNTER = 102;
    public static final int MSG_ATTACK = 110;
    public static final int MSG_BATTLE = 111;
    public static final int MSG_ATTACK_DISABLED = 112;
    public static final int MSG_DAMAGE_STEP_START = 113;
    public static final int MSG_DAMAGE_STEP_END = 114;
    public static final int MSG_MISSED_EFFECT = 120;
    public static final int MSG_BE_CHAIN_TARGET = 121;
    public static final int MSG_CREATE_RELATION = 122;
    public static final int MSG_RELEASE_RELATION = 123;
    public static final int MSG_TOSS_COIN = 130;
    public static final int MSG_TOSS_DICE = 131;
    public static final int MSG_ROCK_PAPER_SCISSORS = 132;
    public static final int MSG_HAND_RES = 133;
    public static final int MSG_ANNOUNCE_RACE = 140;
    public static final int MSG_ANNOUNCE_ATTRIB = 141;
    public static final int MSG_ANNOUNCE_CARD = 142;
    public static final int MSG_ANNOUNCE_NUMBER = 143;
    public static final int MSG_CARD_HINT = 160;
    public static final int MSG_TAG_SWAP = 161;
    public static final int MSG_RELOAD_FIELD = 162;    // Debug.ReloadFieldEnd()
    public static final int MSG_AI_NAME = 163;
    public static final int MSG_SHOW_HINT = 164;
    public static final int MSG_PLAYER_HINT = 165;
    public static final int MSG_MATCH_KILL = 170;
    public static final int MSG_CUSTOM_MSG = 180;


    //Phase
    public static final int PHASE_DRAW = 0x01;
    public static final int PHASE_STANDBY = 0x02;
    public static final int PHASE_MAIN1 = 0x04;
    public static final int PHASE_BATTLE_START = 0x08;
    public static final int PHASE_BATTLE_STEP = 0x10;
    public static final int PHASE_DAMAGE = 0x20;
    public static final int PHASE_DAMAGE_CAL = 0x40;
    public static final int PHASE_BATTLE = 0x80;
    public static final int PHASE_MAIN2 = 0x100;
    public static final int PHASE_END = 0x200;

    public static final int COMMAND_ACTIVATE = 0x0001;
    public static final int COMMAND_SUMMON = 0x0002;
    public static final int COMMAND_SPSUMMON = 0x0004;
    public static final int COMMAND_MSET = 0x0008;
    public static final int COMMAND_SSET = 0x0010;
    public static final int COMMAND_REPOS = 0x0020;
    public static final int COMMAND_ATTACK = 0x0040;
    public static final int COMMAND_LIST = 0x0080;
    public static final int COMMAND_OPERATION = 0x0100;
    public static final int COMMAND_RESET = 0x0200;
    //
    public static final int EDESC_OPERATION = 1;
    public static final int EDESC_RESET = 2;
}
