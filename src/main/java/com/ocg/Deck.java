package com.ocg;

import com.ocg.dataController.CardDataC;
import com.ocg.dataController.DataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

class DataCardPair{
    int code;
    CardDataC data;
    public DataCardPair(int _code, CardDataC _data){
        code = _code;
        data = _data;
    }
}

public class Deck {

    private Vector<Integer> main_code;
    private Vector<Integer> extra_code;
    private Vector<Integer> side_code;
    public Vector<DataCardPair> main;
    public Vector<DataCardPair> extra;
    public Vector<DataCardPair> side;

    public Deck() {

    }

    void clear() {

    }
    public Vector<Integer> getMain_code() {
        return main_code;
    }

    public void setMain_code(Vector<Integer> main_code) {
        this.main_code = main_code;
    }

    public Vector<Integer> getExtra_code() {
        return extra_code;
    }

    public void setExtra_code(Vector<Integer> extra_code) {
        this.extra_code = extra_code;
    }

    public Vector<Integer> getSide_code() {
        return side_code;
    }

    public void setSide_code(Vector<Integer> side_code) {
        this.side_code = side_code;
    }

    public void LoadCardData() {
        main = new Vector<>();
        extra = new Vector<>();
        side = new Vector<>();
        for (int i = 0; i < main_code.size(); i++) {
            main.add(new DataCardPair(main_code.get(i),DataManager.getData(main_code.get(i))));
        }
        for (int i = 0; i < extra_code.size(); i++) {
            extra.add(new DataCardPair(extra_code.get(i),DataManager.getData(extra_code.get(i))));
        }
        for (int i = 0; i < side_code.size(); i++) {
            side.add(new DataCardPair(side_code.get(i),DataManager.getData(side_code.get(i))));
        }
    }

    @Override
    public String toString() {
        Map<Integer, Integer> main = new HashMap<Integer, Integer>();
        Map<Integer, Integer> extra = new HashMap<Integer, Integer>();
        Map<Integer, Integer> side = new HashMap<Integer, Integer>();
        String temp = "主卡组(" + main_code.size() + "张):\n";
        for (int it : main_code) {
            if (main.get(it) == null) {
                main.put(it, 1);
            } else {
                main.put(it, main.get(it) + 1);
            }
        }
        for (int key : main.keySet()) {
            temp += DataManager.getCardDesc(key).name + "×" + main.get(key) + "\n";
        }
        temp += "\n额外(" + extra_code.size() + "张):\n";
        for (int it : extra_code) {
            if (extra.get(it) == null) {
                extra.put(it, 1);
            } else {
                extra.put(it, extra.get(it) + 1);
            }
        }
        for (int key : extra.keySet()) {
            temp += DataManager.getCardDesc(key).name + "×" + extra.get(key) + "\n";
        }
        temp += "\n副卡组(" + side_code.size() + "张):\n";
        for (int it : side_code) {
            if (side.get(it) == null) {
                side.put(it, 1);
            } else {
                side.put(it, side.get(it) + 1);
            }
        }
        for (int key : side.keySet()) {
            temp += DataManager.getCardDesc(key).name + "×" + side.get(key) + "\n";
        }
        return temp;
    }
}
