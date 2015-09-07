package com.thinkdevs.designmymfcommon.mock;

import java.util.Date;

public interface CashAccount {

    String getName();

    int getLogo();

    boolean getType();

    float getAmount();

    int getLogoCurrency();

    Date getDateLastOperation();

    float getAmoutnLastOperation();
}
