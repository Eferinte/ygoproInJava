package com.ocg.dataController;

import com.ocg.Deck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class DeckReader {

    static final String MAIN_IDENTIFIER = "#main";
    static final String EXTRA_IDENTIFIER = "#extra";
    static final String SIDE_IDENTIFIER = "!side";

    public static final Deck ReadYDK(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        Deck deck = new Deck();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            Vector<Integer> tempV = null;
            int line = 2;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 写入deck
                switch (tempString) {
                    case MAIN_IDENTIFIER -> {
                        tempV = new Vector<>();
                    }
                    case EXTRA_IDENTIFIER -> {
                        deck.setMain_code(tempV);
                        tempV = new Vector<>();
                    }
                    case SIDE_IDENTIFIER -> {
                        deck.setExtra_code(tempV);
                        tempV = new Vector<>();
                    }
                    default -> {
                        if(tempV != null){
                            tempV.add(Integer.parseInt(tempString));
                        }
                    }
                }
                line++;
            }
            deck.setSide_code(tempV);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            return deck;
        }
    }
}
