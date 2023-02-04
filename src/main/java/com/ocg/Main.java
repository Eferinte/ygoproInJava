package com.ocg;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class Main {
    public interface CLibrary extends Library {

        String OCG_DLL_PATH = "D:\\vs2022_projs\\ocgcore\\x64\\Release\\ocgcore";

        CLibrary INSTANCE = Native.load(OCG_DLL_PATH, CLibrary.class);

        long create_duel(int seed);
        int jna_test_multi(int a, int b);

        void start_duel(long pduel, int options);
        void set_player_info(long pduel, int playerid, int lp, int startcount, int drawcount);
    }
    public static void main(String[] args) {
        long pduel = CLibrary.INSTANCE.create_duel(39);
        CLibrary.INSTANCE.start_duel(pduel, 0x01);
        System.out.println("Hello ocg!" );

//        CLibrary.INSTANCE.create_duel(39);


    }
}