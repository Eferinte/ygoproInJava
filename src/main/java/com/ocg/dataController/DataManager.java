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
    public static Map<Integer, CardString> _strings = new HashMap<Integer, CardString>();

    static String DataBasePath = "D:/MyCardLibrary/ygopro/cards.cdb";

    boolean loadDB(String data_base_path) {
        if (data_base_path.equals(null)) return false;
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:sqlite://"+data_base_path);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from datas;");
            while (rs.next()) {
                CardDataC card = new CardDataC(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getLong(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11));
                _datas.put(rs.getInt(1),card);
            }
            System.out.println("data load complete");

            rs = stmt.executeQuery("select * from texts");
            while (rs.next()) {
                String[] desc = new String[16];
                for(int i=0;i<16;i++){
                    desc[i] = rs.getString(i+4);
                }
                CardString str = new CardString(rs.getString(2),rs.getString(3),desc);
                _strings.put(rs.getInt(1),str);
            }
            System.out.println("text load complete");
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

    /**
     * 为ygo-core提供的方法
     * @param code
     * @param pdata
     * @return
     */
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

    /**
     * 获取具体数据
     * @param code
     * @return
     */
    public static CardDataC GetData(int code) {
        if(_datas.size() == 0) return null;
        if (!_datas.containsKey(code)) return null;
        return _datas.get(code);
    }

    /**
     * 获取描述
     * @param code
     * @return
     */
    public static CardString GetDesc(int code) {
        if(_strings.size() == 0) return null;
        if (!_strings.containsKey(code)) return null;
        return _strings.get(code);
    }
}