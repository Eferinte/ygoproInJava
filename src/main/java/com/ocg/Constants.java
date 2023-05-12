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


    // CMD
    public static final int BUTTON_LAN_MODE = 100;
    public static final int BUTTON_SINGLE_MODE = 101;
    public static final int BUTTON_REPLAY_MODE = 102;
    public static final int BUTTON_TEST_MODE = 103;
    public static final int BUTTON_DECK_EDIT = 104;
    public static final int BUTTON_MODE_EXIT = 105;
    public static final int LISTBOX_LAN_HOST = 110;
    public static final int BUTTON_JOIN_HOST = 111;
    public static final int BUTTON_JOIN_CANCEL = 112;
    public static final int BUTTON_CREATE_HOST = 113;
    public static final int BUTTON_HOST_CONFIRM = 114;
    public static final int BUTTON_HOST_CANCEL = 115;
    public static final int BUTTON_LAN_REFRESH = 116;
    public static final int BUTTON_HP_DUELIST = 120;
    public static final int BUTTON_HP_OBSERVER = 121;
    public static final int BUTTON_HP_START = 122;
    public static final int BUTTON_HP_CANCEL = 123;
    public static final int BUTTON_HP_KICK = 124;
    public static final int CHECKBOX_HP_READY = 125;
    public static final int BUTTON_HP_READY = 126;
    public static final int BUTTON_HP_NOTREADY = 127;
    public static final int COMBOBOX_HP_CATEGORY = 128;
    public static final int LISTBOX_REPLAY_LIST = 130;
    public static final int BUTTON_LOAD_REPLAY = 131;
    public static final int BUTTON_CANCEL_REPLAY = 132;
    public static final int BUTTON_DELETE_REPLAY = 133;
    public static final int BUTTON_RENAME_REPLAY = 134;
    public static final int BUTTON_EXPORT_DECK = 135;
    public static final int BUTTON_REPLAY_START = 140;
    public static final int BUTTON_REPLAY_PAUSE = 141;
    public static final int BUTTON_REPLAY_STEP = 142;
    public static final int BUTTON_REPLAY_UNDO = 143;
    public static final int BUTTON_REPLAY_EXIT = 144;
    public static final int BUTTON_REPLAY_SWAP = 145;
    public static final int BUTTON_REPLAY_SAVE = 146;
    public static final int BUTTON_REPLAY_CANCEL = 147;
    public static final int LISTBOX_SINGLEPLAY_LIST = 150;
    public static final int BUTTON_LOAD_SINGLEPLAY = 151;
    public static final int BUTTON_CANCEL_SINGLEPLAY = 152;
    public static final int LISTBOX_BOT_LIST = 153;
    public static final int BUTTON_BOT_START = 154;
    public static final int COMBOBOX_BOT_RULE = 155;
    public static final int COMBOBOX_BOT_DECKCATEGORY = 156;
    public static final int EDITBOX_CHAT = 199;
    public static final int BUTTON_MSG_OK = 200;
    public static final int BUTTON_YES = 201;
    public static final int BUTTON_NO = 202;
    public static final int BUTTON_HAND1 = 205;
    public static final int BUTTON_HAND2 = 206;
    public static final int BUTTON_HAND3 = 207;
    public static final int BUTTON_FIRST = 208;
    public static final int BUTTON_SECOND = 209;
    public static final int BUTTON_POS_AU = 210;
    public static final int BUTTON_POS_AD = 211;
    public static final int BUTTON_POS_DU = 212;
    public static final int BUTTON_POS_DD = 213;
    public static final int BUTTON_OPTION_PREV = 220;
    public static final int BUTTON_OPTION_NEXT = 221;
    public static final int BUTTON_OPTION_OK = 222;
    public static final int BUTTON_OPTION_0 = 223;
    public static final int BUTTON_OPTION_1 = 224;
    public static final int BUTTON_OPTION_2 = 225;
    public static final int BUTTON_OPTION_3 = 226;
    public static final int BUTTON_OPTION_4 = 227;
    public static final int SCROLL_OPTION_SELECT = 228;
    public static final int BUTTON_CARD_0 = 230;
    public static final int BUTTON_CARD_1 = 231;
    public static final int BUTTON_CARD_2 = 232;
    public static final int BUTTON_CARD_3 = 233;
    public static final int BUTTON_CARD_4 = 234;
    public static final int SCROLL_CARD_SELECT = 235;
    public static final int BUTTON_CARD_SEL_OK = 236;
    public static final int TEXT_CARD_LIST_TIP = 237;
    public static final int BUTTON_CMD_ACTIVATE = 240;
    public static final int BUTTON_CMD_SUMMON = 241;
    public static final int BUTTON_CMD_SPSUMMON = 242;
    public static final int BUTTON_CMD_MSET = 243;
    public static final int BUTTON_CMD_SSET = 244;
    public static final int BUTTON_CMD_REPOS = 245;
    public static final int BUTTON_CMD_ATTACK = 246;
    public static final int BUTTON_CMD_SHOWLIST = 247;
    public static final int BUTTON_CMD_SHUFFLE = 248;
    public static final int BUTTON_CMD_RESET = 249;
    public static final int BUTTON_ANNUMBER_OK = 250;
    public static final int BUTTON_ANCARD_OK = 251;
    public static final int EDITBOX_ANCARD = 252;
    public static final int LISTBOX_ANCARD = 253;
    public static final int CHECK_ATTRIBUTE = 254;
    public static final int CHECK_RACE = 255;
    public static final int BUTTON_BP = 260;
    public static final int BUTTON_M2 = 261;
    public static final int BUTTON_EP = 262;
    public static final int BUTTON_LEAVE_GAME = 263;
    public static final int BUTTON_CHAIN_IGNORE = 264;
    public static final int BUTTON_CHAIN_ALWAYS = 265;
    public static final int BUTTON_CHAIN_WHENAVAIL = 266;
    public static final int BUTTON_CANCEL_OR_FINISH = 267;
    public static final int BUTTON_PHASE = 268;
    public static final int BUTTON_ANNUMBER_1 = 270;
    public static final int BUTTON_ANNUMBER_2 = 271;
    public static final int BUTTON_ANNUMBER_3 = 272;
    public static final int BUTTON_ANNUMBER_4 = 273;
    public static final int BUTTON_ANNUMBER_5 = 274;
    public static final int BUTTON_ANNUMBER_6 = 275;
    public static final int BUTTON_ANNUMBER_7 = 276;
    public static final int BUTTON_ANNUMBER_8 = 277;
    public static final int BUTTON_ANNUMBER_9 = 278;
    public static final int BUTTON_ANNUMBER_10 = 279;
    public static final int BUTTON_ANNUMBER_11 = 280;
    public static final int BUTTON_ANNUMBER_12 = 281;
    public static final int BUTTON_DISPLAY_0 = 290;
    public static final int BUTTON_DISPLAY_1 = 291;
    public static final int BUTTON_DISPLAY_2 = 292;
    public static final int BUTTON_DISPLAY_3 = 293;
    public static final int BUTTON_DISPLAY_4 = 294;
    public static final int SCROLL_CARD_DISPLAY = 295;
    public static final int BUTTON_CARD_DISP_OK = 296;
    public static final int BUTTON_SURRENDER_YES = 297;
    public static final int BUTTON_SURRENDER_NO = 298;
    public static final int BUTTON_MANAGE_DECK = 300;
    public static final int COMBOBOX_DBCATEGORY = 301;
    public static final int COMBOBOX_DBDECKS = 302;
    public static final int BUTTON_CLEAR_DECK = 303;
    public static final int BUTTON_SAVE_DECK = 304;
    public static final int BUTTON_SAVE_DECK_AS = 305;
    public static final int BUTTON_DELETE_DECK = 306;
    public static final int BUTTON_SIDE_RELOAD = 307;
    public static final int BUTTON_SORT_DECK = 308;
    public static final int BUTTON_SIDE_OK = 309;
    public static final int BUTTON_SHUFFLE_DECK = 310;
    public static final int COMBOBOX_MAINTYPE = 311;
    public static final int COMBOBOX_SECONDTYPE = 312;
    public static final int BUTTON_EFFECT_FILTER = 313;
    public static final int BUTTON_START_FILTER = 314;
    public static final int SCROLL_FILTER = 315;
    public static final int EDITBOX_KEYWORD = 316;
    public static final int BUTTON_CLEAR_FILTER = 317;
    public static final int COMBOBOX_ATTRIBUTE = 318;
    public static final int COMBOBOX_RACE = 319;
    public static final int COMBOBOX_LIMIT = 320;
    public static final int BUTTON_CATEGORY_OK = 321;
    public static final int BUTTON_MARKS_FILTER = 322;
    public static final int BUTTON_MARKERS_OK = 323;
    public static final int COMBOBOX_SORTTYPE = 324;
    public static final int EDITBOX_INPUTS = 325;
    public static final int WINDOW_DECK_MANAGE = 330;
    public static final int BUTTON_NEW_CATEGORY = 331;
    public static final int BUTTON_RENAME_CATEGORY = 332;
    public static final int BUTTON_DELETE_CATEGORY = 333;
    public static final int BUTTON_NEW_DECK = 334;
    public static final int BUTTON_RENAME_DECK = 335;
    public static final int BUTTON_DELETE_DECK_DM = 336;
    public static final int BUTTON_MOVE_DECK = 337;
    public static final int BUTTON_COPY_DECK = 338;
    public static final int LISTBOX_CATEGORIES = 339;
    public static final int LISTBOX_DECKS = 340;
    public static final int BUTTON_DM_OK = 341;
    public static final int BUTTON_DM_CANCEL = 342;
    public static final int COMBOBOX_LFLIST = 349;
    public static final int BUTTON_CLEAR_LOG = 350;
    public static final int LISTBOX_LOG = 351;
    public static final int SCROLL_CARDTEXT = 352;
    public static final int CHECKBOX_AUTO_SEARCH = 360;
    public static final int CHECKBOX_ENABLE_SOUND = 361;
    public static final int CHECKBOX_ENABLE_MUSIC = 362;
    public static final int SCROLL_VOLUME = 363;
    public static final int CHECKBOX_DISABLE_CHAT = 364;
    public static final int BUTTON_WINDOW_RESIZE_S = 365;
    public static final int BUTTON_WINDOW_RESIZE_M = 366;
    public static final int BUTTON_WINDOW_RESIZE_L = 367;
    public static final int BUTTON_WINDOW_RESIZE_XL = 368;
    public static final int CHECKBOX_QUICK_ANIMATION = 369;
    public static final int SCROLL_TAB_HELPER = 370;
    public static final int SCROLL_TAB_SYSTEM = 371;
    public static final int CHECKBOX_MULTI_KEYWORDS = 372;
    public static final int CHECKBOX_PREFER_EXPANSION = 373;
    public static final int CHECKBOX_DRAW_SINGLE_CHAIN = 374;
    public static final int CHECKBOX_LFLIST = 375;
    public static final int BUTTON_BIG_CARD_CLOSE = 380;
    public static final int BUTTON_BIG_CARD_ZOOM_IN = 381;
    public static final int BUTTON_BIG_CARD_ZOOM_OUT = 382;
    public static final int BUTTON_BIG_CARD_ORIG_SIZE = 383;
    // query list
    public static final int QUERY_CODE = 0x1;
    public static final int QUERY_POSITION = 0x2;
    public static final int QUERY_ALIAS = 0x4;
    public static final int QUERY_TYPE = 0x8;
    public static final int QUERY_LEVEL = 0x10;
    public static final int QUERY_RANK = 0x20;
    public static final int QUERY_ATTRIBUTE = 0x40;
    public static final int QUERY_RACE = 0x80;
    public static final int QUERY_ATTACK = 0x100;
    public static final int QUERY_DEFENSE = 0x200;
    public static final int QUERY_BASE_ATTACK = 0x400;
    public static final int QUERY_BASE_DEFENSE = 0x800;
    public static final int QUERY_REASON = 0x1000;
    public static final int QUERY_REASON_CARD = 0x2000;
    public static final int QUERY_EQUIP_CARD = 0x4000;
    public static final int QUERY_TARGET_CARD = 0x8000;
    public static final int QUERY_OVERLAY_CARD = 0x10000;
    public static final int QUERY_COUNTERS = 0x20000;
    public static final int QUERY_OWNER = 0x40000;
    public static final int QUERY_STATUS = 0x80000;
    public static final int QUERY_LSCALE = 0x200000;
    public static final int QUERY_RSCALE = 0x400000;
    public static final int QUERY_LINK = 0x800000;

}
