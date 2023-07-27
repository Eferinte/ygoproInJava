package com.ocg.Moment.Network.proStructs;

import com.ocg.utils.Convert;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

public class CTOSDeck implements Structs {
    int[] main;
    int[] extra;
    int[] side;

    public CTOSDeck(int[] main, int[] extra, int[] side) {
        this.main = main;
        this.extra = extra;
        this.side = side;
    }

    public CTOSDeck(Vector<Integer> main, Vector<Integer> extra, Vector<Integer> side) {
        this.main = Convert.vectorToArray(main);
        this.extra = Convert.vectorToArray(extra);
        this.side = Convert.vectorToArray(side);
    }

    /**
     * 卡组总数、主卡组数、额外卡组数、主卡组code、额外卡组code、副卡组code
     *
     * @return
     */
    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate((2 + main.length + extra.length + side.length) * 4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(main.length+extra.length);
        buffer.putInt(side.length);
        for (int i = 0; i < main.length; i++) {
            buffer.putInt(main[i]);
        }
        for (int i = 0; i < extra.length; i++) {
            buffer.putInt(extra[i]);
        }
        for (int i = 0; i < side.length; i++) {
            buffer.putInt(side[i]);
        }
        return buffer.array();
    }
}
