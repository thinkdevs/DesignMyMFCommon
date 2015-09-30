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
 * Таблица с категориями расходов
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class ParentCategory {

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    int type;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "logo_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Logo logo;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "color_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Color color;

    List<Operation> operations;

    List<SubCategory> subCategories;

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

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "subCategories")
    public List<SubCategory> getSubCategories(){
        if(subCategories == null){
            subCategories = new Select()
                    .from(SubCategory.class)
                    .where(Condition.column(SubCategory$Table.PARENTCATEGORY_PARENTCATEGORY_ID).is(this.id))
                    .queryList();
        }
        return subCategories;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static List<ParentCategory> getExpenseCategories(){
        return new Select()
                .from(ParentCategory.class)
                .where(Condition.column(ParentCategory$Table.TYPE).is(TYPE_EXPENSE))
                .queryList();
    }

    public static List<ParentCategory> getProfitCategories(){
        return new Select()
                .from(ParentCategory.class)
                .where(Condition.column(ParentCategory$Table.TYPE).is(TYPE_PROFIT))
                .queryList();
    }

    public static ParentCategory getProfitCategoryByName(String title){
        return new Select()
                .from(ParentCategory.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(ParentCategory$Table.NAME).is(title))
                        .and(Condition.column(ParentCategory$Table.TYPE).is(TYPE_PROFIT)))
                .querySingle();
    }

    public static ParentCategory getExpenseCategoryByName(String title){
        return new Select()
                .from(ParentCategory.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(ParentCategory$Table.NAME).is(title))
                        .and(Condition.column(ParentCategory$Table.TYPE).is(TYPE_EXPENSE)))
                .querySingle();
    }

    public static boolean isExist(String name, int type){
        return  new Select()
                .from(ParentCategory.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(ParentCategory$Table.NAME).eq(name))
                        .and  (Condition.column(ParentCategory$Table.TYPE).is(type)))
                .querySingle() != null;
    }

    public boolean isExpense(){
        return TYPE_EXPENSE == this.getType();
    }

    public static ParentCategory getById(long id) {
        return new Select()
                .from(ParentCategory.class)
                .where(Condition.column(ParentCategory$Table.ID).is(id))
                .querySingle();
    }

    @Override
    public int getHierarchy() {
        return HIERARCHY_PARENT;
    }

    public List<OperationTemplate> getOperationTemplates(){
        List<OperationTemplate> templates = new ArrayList<>();
            templates = new Select()
                    .from(OperationTemplate.class)
                    .where(Condition.column(OperationTemplate$Table.CATEGORY_CATEGORY_ID).is(this.id))
                    .queryList();
        return templates;
    }

    public List<Operation> getOperationsFromAllHierarchy(){
        List<Operation> operations = new ArrayList<>();
        operations.addAll(getOperations());
        for(SubCategory subCategory : getSubCategories()){
            operations.addAll(subCategory.getOperations());
        }
        return operations;
    }

    public List<OperationTemplate> getOperationTemplatesFromAllHierarchy(){
        List<OperationTemplate> templates = new ArrayList<>();
        templates.addAll(getOperationTemplates());
        for(SubCategory subCategory : getSubCategories()){
            templates.addAll(subCategory.getOperationTemplates());
        }
        return templates;
    }
}
