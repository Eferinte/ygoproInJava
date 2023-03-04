package com.ocg;

import com.ocg.dataController.CardDataC;
import com.ocg.dataController.DataManager;
import com.ocg.dataController.DeckReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Deck {
    public Vector<Integer> main_code;
    public Vector<Integer> extra_code;
    public Vector<Integer> side_code;
    public Vector<CardDataC> main;
    public Vector<CardDataC> extra;
    public Vector<CardDataC> side;

    public Deck() {

    }

    void clear() {

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
            temp += DataManager.GetDesc(key).name + "×" + main.get(key) + "\n";
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
            temp += DataManager.GetDesc(key).name + "×" + extra.get(key) + "\n";
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
            temp += DataManager.GetDesc(key).name + "×" + side.get(key) + "\n";
        }
        return temp;
    }
}
