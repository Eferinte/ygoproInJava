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

    // Positions 卡片放置状态

    public static final byte POS_FACEUP_ATTACK = 0x1;
    public static final byte POS_FACEDOWN_ATTACK = 0x2;
    public static final byte POS_FACEUP_DEFENSE = 0x4;
    public static final byte POS_FACEDOWN_DEFENSE = 0x8;
    public static final byte POS_FACEUP = 0x5;
    public static final byte POS_FACEDOWN = 0xa;
    public static final byte POS_ATTACK = 0x3;
    public static final byte POS_DEFENSE = 0xc;

}
