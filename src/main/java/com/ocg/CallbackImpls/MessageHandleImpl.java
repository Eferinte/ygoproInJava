package com.ocg.CallbackImpls;

import com.ocg.OCGDll;
import com.sun.jna.Pointer;

public class MessageHandleImpl implements OCGDll.message_handler {
    @Override
    public int invoke(Pointer pointer, int signal) {
        byte[] msgbuff = new byte[1024];
        OCGDll.INSTANCE.get_log_message(pointer.getLong(0), msgbuff);
        System.out.println("[OCG-CORE]:" + msgbuff);
        return 0;
    }
}
