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

@Table(databaseName = MoneyFlowDataBase.NAME)
public class CashAccount extends BaseModel {

    public static final int CREATE = 0;
    public static final int EDIT   = 1;

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    String comment;

    @Column
    long  amount;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "currency_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Currency currency;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "icon_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Icon icon;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "color_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Color color;

    List<Operation> operations;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setDescription(String comment) {
        this.comment = comment;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "operations")
    public List<Operation> getAllOperations(){
        if(operations == null){
            operations = new Select()
                    .from(Operation.class)
                    .where(Condition.column(Operation$Table.CASHACCOUNT_CASHACCOUNT_ID).is(this.id))
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
                .where(Condition.CombinedCondition
                .begin(Condition.column(Operation$Table.TYPE).is(Category.EXPENSE))
                .and(Condition.column(Operation$Table.CASHACCOUNT_CASHACCOUNT_ID).is(this.getId())))
                .queryList();
    }

    public List<Operation> getProfitOperations(){
        return new Select()
                .from(Operation.class)
                .where(Condition.CombinedCondition
                .begin(Condition.column(Operation$Table.TYPE).is(Category.PROFIT))
                .and(Condition.column(Operation$Table.CASHACCOUNT_CASHACCOUNT_ID).is(this.getId())))
                .queryList();
    }

    public static boolean isExist(String title){
        return  new Select()
                .from(CashAccount.class)
                .where(Condition.column(CashAccount$Table.NAME).eq(title))
                .querySingle() != null;
    }

    public static List<CashAccount> getCashAccounts(){
        return new Select().from(CashAccount.class).queryList();
    }

    public static CashAccount getByName(String title){
        return new Select().from(CashAccount.class).
                where(Condition.column(CashAccount$Table.NAME)
                        .is(title))
                .querySingle();
    }

    public static CashAccount getByID(long id){
        return new Select().from(CashAccount.class).
                where(Condition.column(CashAccount$Table.ID)
                        .is(id))
                .querySingle();
    }

    public static void deleteByID(long id){
        getByID(id).delete();
    }

}
