package com.thinkdevs.designmymfcommon.mock;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CashAccountMock implements CashAccount {

    private String name; //Название счета
    private int logo; //Логотип счета
    private boolean type; //Тип счета
    private float amount; //Средства на счету
    private int logoCurrency; //Логотип валюты
    private Operation lastOperation; //Последняя операция

    public CashAccountMock(String name, int logo, boolean type, float amount, int logoCurrency, Operation lastOperation) {
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

    public String getType() {

        return type ? "кошелек" : "карта";
    }

    public float getAmount() {
        return amount;
    }

    public int getLogoCurrency() {
        return logoCurrency;
    }

    //Форматируем отображение даты
    public String getDateLastOperation(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(lastOperation.getDate());
    }

    public float getAmountLastOperation(){
        return lastOperation.getAmount();
    }
}
