package com.ocg.utils;

public class MutateInt {
    private int origin;
    private int value;

    public MutateInt(){
        this.origin = 0;
        this.value = 0;
    }
    public MutateInt(int num){
        this.origin = num;
        this.value = num;
    }
    public void step(){
        value++;
    }
    public void step(int num){
        value+=num;
    }
    public void reset(){
        value = origin;
    }
    public void reset(int num){
        value = num;
        origin = num;
    }

    public int getValue() {
        return value;
    }
}
