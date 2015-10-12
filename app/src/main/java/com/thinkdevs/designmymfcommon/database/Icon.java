package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(databaseName = MoneyFlowDataBase.NAME)
public class Icon extends BaseModel {

    public static final int TYPE_CASH_ACCOUNT = 1;
    public static final int TYPE_CATEGORY     = 2;

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    int type;

    @Column
    int resourceId;

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

    public static List<Icon> getCashAccountIcons(){
        return new Select()
                .from(Icon.class)
                .where(Condition.column(Icon$Table.TYPE).is(TYPE_CASH_ACCOUNT))
                .queryList();
    }

    public static List<Icon> getCategoryIcons(){
        return new Select()
                .from(Icon.class)
                .where(Condition.column(Icon$Table.TYPE).is(TYPE_CATEGORY))
                .queryList();
    }

    public static Icon getByResourceId(long resourceId){
        return  new Select()
                .from(Icon.class)
                .where(Condition.column(Icon$Table.RESOURCEID)
                        .eq(resourceId)).querySingle();
    }

    public static Icon getById(long id){
        return  new Select()
                .from(Icon.class)
                .where(Condition.column(Icon$Table.ID)
                        .eq(id)).querySingle();
    }
}
