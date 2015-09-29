package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Таблица с шаблонами доходов
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class OperationTemplate extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    int type;

    @Column
    float amount;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "category_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Category category;

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void  setAmount(float amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category subCategory) {
        this.category = subCategory;
    }

    public static boolean isExist(String name, String type){
        return  new Select()
                .from(OperationTemplate.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(OperationTemplate$Table.NAME).is(name))
                        .and  (Condition.column(OperationTemplate$Table.TYPE).is(type)))
                .querySingle() != null;
    }

    public static List<OperationTemplate> getOperationTemplates(){
        return new Select()
                .from(OperationTemplate.class)
                .queryList();
    }

    public static List<OperationTemplate> getExpenseOperationTemplates(){
        return new Select()
                .from(OperationTemplate.class)
                .where(Condition.column(OperationTemplate$Table.TYPE).is(Category.TYPE_EXPENSE))
                .queryList();
    }

    public static List<OperationTemplate> getProfitOperationTemplates(){
        return new Select()
                .from(OperationTemplate.class)
                .where(Condition.column(OperationTemplate$Table.TYPE).is(Category.TYPE_PROFIT))
                .queryList();
    }

    public static OperationTemplate getOperationTemplateByName(String name, String type){
        return  new Select()
                .from(OperationTemplate.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(OperationTemplate$Table.NAME).is(name))
                        .and(Condition.column(OperationTemplate$Table.TYPE).is(type)))
                .querySingle();
    }

    public boolean isExpense(){
        return Category.TYPE_EXPENSE == this.getType();
    }

    public static void newTemplateFromOperation(Operation operation){

    }

}
