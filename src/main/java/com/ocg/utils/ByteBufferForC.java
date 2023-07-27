package com.ocg.utils;

import java.nio.ByteBuffer;

public class ByteBufferForC {
    public ByteBuffer nativeBuffer;

    public ByteBufferForC(int capacity) {
        nativeBuffer = ByteBuffer.allocate(capacity);
    }
    public int getInt8(){
        return 0;
    }
    public int getUInt8(){
        return 0;
    }
    public int getInt16(){
        return 0;
    }
}
