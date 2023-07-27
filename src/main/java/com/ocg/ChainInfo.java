package com.ocg;

import com.ocg.Client.ClientCard;

import java.util.HashSet;
import java.util.Set;

public class ChainInfo {
    public int code;
    public int desc;
    public int controller;
    public  int location;
    public  int sequence;
    public boolean solved;
    public ClientCard chain_card;
    public Set<ClientCard> target = new HashSet<>();
}
