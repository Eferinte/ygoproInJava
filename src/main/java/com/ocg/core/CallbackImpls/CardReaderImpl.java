package com.ocg.core.CallbackImpls;

import com.ocg.core.OCGDll;
import com.ocg.dataController.DataManager;

public class CardReaderImpl implements OCGDll.card_reader {
    @Override
    public int invoke(int code, card_data.ByReference pdata) {
        if (!DataManager.getDataForCore(code, pdata)) {
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
