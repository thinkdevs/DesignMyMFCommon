package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Таблица со счетами
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class CashAccount extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String title;   //название счета

    @Column
    String comment; //комментарий к счету

    @Column
    float  amount;  //колличество средств

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "currency_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Currency currency; //валюта

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "logo_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Logo logo; //логотип

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "colour_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Color color; //цвет

    List<Operation> operations; //список операций

    public long     getId() {
        return id;
    }

    public String   getTitle() {
        return title;
    }
    public void     setTitle(String title) {
        this.title = title;
    }

    public String   getComment() {
        return comment;
    }
    public void     setComment(String comment) {
        this.comment = comment;
    }

    public float    getAmount() {
        return amount;
    }
    public void     setAmount(float amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }
    public void     setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Logo     getLogo() {
        return logo;
    }
    public void     setLogo(Logo logo) {
        this.logo = logo;
    }

    public Color    getColor() {
        return color;
    }
    public void     setColor(Color color) {
        this.color = color;
    }

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "operations")
    public List<Operation> getAllOperations(){
        if(operations == null){
            operations = new Select()
                    .from(Operation.class)
                    .where(Condition.column(Operation$Table.CASH_CASH_ID).is(this.id))
                    .queryList();
        }
        return operations;
    }

    public Operation getLastOperation (){
        if(getAllOperations() == null || getAllOperations().size() == 0)
            return null;
        else {
            return getAllOperations().get(getAllOperations().size() - 1);
        }
    }

    public List<Operation> getExpenseOperations(){
        return new Select()
                .from(Operation.class)
                .where(Condition.column(Operation$Table.TYPE).is(Operation.TYPE_EXPENSE)
                    && Condition.column(Operation$Table.CASHACCOUNT).is(this.getId()))
                .queryList();
    }
    public List<Operation> getProfitOperations(){
        return new Select()
                .from(Operation.class)
                .where(Condition.column(Operation$Table.TYPE).is(Operation.TYPE_PROFIT)
                    && Condition.column(Operation$Table.CASHACCOUNT).is(this.getId()))
                .queryList();
    }

    public static boolean isExist(String title){
        return  new Select()
                .from(CashAccount.class)
                .where(Condition.column(CashAccount$Table.NAME).eq(title))
                .querySingle() != null;
    }

    public static CashAccount getCashAccountByTitle(String title){
        return new Select().from(CashAccount.class).
                where(Condition.column(CashAccount$Table.NAME)
                        .is(title))
                .querySingle();
    }
}
