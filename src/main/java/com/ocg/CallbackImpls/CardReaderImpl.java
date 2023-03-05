package com.ocg.CallbackImpls;

import com.ocg.OCGDll;
import com.ocg.dataController.CardData;
import com.ocg.dataController.CardDataC;
import com.ocg.dataController.DataManager;
import com.sun.jna.Pointer;

import static com.ocg.dataController.DataManager.GetDesc;

public class CardReaderImpl implements OCGDll.card_reader {
    @Override
    public int invoke(int code, card_data.ByReference pdata) {
        System.out.println("read"+code);
        System.out.println(GetDesc(code).name);
        if (!DataManager.GetDataForCore(code, pdata)) {
            pdata.code = 0;
            pdata.alias = 0;
            pdata.setcode = 0;
            pdata.type = 0;
            pdata.level = 0;
            pdata.attribute = 0;
            pdata.race = 0;
            pdata.attack = 0;
            pdata.defense = 0;
            pdata.lscale = 0;
            pdata.rscale = 0;
            pdata.link_marker = 0;
        }
        return 0;
    }
}
