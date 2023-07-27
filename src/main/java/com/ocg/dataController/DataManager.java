package com.ocg.dataController;

import com.ocg.core.CallbackImpls.card_data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    public static Map<Integer, String> sysStrings = new HashMap<Integer, String>();
    public static Map<Integer, String> victoryStrings = new HashMap<Integer, String>();
    public static Map<Integer, String> counterStrings = new HashMap<Integer, String>();

    static String dataBasePath = "D:/MyCardLibrary/ygopro/cards.cdb";
    static String stringsPath = "D:/MyCardLibrary/ygopro/strings.conf";
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
    boolean loadString(String string_path){
        try (BufferedReader reader = new BufferedReader(new FileReader(string_path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 忽略注释和以 '!' 开头的行
                if (line.trim().startsWith("#")) {
                    continue;
                }
                // 解析有效的键值对
                String[] parts = line.trim().split("\\s+", 3);
                switch (parts[0]){
                    case "!system"->{
                        sysStrings.put(Integer.parseInt(parts[1]),parts[2]);
                    }
                    case "!victory"->{
                        victoryStrings.put(Integer.parseInt(parts[1].substring(2),16),parts[2]);
                    }
                    case "!counter"->{
                        counterStrings.put(Integer.parseInt(parts[1].substring(2),16),parts[2]);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public DataManager() {
        // read cards.cdb data to _datas
        if (!loadDB(dataBasePath)) {
            throw new RuntimeException("read data failed");
        }
        // read strings
        if (!loadString(stringsPath)) {
            throw new RuntimeException("read strings failed");
        }
    }

    /**
     * 为ygo-core提供的方法
     * @param code
     * @param pdata
     * @return
     */
    public static boolean getDataForCore(int code, card_data.ByReference pdata) {
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
    public static CardDataC getData(int code) {
        if(_datas.size() == 0) return null;
        if (!_datas.containsKey(code)) return null;
        return _datas.get(code);
    }

    /**
     * 获取描述
     * @param code
     * @return
     */
    public static CardString getCardDesc(int code) {
        if(_strings.size() == 0) return null;
        if (!_strings.containsKey(code)) return null;
        return _strings.get(code);
    }
    public static String getDesc(int key){
        if(key < 10000) return getSysString(key);
        int code = (key >> 4) & 0x0fffffff;
        int offset = key & 0xf;
        CardString cardString = _strings.get(code);
        if(cardString!=null) return cardString.desc[offset];
        return UNKNOW_STRING;
    }

    public static String getSysString(int key){
        return sysStrings.get(key);
    }
    public static String getVictoryString(int key){
        return victoryStrings.get(key);
    }
    public static String getCounterString(int key){
        return counterStrings.get(key);
    }
}