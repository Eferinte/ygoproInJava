package com.ocg.core.CallbackImpls;

import com.sun.jna.Structure;
@Structure.FieldOrder({"code","alias","setcode","type","level","attribute","race","attack","defense","lscale","rscale","link_marker"})
public class card_data extends Structure {
    public int code;
    public int alias;
    public long setcode;
    public int type;
    public int level;
    public int attribute;
    public int race;
    public int attack;
    public int defense;
    public int lscale;
    public int rscale;
    public int link_marker;

    public void clear(){

    };

    //指针实现
    public static class ByReference extends card_data implements Structure.ByReference {

    }
}
