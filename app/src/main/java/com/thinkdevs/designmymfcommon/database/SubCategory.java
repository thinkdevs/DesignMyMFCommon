package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Таблица с подкатегориями расходов
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class SubCategory extends Category {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "parentCategory_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    ParentCategory parentCategory;

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

    public ParentCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ParentCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "operations")
    public List<Operation> getOperations(){
        if(operations == null){
            operations = new Select()
                    .from(Operation.class)
                    .where(Condition.column(Operation$Table.CATEGORY_CATEGORY_ID).is(this.id))
                    .queryList();
        }
        return operations;
    }

    @Override
    public Logo getLogo() {
        return parentCategory.getLogo();
    }

    @Override
    public Color getColor() {
        return parentCategory.getColor();
    }

    @Override
    public int getHierarchy() {
        return HIERARCHY_SUB;
    }

    @Override
    public int getType() {
        return parentCategory.getType();
    }

    public static List<SubCategory> getExpenseSubCategories() {
        List<SubCategory> subCategories = new ArrayList<>();
        List<ParentCategory> expenseCategories = ParentCategory.getExpenseCategories();
        for (ParentCategory parentCategory : expenseCategories) {
            subCategories.addAll(parentCategory.getSubCategories());
        }
        return subCategories;
    }

    public static List<SubCategory> getProfitSubCategories() {
        List<SubCategory> subCategories = new ArrayList<>();
        List<ParentCategory> profitCategories = ParentCategory.getProfitCategories();
        for (ParentCategory parentCategory : profitCategories) {
            subCategories.addAll(parentCategory.getSubCategories());
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

    public static SubCategory getSubCategoryByName(String title, ParentCategory parentCategory){
        return  new Select()
                .from(SubCategory.class)
                .where(Condition.CombinedCondition
                                .begin(Condition.column(SubCategory$Table.NAME).eq(title))
                                .and(Condition.column(SubCategory$Table.PARENTCATEGORY_PARENTCATEGORY_ID).is(parentCategory.getId()))
                )
                .querySingle();
    }

    public static boolean isExist(String name, ParentCategory parentCategory){
        return  new Select()
                .from(SubCategory.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(SubCategory$Table.NAME).eq(name))
                        .and  (Condition.column(SubCategory$Table.PARENTCATEGORY_PARENTCATEGORY_ID).is(parentCategory.getId())))
                .querySingle() != null;
    }

    public static SubCategory getSubCategoryById(long idSubCategoryToDelete) {
        return new Select()
                .from(SubCategory.class)
                .where(Condition.column(SubCategory$Table.ID).is(idSubCategoryToDelete))
                .querySingle();
    }
}
