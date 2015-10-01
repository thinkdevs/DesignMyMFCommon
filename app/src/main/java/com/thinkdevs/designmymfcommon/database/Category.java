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
 * Таблица с категориями
 */
@Table(databaseName = MoneyFlowDataBase.NAME)
public class Category extends BaseModel {

    public static final int PARENT = 0;
    public static final int SUB    = 1;

    public static final int TYPE_WITHOUT = 0;
    public static final int TYPE_EXPENSE = 1;
    public static final int TYPE_PROFIT  = 2;

    public static final int EMPTY_SUB_ID = 1;
    public static final int EMPTY_PARENT_EXPENSE_ID = 2;
    public static final int EMPTY_PARENT_PROFIT_ID  = 3;

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    int type;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "parent_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Category parent;

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

    List<OperationTemplate> templates;

    List<Category> subCategories;

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

    public List<Operation> getOperationsFromAllHierarchy(){
        List<Operation> operations = getOperations();
        for(Category subCategory : getSubCategories()){
            operations.addAll(subCategory.getOperations());
        }
        return operations;
    }

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "templates")
    public List<OperationTemplate> getTemplates() {
        if (templates == null) {
            templates = new Select()
                    .from(OperationTemplate.class)
                    .where(Condition.column(OperationTemplate$Table.CATEGORY_CATEGORY_ID).is(this.id))
                    .queryList();
        }
        return templates;
    }

    public List<OperationTemplate> getTemplatesFromAllHierarchy(){
        List<OperationTemplate> templates = getTemplates();
        for(Category subCategory : getSubCategories()){
            templates.addAll(subCategory.getTemplates());
        }
        return templates;
    }

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "subCategories")
    public List<Category> getSubCategories(){
        if(subCategories == null){
            subCategories = new Select()
                    .from(Category.class)
                    .where(Condition.column(Category$Table.PARENT_PARENT_ID).is(this.id))
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

    public int getHierarchy() {
        if(parent == null)
            return PARENT;
        else
            return SUB;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
        if(parent != null) {
            this.color = parent.getColor();
            this.logo = parent.getLogo();
            this.type = parent.getType();
        }
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

    public static Category getById(long id) {
        return new Select()
                .from(Category.class)
                .where(Condition.column(Category$Table.ID).is(id))
                .querySingle();
    }

    public static Category getEmptyParentCategory(int type){
        if(type == Category.TYPE_EXPENSE)
            return getById(EMPTY_PARENT_EXPENSE_ID);
        else
            return getById(EMPTY_PARENT_PROFIT_ID);
    }

    public static Category getParentCategoryByName(String name, int type){
        if(type == TYPE_EXPENSE)
            return getParentCategoryExpenseByName(name);
        else
            return getParentCategoryProfitByName(name);
    }

    private static Category getParentCategoryExpenseByName(String name){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.NAME).eq(name))
                    .and(Condition.column(Category$Table.PARENT_PARENT_ID).isNull())
                    .and(Condition.column(Category$Table.TYPE).is(TYPE_EXPENSE))
                )
                .querySingle();
    }

    private static Category getParentCategoryProfitByName(String name){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.NAME).eq(name))
                    .and(Condition.column(Category$Table.PARENT_PARENT_ID).isNull())
                    .and(Condition.column(Category$Table.TYPE).is(TYPE_PROFIT))
                )
                .querySingle();
    }

    public static Category getSubCategoryByName(String name, int type){
        if(type == TYPE_EXPENSE)
            return getSubCategoryExpenseByName(name);
        else
            return getSubCategoryProfitByName(name);
    }

    private static Category getSubCategoryExpenseByName(String name){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.NAME).eq(name))
                    .and(Condition.column(Category$Table.PARENT_PARENT_ID).isNotNull())
                    .and(Condition.column(Category$Table.TYPE).is(TYPE_EXPENSE))
                )
                .querySingle();
    }

    private static Category getSubCategoryProfitByName(String name){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.NAME).eq(name))
                    .and(Condition.column(Category$Table.PARENT_PARENT_ID).isNotNull())
                    .and(Condition.column(Category$Table.TYPE).is(TYPE_PROFIT))
                )
                .querySingle();
    }

    public static List<Category> getParentCategoriesByType(int type){
        if(type == Category.TYPE_EXPENSE)
            return getExpenseParentCategories();
        else
            return getProfitParentCategories();
    }

    public static List<Category> getExpenseParentCategories(){
        return new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.PARENT_PARENT_ID).isNull())
                    .and(Condition.column(Category$Table.TYPE).is(TYPE_EXPENSE)))
                .queryList();
    }

    public static List<Category> getProfitParentCategories(){
        return new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.PARENT_PARENT_ID).isNull())
                    .and(Condition.column(Category$Table.TYPE).is(TYPE_PROFIT)))
                .queryList();
    }

    public static List<Category> getSubCategories(Category parentCategoryID){
        List<Category> subCategories = new ArrayList<>();
        subCategories.add(getById(EMPTY_SUB_ID));
        subCategories.addAll(
             new Select()
                    .from(Category.class)
                    .where(Condition.column(Category$Table.PARENT_PARENT_ID)
                        .is(parentCategoryID))
                    .queryList());
       return subCategories;
    }

    public static boolean isExistParent(String name, int type){
        List<String> names = getNamesParentCategories(type);
            for(String existName : names){
                if(existName.trim().toLowerCase().equals(name.trim().toLowerCase()))
                    return true;
            }
        return false;
    }

    public static boolean isExistSub(String name, Category parent) {
        List<String> names = parent.getNamesSubCategories();
        for(String existName : names){
            if(existName.trim().toLowerCase().equals(name.trim().toLowerCase()))
                return true;
        }
        return false;
    }

    private boolean isParent(){
        return parent == null;
    }

    public static boolean isEmptySubCategory(long id){
        return id == EMPTY_SUB_ID;
    }

    public static boolean isEmptyParentCategory(long id){
        return EMPTY_PARENT_EXPENSE_ID == id || id == EMPTY_PARENT_PROFIT_ID;
    }

    public boolean deleteCategory(){
        if(isEmptyParentCategory(id) || isEmptySubCategory(id))
            return false;
        if(isParent())
            prepToDeleteParent();
        else
            prepToDeleteSub();
        delete();
        return true;
    }

    private void prepToDeleteSub(){
        if(operations != null){
            for(Operation operation : operations){
                operation.setCategory(parent);
            }
        }
        if(templates != null){
            for(OperationTemplate template : templates){
                template.setCategory(parent);
            }
        }
    }

    private void prepToDeleteParent(){
        List<Operation> operations = getOperationsFromAllHierarchy();
        if(operations.size() != 0){
            for(Operation operation : getOperationsFromAllHierarchy()){
                operation.setCategory(getEmptyParentCategory(type));
            }
        }
        List<OperationTemplate> templates = getTemplatesFromAllHierarchy();
        if(templates != null) {
            for (OperationTemplate template : templates) {
                template.setCategory(getEmptyParentCategory(type));
            }
        }
    }

    public List<String> getNamesSubCategories(){
        List<String> names = new ArrayList<>();
        for(Category category : getSubCategories()){
            names.add(category.getName().toLowerCase());
        }
        return names;
    }

    public static List<String> getNamesParentCategories(int type){
        if(type == Category.TYPE_EXPENSE)
            return getNamesExpenseParentCategories();
        else
            return getNamesProfitParentCategories();
    }

    private static List<String> getNamesExpenseParentCategories(){
        List<String> names = new ArrayList<>();
        for(Category category : getExpenseParentCategories()){
            names.add(category.getName());
        }
        return names;
    }

    private static List<String> getNamesProfitParentCategories(){
        List<String> names = new ArrayList<>();
        for(Category category : getProfitParentCategories()){
            names.add(category.getName());
        }
        return names;
    }

}
