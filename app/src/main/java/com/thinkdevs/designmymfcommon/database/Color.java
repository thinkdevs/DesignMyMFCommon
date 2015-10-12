package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(databaseName = MoneyFlowDataBase.NAME)
public class Color extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    int resourceId; //id цвета

    public long getId() {
        return id;
    }

    public int  getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public static List<Color> getColors(){
        return new Select()
                .from(Color.class)
                .queryList();
    }

    public static List<Color> getColorsWithoutSystems(){
       List<Color> colors = getColors();
        colors.remove(18);
        colors.remove(17);
        colors.remove(12);
        colors.remove(8);
        return colors;
    }

    public static Color getByResourceId(long resourceId){
        return  new Select()
                .from(Color.class)
                .where(Condition.column(Color$Table.RESOURCEID)
                        .eq(resourceId)).querySingle();
    }

    public static Color getById(long id){
        return  new Select()
                .from(Color.class)
                .where(Condition.column(Color$Table.ID)
                        .eq(id)).querySingle();
    }
}
