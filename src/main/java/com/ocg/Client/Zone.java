package com.ocg.Client;

import com.ocg.Moment.Client.SelectOption;
import com.ocg.utils.ConstantDict.LOCATION;

import java.util.ArrayList;

public class Zone {
    public static ArrayList<SelectOption> getSelectablePlace(int flag) {
        ArrayList<SelectOption> opts = new ArrayList();
        int filter;
        filter = 0x1;
        for (int i = 0; i < 7; i++, filter <<= 1) {
            if ((flag & filter) == 0) opts.add(new SelectOption("怪兽区域-" + (i+1),i));
        }
        filter = 0x100;
        for (int i = 0; i < 8; i++, filter <<= 1) {
            if ((flag & filter) == 0) opts.add(new SelectOption("魔陷区域-" + (i+1),i+7));
        }
        filter = 0x10000;
        for (int i = 0; i < 7; i++, filter <<= 1) {
            if ((flag & filter) == 0) opts.add(new SelectOption("对手怪兽区域-" + (i+1),i+15));
        }
        filter = 0x1000000;
        for (int i = 0; i < 8; i++, filter <<= 1) {
            if ((flag & filter) == 0) opts.add(new SelectOption("对手怪兽区域-" + (i+1),i+22));
        }
        return opts;
    }

    public static LOCATION getType(int ans) {
        if (ans < 7)return LOCATION.MZONE;
        if (ans < 15) return LOCATION.SZONE;
        if (ans < 22)return LOCATION.MZONE;
        return LOCATION.SZONE;
    }

    public static int getSeq(int ans) {
        if (ans < 7)return ans;
        if (ans < 15) return ans-7;
        if (ans < 22)return ans -15;
        return ans -22;
    }
}
