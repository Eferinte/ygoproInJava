package com.ocg.utils;

import com.sun.jna.Function;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Convertor {
    static public int byteToUInt8(byte uInt8){
        return 0xff & uInt8;
    }
    static public int byteToUInt16(short uInt16){
        return 0;
    }
    static public byte[] getRestBytes(ByteBuffer buffer){
        byte[] rest = new byte[buffer.remaining()];
        buffer.get(rest);
        return rest;
    }
    static public byte[] getRestBytes(ByteBuffer buffer,int len){
        byte[] rest = new byte[len];
        buffer.get(rest);
        return rest;
    }
    static public <T> ArrayList<String> getStringList(ArrayList<T> arrayList){
        ArrayList<String> stringList = new ArrayList<>();
        for(T t: arrayList){
            stringList.add(t.toString());
        }
        return stringList;
    }
    static public <T> ArrayList<String> getStringList(ArrayList<T> arrayList, FuncFilter<T> filter ){
        ArrayList<String> stringList = new ArrayList<>();
        for(T t: arrayList){
            if(filter.apply(t)) stringList.add(t.toString());
        }
        return stringList;
    }


}
