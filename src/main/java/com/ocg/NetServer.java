package com.ocg;

import com.ocg.Client.DuelClient;
import com.ocg.utils.BitReader;
import com.ocg.utils.MutateInt;

import static com.ocg.Constants.*;

public class Net {
    static byte[]  net_server_read = new byte[0x2000];
    static  byte[]  net_server_write = new byte[0x2000];
    // 暂时使用参数传递length代替添加长度头,
    public static void SendBufferToPlayer(DuelPlayer dp, byte proto, int offset, int length, byte[] buffer) {
        byte[] packet = net_server_write;
        packet[0]=proto;
        for(int i=0;i<length;i++){
            packet[i+1]=buffer[i];
        }
        DuelClient.HandleSTOCPacketLan(packet,length);
    }
    public static void SendBufferToPlayer(DuelPlayer dp, byte proto) {
        byte[] packet= net_server_write;
        packet[1]=proto;
        DuelClient.HandleSTOCPacketLan(packet,1);
    }
    public static void HandleCTOSPacket(DuelPlayer dp,byte[] data, int len){
        byte[] pdata = data;
        BitReader br = new BitReader(pdata,0);
        int pktType = br.readInt8();
        //TODO 条件确认
        if((pktType!= CTOS_SURRENDER)&&(pktType!=CTOS_CHAT)&&(dp.state == 0xff || ((dp.state!=0) && (dp.state != pktType)))){
            return;
        }
        switch (pktType){
            case CTOS_RESPONSE -> {
                if(dp.game)
            }

        }
    }
}
