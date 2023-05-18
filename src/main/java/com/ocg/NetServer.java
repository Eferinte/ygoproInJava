package com.ocg;

import com.ocg.Client.DuelClient;
import com.ocg.utils.BitReader;

import static com.ocg.Constants.*;
import static com.ocg.Main.duel_mode;

public class NetServer {
    int server_port = 0;
    static byte[] net_server_read = new byte[0x2000];
    static byte[] net_server_write = new byte[0x2000];
    static int last_sent =0;

    // 暂时使用参数传递length代替添加长度头,

    /**
     * 原工程中将消息长度写入buffer供libevent库解析消息长度，此处无必要
     * @param dp
     * @param proto
     * @param offset
     * @param length
     * @param buffer
     */
    public static void SendBufferToPlayer(DuelPlayer dp, byte proto, int offset, int length, byte[] buffer) {
        byte[] packet = net_server_write;
        packet[0] = proto;
        if (length > 0) System.arraycopy(buffer, 0, packet, 1, length);
        DuelClient.HandleSTOCPacketLan(packet, length);
    }
    //TODO 先不泛化
    public static<T> void SendPacketToPlayer(DuelPlayer dp, byte proto, byte st) {
        byte[] packet = net_server_write;
        packet[0] = proto;
        packet[1] = st;
        last_sent = 2;//记录长度?
        if(dp!=null){
            //TODO 发给Client
        }
    }

    public static void SendBufferToPlayer(DuelPlayer dp, byte proto, int offset, int i) {
        byte[] packet = net_server_write;
        packet[1] = proto;
        DuelClient.HandleSTOCPacketLan(packet, 1);
    }

    public static void HandleCTOSPacket(DuelPlayer dp, byte[] data, int len) {
        byte[] pdata = data;
        BitReader br = new BitReader(pdata, 0);
        int pktType = br.readInt8();
        //TODO 条件确认
//        if ((pktType != CTOS_SURRENDER) && (pktType != CTOS_CHAT) && (dp.state == 0xff || ((dp.state != 0) && (dp.state != pktType)))) {
//            return;
//        }
        switch (pktType) {
            case CTOS_RESPONSE -> {
                //TODO 校验pduel
                duel_mode.GetResponse(dp, pdata, len > 64 ? 64 : len - 1);
                break;
            }
        }
    }
}
