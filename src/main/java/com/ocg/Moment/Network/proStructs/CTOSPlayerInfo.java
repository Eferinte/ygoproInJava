package com.ocg.Moment.Network.proStructs;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CTOSPlayerInfo implements Structs{
    public String name;

    public CTOSPlayerInfo(String name) {
        this.name = name;
    }

    @Override
    public byte[] getBytes() {
        byte[] rlt = new byte[40];
        byte[] nameBuffer = this.name.getBytes(StandardCharsets.UTF_16LE);
        for(int i=0;i<Math.min(38,nameBuffer.length);i++){
            rlt[i] = nameBuffer[i];
        }
        // 中断符 00 00
        rlt[38] = 0x0;
        rlt[39] = 0x0;
        return rlt;
    }
}
