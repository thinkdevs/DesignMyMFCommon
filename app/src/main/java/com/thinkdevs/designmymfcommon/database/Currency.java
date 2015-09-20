package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Таблица с валютами
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class Currency extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String title; //название валюты

    @Column
    String strSymbol; //короткая запись

    public long   getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void   setTitle(String title) {
        this.title = title;
    }

    public String getStrSymbol() {
        return strSymbol;
    }
    public void   setStrSymbol(String strSymbol) {
        this.strSymbol = strSymbol;
    }

    public static Currency getCurrencyByStrSymbol(String strSymbol){
        return new Select()
                .from(Currency.class)
                .where(Condition.column(Currency$Table.STRSYMBOL).eq(strSymbol))
                .querySingle();
    }
}
