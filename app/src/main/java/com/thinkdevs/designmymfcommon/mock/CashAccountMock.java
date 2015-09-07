package com.thinkdevs.designmymfcommon.mock;

import java.util.Date;

public class CashAccountMock implements CashAccount {

    private String name; //Название счета
    private int logo; //Логотип счета
    private boolean type; //Тип счета
    private float amount; //Средства на счету
    private int logoCurrency; //Логотип валюты
    private Operation lastOperation; //Последняя операция

    public CashAccountMock(String name, int logo, boolean type, float amount, int logoCurrency, OperationMock lastOperation) {
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.amount = amount;
        this.logoCurrency = logoCurrency;
        this.lastOperation = lastOperation;
    }

    public String getName() {
        return name;
    }

    public int getLogo() {
        return logo;
    }

    public boolean getType() {
        return type;
    }

    public float getAmount() {
        return amount;
    }

    public int getLogoCurrency() {
        return logoCurrency;
    }

    public Date getDateLastOperation(){
        return lastOperation.getDate();
    }

    public float getAmoutnLastOperation(){
        return lastOperation.getAmount();
    }
}
