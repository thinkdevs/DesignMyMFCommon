package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(databaseName = MoneyFlowDataBase.NAME)
public class Currency extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    String strSymbol;

    public long   getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrSymbol() {
        return strSymbol;
    }

    public void   setStrSymbol(String strSymbol) {
        this.strSymbol = strSymbol;
    }

    public static Currency getByStrSymbol(String strSymbol){
        return new Select()
                .from(Currency.class)
                .where(Condition.column(Currency$Table.STRSYMBOL).eq(strSymbol))
                .querySingle();
    }

    public static Currency getById(long id){
        return new Select()
                .from(Currency.class)
                .where(Condition.column(Currency$Table.ID).eq(id))
                .querySingle();
    }
}
