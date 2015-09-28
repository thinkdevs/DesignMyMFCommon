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

import java.util.ArrayList;
import java.util.List;

/**
 * Таблица с подкатегориями расходов
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class SubCategory extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name; //название подкатегории

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "category_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Category category; //категория

    List<Operation> operations; //список операций

    public long     getId() {
        return id;
    }

    public String   getName() {
        return name;
    }
    public void     setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }
    public void     setCategory(Category category) {
        this.category = category;
    }

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "operations")
    public List<Operation> getOperations(){
        if(operations == null){
            operations = new Select()
                    .from(Operation.class)
                    .where(Condition.column(Operation$Table.SUBCATEGORY_SUBCATEGORY_ID).is(this.id))
                    .queryList();
        }
        return operations;
    }

    public static List<SubCategory> getExpenseSubCategories() {
        List<SubCategory> subCategories = new ArrayList<>();
        List<Category> expenseCategories = Category.getExpenseCategories();
        for (Category category : expenseCategories) {
            subCategories.addAll(category.getSubCategories());
        }
        return subCategories;
    }
    public static List<SubCategory> getProfitSubCategories() {
        List<SubCategory> subCategories = new ArrayList<>();
        List<Category> profitCategories = Category.getProfitCategories();
        for (Category category : profitCategories) {
            subCategories.addAll(category.getSubCategories());
        }
        return subCategories;
    }

    public static SubCategory getExpenseSubCategoryByName(String title){
        for(SubCategory subCategory : getExpenseSubCategories()){
            if(subCategory.getName().equals(title))
                return subCategory;
        }
        return null;
    }
    public static SubCategory getProfitSubCategoryByName(String title){
        for(SubCategory subCategory : getProfitSubCategories()){
            if(subCategory.getName().equals(title))
                return subCategory;
        }
        return null;
    }

    public static SubCategory getSubCategoryByName(String title, Category category){
        return  new Select()
                .from(SubCategory.class)
                .where(Condition.CombinedCondition
                                .begin(Condition.column(SubCategory$Table.NAME).eq(title))
                                .and(Condition.column(SubCategory$Table.CATEGORY_CATEGORY_ID).is(category.getId()))
                )
                .querySingle();
    }

    public static boolean isExist(String name, Category category){
        return  new Select()
                .from(SubCategory.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(SubCategory$Table.NAME).eq(name))
                        .and  (Condition.column(SubCategory$Table.CATEGORY_CATEGORY_ID).is(category.getId())))
                .querySingle() != null;
    }

    public static SubCategory getSubCategoryById(long idSubCategoryToDelete) {
        return new Select()
                .from(SubCategory.class)
                .where(Condition.column(SubCategory$Table.ID).is(idSubCategoryToDelete))
                .querySingle();
    }
}
