package com.ocg.core;

import com.ocg.core.CallbackImpls.card_data;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

public interface OCGDll extends Library {


    String OCG_DLL_PATH = "./core/ocgcore";
    OCGDll INSTANCE = Native.load(OCG_DLL_PATH, OCGDll.class);
    int jna_test_multi(int a, int b);
    void jna_test_log(Pointer p);
    void logger(Pointer str);


    interface message_handler extends Callback {
        int invoke(long pduel, int signal);
    }

    interface script_reader extends Callback {
        ByteByReference invoke(String s, IntByReference i);
    }

    interface card_reader extends Callback {
        int invoke(int i, card_data.ByReference _i);
    }


    void set_script_reader(script_reader f);

    void set_card_reader(card_reader f);

    void set_message_handler(Callback cb);



    long create_duel(int seed);

    void new_card(long pduel, int code, int owner, int playerid, int location, int sequence, int position);

    void start_duel(long pduel, int options);

    int process(long pduel);

    int get_message(long pduel, byte[] buf);

    void set_player_info(long pduel, int playerid, int lp, int startcount, int drawcount);

    void get_log_message(long pduel, Pointer buf);
    int query_card(long pduel,int playerid,int location, int sequence, int query_flag, byte[]buf, int use_cache);
    int query_field_card(long pduel,int playerid,int location, int query_flag, byte[]buf, int use_cache);
    void set_responseb(long pduel,byte[]buf);
}