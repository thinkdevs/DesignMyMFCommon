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

@Table(databaseName = MoneyFlowDataBase.NAME)
public class Operation extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    int type;

    @Column
    Date date;

    @Column
    long amount;

    @Column
    String description;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "cashAccount_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    CashAccount cashAccount;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CashAccount getCashAccount() {
        return cashAccount;
    }

    public void setCashAccount(CashAccount cashAccount) {
        this.cashAccount = cashAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static List<Operation> getOperations(){
        return new Select()
                .from(Operation.class)
                .queryList();
    }

    public static List<Operation> getExpenseOperations(){
        return new Select()
                .from(Operation.class)
                .where(Condition.column(Operation$Table.TYPE).is(Category.EXPENSE))
                .queryList();
    }

    public static List<Operation> getProfitOperations(){
        return new Select()
                .from(Operation.class)
                .where(Condition.column(Operation$Table.TYPE).is(Category.PROFIT))
                .queryList();
    }

    public boolean isExpense(){
        return Category.EXPENSE == this.getType();
    }

    public static void add(long templateID, long cashAccountID){

        //Шаблон операции
        OperationTemplate template = OperationTemplate.getByID(templateID);
        // Счет
        CashAccount cashAccount = CashAccount.getByID(cashAccountID);
        // Получаем стоимость
        long amount = template.getAmount();
        // Получаем комментарий
        String comment = template.getDescription();

        // Сохраняем операцию
        Operation operation = new Operation();
        operation.setDate(new Date(System.currentTimeMillis()));
        operation.setCashAccount(cashAccount);
        operation.setType(template.getType());
        operation.setCategory(template.getCategory());

        operation.setAmount(amount);
        operation.setDescription(comment);
        operation.save();

        amount = template.isExpense() ? amount * -1 : amount;

        cashAccount.setAmount(cashAccount.getAmount() + amount);
        cashAccount.update();
    }

    public static Operation getByID(long id) {
        return new Select().
                from(Operation.class).
                where(Condition.column(Operation$Table.ID).is(id)).
                querySingle();
    }

    public static void deleteByID(long id){
        getByID(id).delete();
    }
}
