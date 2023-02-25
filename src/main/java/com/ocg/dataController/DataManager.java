package com.ocg.dataController;

import com.ocg.CallbackImpls.card_data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    public static String UNKNOW_STRING = "???";
    public static Map<Integer, CardDataC> _datas = new HashMap<Integer, CardDataC>();
    public static Map<Integer, CardString> _strings;

    static String DataBasePath = "D:/MyCardLibrary/ygopro/cards.cdb";

    boolean loadDB(String data_base_path) {
        if (data_base_path.equals(null)) return false;
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:sqlite://"+data_base_path);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from datas;");
            while (rs.next()) {
                CardDataC card = new CardDataC(rs.getInt(1), rs.getInt(2), rs.getLong(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11));
                _datas.put(rs.getInt(1),card);
            }
            System.out.println("data load complete");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    public DataManager() {
        // read cards.cdb data to _datas
        if (!loadDB(DataBasePath)) {
            throw new RuntimeException("read data failed");
        }
    }


    public static boolean GetDataForCore(int code, card_data.ByReference pdata) {
        if (!_datas.containsKey(code)) return false;
        CardDataC target = _datas.get(code);
        pdata.code = target.code;
        pdata.alias = target.alias;
        pdata.setcode = target.setcode;
        pdata.type = target.type;
        pdata.level = target.level;
        pdata.attribute = target.attribute;
        pdata.race = target.race;
        pdata.attack = target.attack;
        pdata.defense = target.defense;
        pdata.lscale = target.lscale;
        pdata.rscale = target.rscale;
        pdata.link_marker = target.link_marker;
        return true;
    }
}