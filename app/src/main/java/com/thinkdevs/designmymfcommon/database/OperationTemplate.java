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

    public final static String TYPE_EXPENSE = "expense";
    public final static String TYPE_PROFIT  = "profit";

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name; //название счета

    @Column
    String type; //тип операции

    @Column
    float amount; //сумма

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "subCategory_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    SubCategory subCategory; //подкатегория

    public String      getName() {
        return name;
    }
    public void        setName(String title) {
        this.name = title;
    }

    public String      getType() {
        return type;
    }
    public void        setType(String type) {
        this.type = type;
    }

    public float       getAmount() {
        return amount;
    }
    public void        setAmount(float amount) {
        this.amount = amount;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }
    public void        setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
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
                .where(Condition.column(OperationTemplate$Table.TYPE).eq(OperationTemplate.TYPE_EXPENSE))
                .queryList();
    }

    public static List<OperationTemplate> getProfitOperationTemplates(){
        return new Select()
                .from(OperationTemplate.class)
                .where(Condition.column(OperationTemplate$Table.TYPE).eq(OperationTemplate.TYPE_PROFIT))
                .queryList();
    }

    public static OperationTemplate getOperationTemplateByName(String name, String type){
        return  new Select()
                .from(OperationTemplate.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(OperationTemplate$Table.NAME).is(name))
                        .and  (Condition.column(OperationTemplate$Table.TYPE).is(type)))
                .querySingle();
    }

    public boolean isExpense(){
        return OperationTemplate.TYPE_EXPENSE.equals(this.getType());
    }

}
