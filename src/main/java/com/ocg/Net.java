package com.ocg;

import com.ocg.Client.DuelClient;
import com.ocg.utils.MutateInt;

public class Net {
    // 暂时使用参数传递length代替添加长度头,
    public static void SendBufferToPlayer(DuelPlayer dp, byte proto, int offset, int length, byte[] buffer) {
        byte[] packet = new byte[length+1];
        packet[0]=proto;
        for(int i=0;i<length;i++){
            packet[i+1]=buffer[i];
        }
        DuelClient.HandleSTOCPacketLan(packet,length);
    }
    public static void SendBufferToPlayer(DuelPlayer dp, byte proto) {
        byte[] packet= new byte[1];
        packet[1]=proto;
        DuelClient.HandleSTOCPacketLan(packet,1);
    }
}
