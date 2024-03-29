package com.ocg.utils;


public class BitWriter {
    private byte[] buffer;
    private MutateInt ptr;

    public BitWriter(byte[] buffer, int offset) {
        this.buffer = buffer;
        ptr = new MutateInt(offset);
    }
    public BitWriter(int size, int offset) {
        this.buffer = new byte[size];
        ptr = new MutateInt(offset);
    }

    public void writeInt8(int value) {
        buffer[ptr.getValue()] = (byte)(value);
        ptr.step();
    }

    public void writeInt16(int value) {
        for (int i = 0; i < 2; i++) {
            byte Byte = (byte) (value & 0xff);
            value = value >> 8;
            buffer[ptr.getValue() + 1 - i] = Byte;
        }
        ptr.step(2);
    }

    public void writeInt32(int value){
        for (int i = 0; i < 4; i++) {
            byte Byte = (byte) (value & 0xff);
            value = value >> 8;
            buffer[ptr.getValue() + 3 - i] = Byte;
        }
        ptr.step(4);
    }

    public static void writeInt32(byte[] buffer, int value, int offset) throws BitException {
        if(buffer.length < offset+4){
            throw new BitException("out of index");
        }
        for (int i = 0; i < 4; i++) {
            byte Byte = (byte) (value & 0xff);
            value = value >> 8;
            buffer[offset + i] = Byte;
        }
    }
    public byte[] getBuffer(){
        return buffer;
    }
}
