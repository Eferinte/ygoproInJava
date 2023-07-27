package com.ocg.utils;

import java.util.Vector;

public class Convert {
    public static int[] vectorToArray(Vector<Integer> v){
        int[] a = new int[v.size()];
        for(int i=0;i<v.size();i++){
            a[i] = v.get(i);
        }
        return a;
    }
}
