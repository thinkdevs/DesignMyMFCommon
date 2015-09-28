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
 * Таблица с категориями расходов
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class Category extends BaseModel {

    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_PROFIT  = "profit";

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name; //название категории

    @Column
    String type; //тип категории

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "logo_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Logo logo; //логотип

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "color_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Color color; //цвет

    List<SubCategory> subCategory; //список подкатегорий

    /**
     * @return Список подкатегорий для текущей категории
     */
    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "subCategory")
    public List<SubCategory> getSubCategories(){
        if(subCategory == null){
            subCategory = new Select()
                    .from(SubCategory.class)
                    .where(Condition.column(SubCategory$Table.CATEGORY_CATEGORY_ID).is(this.id))
                    .queryList();
        }
        return subCategory;
    }

    public long   getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void   setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void   setType(String type) {
        this.type = type;
    }

    public Logo   getLogo() {
        return logo;
    }
    public void   setLogo(Logo logo) {
        this.logo = logo;
    }

    public Color  getColor() {
        return color;
    }
    public void   setColor(Color color) {
        this.color = color;
    }

    public static List<Category> getExpenseCategories(){
        return new Select()
                .from(Category.class)
                .where(Condition.column(Category$Table.TYPE).is(TYPE_EXPENSE))
                .queryList();
    }
    public static List<Category> getProfitCategories(){
        return new Select()
                .from(Category.class)
                .where(Condition.column(Category$Table.TYPE).is(TYPE_PROFIT))
                .queryList();
    }

    public static Category getProfitCategoryByName(String title){
        return new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                .begin(Condition.column(Category$Table.NAME).is(title))
                .and(Condition.column(Category$Table.TYPE).is(Category.TYPE_PROFIT)))
                .querySingle();
    }
    public static Category getExpenseCategoryByName(String title){
        return new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                .begin(Condition.column(Category$Table.NAME).is(title))
                .and(Condition.column(Category$Table.TYPE).is(Category.TYPE_EXPENSE)))
                .querySingle();
    }

    public static boolean isExist(String name, String type){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(Category$Table.NAME).eq(name))
                        .and  (Condition.column(Category$Table.TYPE).eq(type)))
                .querySingle() != null;
    }

    public boolean isExpense(){
        return Category.TYPE_EXPENSE.equals(this.getType());
    }

    public static Category getCategoryById(long idCategoryToDelete) {
        return new Select()
                .from(Category.class)
                .where(Condition.column(Category$Table.ID).is(idCategoryToDelete))
                .querySingle();
    }
}
