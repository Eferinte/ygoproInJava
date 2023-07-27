package com.ocg.Moment.Network.proStructs;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CTOSJoinGame implements Structs {
    short version;
    int gameId;
    String pass;
    String name;

    @Override
    public byte[] getBytes() {
        byte[] buffs = new byte[48];
        buffs[0] = (byte)(version & 0xff);
        buffs[1] = (byte)(version >> 8 & 0xff);
        System.arraycopy(name.getBytes(StandardCharsets.UTF_16LE), 2, buffs, 2, 2);
        System.arraycopy(ByteBuffer.allocate(4).putInt(gameId).array(), 0, buffs, 4, 4);
        System.arraycopy(pass.getBytes(StandardCharsets.UTF_16LE), 0, buffs, 8, Math.min(40, pass.length()*2));
        buffs[46] = 0x0;
        buffs[47] = 0x0;
        return buffs;
    }

    public CTOSJoinGame(short version, int gameId, String pass, String name) {
        this.version = version;
        this.gameId = gameId;
        this.pass = pass;
        this.name = name;
    }
}
