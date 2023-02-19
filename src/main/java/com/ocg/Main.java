package com.ocg;

import com.ocg.CallbackImpls.MessageHandleImpl;
import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello ocg! JNA Test = " + OCGDll.INSTANCE.jna_test_multi(9,9));


    }
}