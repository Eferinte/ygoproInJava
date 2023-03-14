package com.ocg;

import com.ocg.Client.DuelClient;
import com.ocg.utils.MutateInt;

public class Net {
    public static void SendBufferToPlayer(DuelPlayer dp, byte proto, int offset, int length, byte[] buffer) {
        DuelClient.ClientAnalyze(buffer, new MutateInt(offset),length-1);
    }
}
