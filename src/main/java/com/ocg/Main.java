package com.ocg;


import com.ocg.dataController.DataManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello ocg! JNA Test = " + OCGDll.INSTANCE.jna_test_multi(9, 9));
//        SingleDuel singleDuel = new SingleDuel(false);
//        singleDuel.StartDuel(new DuelPlayer());
//        singleDuel.TPResult(new DuelPlayer());
        DataManager dm = new DataManager();
        System.out.println(dm._datas.get(2511));
    }
}