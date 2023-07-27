package com.ocg.Moment.Client;

import com.ocg.Client.ClientCard;

import java.util.ArrayList;

public interface ClientInterface {
    /**
     * 用户交互
     */
    boolean confirm(String hint);

    int selectCard(ClientCard[] cards);

    int selectPos(byte position);
    SelectOption select(ArrayList<SelectOption> options);
    void log(String operate);
}
