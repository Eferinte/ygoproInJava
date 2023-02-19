package com.ocg.dataController;

import com.ocg.CallbackImpls.card_data;

import java.util.Map;

public class DataManager {
    public static String UNKNOW_STRING = "???";
    static Map<Integer, CardDataC>  _datas;
    static Map<Integer, CardString> _strings;

    public static boolean  GetData(int code, card_data.ByReference pdata){
        if(!_datas.containsKey(code)) return false;
        pdata.code = _datas.get(code).code;
        pdata.alias = _datas.get(code).alias;
        pdata.setcode = _datas.get(code).setcode;
        pdata.type = _datas.get(code).type;
        pdata.level = _datas.get(code).level;
        pdata.attribute = _datas.get(code).attribute;
        pdata.race = _datas.get(code).race;
        pdata.attack = _datas.get(code).attack;
        pdata.defense = _datas.get(code).defense;
        pdata.lscale = _datas.get(code).lscale;
        pdata.rscale = _datas.get(code).rscale;
        pdata.link_marker = _datas.get(code).link_marker;
        return true;
    }
}