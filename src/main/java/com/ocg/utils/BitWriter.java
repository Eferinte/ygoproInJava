package com.ocg.utils;

public class BitWriter {
    private byte[] buffer;
    private MutateInt ptr;
    public BitWriter(byte[]buffer, int offset){
        this.buffer = buffer;
        ptr = new MutateInt(offset);
    }
    public void writeInt16(int value){
        byte lowerByte = (byte)(value & 0xff);
        buffer[ptr.getValue()] = lowerByte;
        value = value >> 8;
        byte higherByte = (byte)(value & 0xff);
        buffer[ptr.getValue()] = higherByte;
        ptr.step();
        buffer[ptr.getValue()] = lowerByte;
        ptr.step();
    }
}
