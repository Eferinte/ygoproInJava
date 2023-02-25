package com.ocg;

import com.ocg.CallbackImpls.card_data;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

public interface OCGDll extends Library {


    String OCG_DLL_PATH = "D:\\vs2022_projs\\ocgcore\\x64\\Release\\ocgcore";
    OCGDll INSTANCE = Native.load(OCG_DLL_PATH, OCGDll.class);



    interface message_handler extends Callback {
        int invoke(Pointer pointer, int signal);
    }

    interface script_reader extends Callback {
        ByteByReference invoke(String s, IntByReference i);
    }

    interface card_reader extends Callback {
        int invoke(int i, card_data.ByReference _i);
    }


    void set_script_reader(script_reader f);

    void set_card_reader(card_reader f);

    void set_message_handler(message_handler f);

    int jna_test_multi(int a, int b);

    long create_duel(int seed);

    void new_card(long pduel, int code, byte owner, byte playerid, byte location, byte sequence, byte position);

    void start_duel(long pduel, int options);

    int process(long pduel);

    int get_message(long pduel, byte[] buf);

    void set_player_info(long pduel, int playerid, int lp, int startcount, int drawcount);

    void get_log_message(long pduel, byte[] buf);
}