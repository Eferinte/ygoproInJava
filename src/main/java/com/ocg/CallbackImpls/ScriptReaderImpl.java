package com.ocg.CallbackImpls;

import com.ocg.OCGDll;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

public class ScriptReaderImpl implements OCGDll.script_reader {
    @Override
    public ByteByReference invoke(String s, IntByReference i) {
//        Pointer msgbuff = new Memory(1024);
//        OCGDll.INSTANCE.get_log_message(pduel, msgbuff);
//        System.out.println("[OCG-CORE]:" + msgbuff.getString(0));
//        Native.free(Pointer.nativeValue(msgbuff));     //手动释放内存
//        Pointer.nativeValue(msgbuff, 0);            //避免Memory对象被GC时重复执行Native.free()方法
        return new ByteByReference();
    }
}
