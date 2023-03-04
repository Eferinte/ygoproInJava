package com.ocg.CallbackImpls;

import com.ocg.OCGDll;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class MessageHandleImpl implements OCGDll.message_handler {
    @Override
    public int invoke(long pduel, int signal) {
        System.out.println("[OCG-CORE]: 调用了 messageHandler");
        Pointer msgbuff = new Memory(20);
        System.out.println(pduel);
        OCGDll.INSTANCE.jna_test_log(msgbuff);
        OCGDll.INSTANCE.get_log_message(pduel, msgbuff);
        System.out.println("[OCG-CORE]:" + msgbuff.getString(0));

        Native.free(Pointer.nativeValue(msgbuff));     //手动释放内存
        Pointer.nativeValue(msgbuff, 0);      		//避免Memory对象被GC时重复执行Native.free()方法
        return 0;
    }
}
