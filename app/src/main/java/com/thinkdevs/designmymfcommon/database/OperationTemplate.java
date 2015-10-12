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
    long amount;

    @Column
    String description;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "category_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Category category;

    public long getId() {
        return id;
    }

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

    public long getAmount() {
        return amount;
    }

    public void  setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category subCategory) {
        this.category = subCategory;
    }

    public static boolean isExist(String name, int type){
        return  new Select()
                .from(OperationTemplate.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(OperationTemplate$Table.NAME).eq(name))
                        .and  (Condition.column(OperationTemplate$Table.TYPE).is(type)))
                .querySingle() != null;
    }

    public static List<OperationTemplate> getTemplates(){
        return new Select()
                .from(OperationTemplate.class)
                .queryList();
    }

    public static List<OperationTemplate> getExpenseTemplates(){
        return new Select()
                .from(OperationTemplate.class)
                .where(Condition.column(OperationTemplate$Table.TYPE).is(Category.EXPENSE))
                .queryList();
    }

    public static List<OperationTemplate> getProfitTemplates(){
        return new Select()
                .from(OperationTemplate.class)
                .where(Condition.column(OperationTemplate$Table.TYPE).is(Category.PROFIT))
                .queryList();
    }

    public static OperationTemplate getByName(String name, String type){
        return  new Select()
                .from(OperationTemplate.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(OperationTemplate$Table.NAME).is(name))
                        .and(Condition.column(OperationTemplate$Table.TYPE).is(type)))
                .querySingle();
    }

    public static OperationTemplate getByID(long ID){
        return new Select()
                .from(OperationTemplate.class)
                .where(Condition.column(OperationTemplate$Table.ID).is(ID))
                .querySingle();
    }

    public boolean isExpense(){
        return Category.EXPENSE == this.getType();
    }

    public static void add(Operation operation){

    }

    public static void deleteByID(long ID){
        getByID(ID).delete();
    }

}
