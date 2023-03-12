package com.ocg.utils;

public class BitReader {
    public static int getUnsignedInt32(byte[] buff, int head) {
        if (head < 0) return -1;
        if (head > buff.length - 4) return -1;
        int ans = 0;
        for (int i = 0; i < 4; i++) {
            ans |= buff[head + i];
            ans = ans << 8;
        }
        return ans;
    }
    public static String getBit(byte[] buff, int head,int length){
        if(buff.length == 0) return null;
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<buff.length;i++){
            System.out.println(buff[head+i]);
            byte cur = buff[head+i];
            for(int j=7;j>-1;j--){
                if(length == 0) return sb.toString();
                sb.append((cur>>j) & 0x1);
                length--;
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        int a = -1;
        a&=0x7fffffff;
        System.out.println(a);
    }
}
