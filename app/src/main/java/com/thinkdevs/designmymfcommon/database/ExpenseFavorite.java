package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Таблица с шаблонами расходов
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class ExpenseFavorite extends BaseModel implements OperationFavorite {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name; //название шаблона

    @Column
    float amount; //сумма

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "subCategoryExpense_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    SubCategoryExpense subCategoryExpense; //подкатегория

    @Override
    public void setTitle(String title) {
        this.name = title;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public void setSubCategory(SubCategory subCategory) {
        this.subCategoryExpense = (SubCategoryExpense)subCategory;
    }

    @Override
    public SubCategory getSubCategory() {
        return subCategoryExpense;
    }
}
