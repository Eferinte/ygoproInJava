package com.ocg.Moment.Network;

import com.ocg.Moment.Client.LogicClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class NetworkBase {

    LogicClient client;
    ByteBuffer duelClientRead = ByteBuffer.allocate(0x2000).order(ByteOrder.LITTLE_ENDIAN);
    public byte [] lastSuccessfulMsg = new byte[0x2000];
    public int lastSuccessfulMsgLen;

    /**
     * 通信接口
     */
    public abstract boolean CTOS(byte[] msg) throws IOException;

    public NetworkBase(LogicClient client) {
        this.client = client;
    }

    public void onReceived(byte[] buffer, int len) throws IOException {
        duelClientRead.put(buffer, 0, len);
        int leftLen = duelClientRead.position();
        duelClientRead.flip();
        short packetLen;
        while (true) {
            if (leftLen < 2) break;
            // 不弹出读取
            duelClientRead.mark();
            packetLen = duelClientRead.getShort();
            duelClientRead.reset();
            if (leftLen < packetLen + 2) break;
            if (packetLen != 0) {
                duelClientRead.getShort();
                byte[] packet = new byte[packetLen];
                duelClientRead.get(packet);
                client.handleSTOC(packet, packetLen);
            }
            leftLen -= packetLen + 2;
        }
        duelClientRead.compact();
    }
}
