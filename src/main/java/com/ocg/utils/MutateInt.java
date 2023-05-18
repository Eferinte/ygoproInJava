package com.ocg.utils;

/**
 * 模拟c++的指针滑动
 */
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
    public void set(int num){
        value = num;
    }

    public int getValue() {
        return value;
    }
    public int getOrigin() {
        return origin;
    }

}
