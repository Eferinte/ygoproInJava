package com.ocg.utils.ConstantDict;


import java.util.EnumMap;

public class Dictionary {
    public static EnumMap<LOCATION,EnumStruct> locationMap = new EnumMap<>(LOCATION.class);
    public static EnumMap<PLAYER,EnumStruct> playerMap = new EnumMap<>(PLAYER.class);

    public static void load() {
        locationMap.put(LOCATION.DECK,new EnumStruct(0x01,"卡组"));
        locationMap.put(LOCATION.HAND,new EnumStruct(0x02,"手牌"));
        locationMap.put(LOCATION.MZONE,new EnumStruct(0x04,"怪兽区"));
        locationMap.put(LOCATION.SZONE,new EnumStruct(0x08,"魔陷区"));
        locationMap.put(LOCATION.GRAVE,new EnumStruct(0x10,"墓地"));
        locationMap.put(LOCATION.REMOVED,new EnumStruct(0x20,"除外区"));
        locationMap.put(LOCATION.EXTRA,new EnumStruct(0x40,"额外区"));
        locationMap.put(LOCATION.OVERLAY,new EnumStruct(0x80,"超量素材"));
        locationMap.put(LOCATION.ONFIELD,new EnumStruct(0x0c,"场上"));
        locationMap.put(LOCATION.FZONE,new EnumStruct(0x100,"场地"));
        locationMap.put(LOCATION.PZONE,new EnumStruct(0x200,"灵摆区"));

        playerMap.put(PLAYER.SELF,new EnumStruct(0,"己方"));
        playerMap.put(PLAYER.OPPOSITE,new EnumStruct(1,"对方"));
    }

    public static String getLocationDesc(int value){
        String desc;
        for(Object location: locationMap.keySet()){
            EnumStruct enumStruct = locationMap.get(location);
            if(enumStruct.value == value) return enumStruct.desc;
        }
        return "未知常量";
    }
    public static String getPlayerDesc(int value){
        String desc;
        for(Object player: playerMap.keySet()){
            EnumStruct enumStruct = playerMap.get(player);
            if(enumStruct.value == value) return enumStruct.desc;
        }
        return "未知常量";
    }
}
