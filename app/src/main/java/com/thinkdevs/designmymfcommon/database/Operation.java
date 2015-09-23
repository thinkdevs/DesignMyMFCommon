package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Date;
import java.util.List;

/**
 * Таблица с операциями
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class Operation extends BaseModel {

    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_PROFIT  = "profit";

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String type;   //тип операции;

    @Column
    Date   date;   //дата расхода

    @Column
    float  amount; //сумма

    @Column
    String comment; //комментарий к расходу

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "cashAccount_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    CashAccount cashAccount; // счет

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "subCategory_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    SubCategory subCategory; //подкатегория

    public long        getId() {
        return id;
    }

    public String      getType() {
        return type;
    }
    public void        setType(String type) {
        this.type = type;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }
    public void        setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public CashAccount getCashAccount() {
        return cashAccount;
    }
    public void        setCashAccount(CashAccount cashAccount) {
        this.cashAccount = cashAccount;
    }

    public String      getComment() {
        return comment;
    }
    public void        setComment(String comment) {
        this.comment = comment;
    }

    public float       getAmount() {
        return amount;
    }
    public void        setAmount(float amount) {
        this.amount = amount;
    }

    public Date        getDate() {
        return date;
    }
    public void        setDate(Date date) {
        this.date = date;
    }

    public static List<Operation> getAllOperations(){
        return new Select()
                .from(Operation.class)
                .queryList();
    }
    public static List<Operation> getExpenseOperations(){
        return new Select()
                .from(Operation.class)
                .where(Condition.column(Operation$Table.TYPE).is(TYPE_EXPENSE))
                .queryList();
    }
    public static List<Operation> getProfitOperations(){
        return new Select()
                .from(Operation.class)
                .where(Condition.column(Operation$Table.TYPE).is(TYPE_PROFIT))
                .queryList();
    }

    public boolean isExpense(){
        return Operation.TYPE_EXPENSE.equals(this.getType());
    }

    public static void newOperationFromTemplate(OperationTemplate operationTemplate, CashAccount cashAccount){

        // Получаем стоимость
        float amount = operationTemplate.getAmount();
        amount = operationTemplate.isExpense() ? amount * -1 : amount;

        // Получаем комментарий
        String comment = "";

        // Сохраняем операцию
        Operation operation = new Operation();
        operation.setDate(new Date(System.currentTimeMillis()));
        operation.setCashAccount(cashAccount);
        operation.setType(operationTemplate.getType());
        operation.setSubCategory(operationTemplate.getSubCategory());

        operation.setAmount(amount);
        operation.setComment(comment);
        operation.save();

        cashAccount.setAmount(cashAccount.getAmount() + amount);
        cashAccount.update();
    }

    public static Operation getOperationByTime(long time){
        return new Select().
                from(Operation.class).
                where(Condition.column(Operation$Table.DATE).is(time)).
                querySingle();
    }
}
