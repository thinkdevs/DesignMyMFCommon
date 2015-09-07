package com.thinkdevs.designmymfcommon.mock;

import java.util.Date;

public class OperationMock implements Operation {

    private Date date; //Дата последней операции
    private float amount; //Сумма последней операции

    public OperationMock(Date date, float amount) {
        this.date = date;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }
}
