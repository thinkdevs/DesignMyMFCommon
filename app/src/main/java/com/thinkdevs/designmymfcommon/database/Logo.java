package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Таблица с логотипами
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class Logo extends BaseModel {

    public static final int TYPE_CASH_ACCOUNT = 1;
    public static final int TYPE_CATEGORY     = 2;

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    int type; //тип логотипа

    @Column
    int resourceId; //id логотипа

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public static List<Logo> getAllCashAccountLogos(){
        return new Select()
                .from(Logo.class)
                .where(Condition.column(Logo$Table.TYPE).is(TYPE_CASH_ACCOUNT))
                .queryList();
    }

    public static List<Logo> getAllCategoryLogos(){
        return new Select()
                .from(Logo.class)
                .where(Condition.column(Logo$Table.TYPE).is(TYPE_CATEGORY))
                .queryList();
    }

    public static Logo getLogoByResourceId(long resourceId){
        return  new Select()
                .from(Logo.class)
                .where(Condition.column(Logo$Table.RESOURCEID)
                        .eq(resourceId)).querySingle();
    }
}
