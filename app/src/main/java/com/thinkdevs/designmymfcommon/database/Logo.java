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

    public static final String TYPE_CASH_ACCOUNT = "cash_account";
    public static final String TYPE_CATEGORY     = "parentCategory";

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String type; //тип логотипа

    @Column
    int resourceId; //id логотипа

    public long   getId() {
        return id;
    }

    public String getType() {
        return type;
    }
    public void   setType(String type) {
        this.type = type;
    }

    public int    getResourceId() {
        return resourceId;
    }
    public void   setResourceId(int resourceId) {
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
