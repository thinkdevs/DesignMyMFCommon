package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Таблица с шаблонами доходов
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class ProfitFavorite extends BaseModel implements OperationFavorite {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name; //название счета

    @Column
    float amount; //сумма

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "subCategoryProfit_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    SubCategoryProfit subCategoryProfit; //подкатегория

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
        this.subCategoryProfit = (SubCategoryProfit)subCategory;
    }

    @Override
    public SubCategory getSubCategory() {
        return subCategoryProfit;
    }
}
