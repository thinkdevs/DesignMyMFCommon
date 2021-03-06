package com.thinkdevs.designmymfcommon.database;

import android.util.Log;

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

    private static final String LOG_TAG = "AcCreateCategory";
    private static boolean mDebug = true;

    public static final int PARENT = 0;
    public static final int CHILD  = 1;

    public static final int WITHOUT_TYPE = 0;
    public static final int EXPENSE      = 1;
    public static final int PROFIT       = 2;
    public static final int TRANSFER     = 3;

    public static final int EMPTY_CHILD_ID = 1;
    public static final int EMPTY_PARENT_EXPENSE_ID = 2;
    public static final int EMPTY_PARENT_PROFIT_ID  = 3;
    public static final int TRANSFER_CATEGORY_ID    = 4;

    public static final int CREATE_CATEGORY = 1;
    public static final int CREATE_PARENT   = 2;
    public static final int CREATE_CHILD    = 3;
    public static final int EDIT_PARENT     = 4;
    public static final int EDIT_CHILD      = 5;

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

    List<OperationTemplate> templates;

    List<Category> childs;

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
        for(Category subCategory : getChilds()){
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
        for(Category subCategory : getChilds()){
            templates.addAll(subCategory.getTemplates());
        }
        return templates;
    }

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "childs")
    public List<Category> getChilds(){
        if(childs == null){
            childs = new Select()
                    .from(Category.class)
                    .where(Condition.column(Category$Table.PARENT_PARENT_ID).is(this.id))
                    .queryList();
        }
        return childs;
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
            return CHILD;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
        if(parent != null) {
            this.color = parent.getColor();
            this.icon = parent.getIcon();
            this.type  = parent.getType();
        }
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

    public static Category getById(long id) {
        return new Select()
                .from(Category.class)
                .where(Condition.column(Category$Table.ID).is(id))
                .querySingle();
    }

    public static Category getEmptyParentCategory(int type){
        if(type == Category.EXPENSE)
            return getById(EMPTY_PARENT_EXPENSE_ID);
        else
            return getById(EMPTY_PARENT_PROFIT_ID);
    }

    public static Category getParentCategory(String name, int type){
        if(type == EXPENSE)
            return getParentCategoryExpense(name);
        else
            return sGetParentCategoryProfit(name);
    }

    private static Category getParentCategoryExpense(String name){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.NAME).eq(name))
                    .and(Condition.column(Category$Table.PARENT_PARENT_ID).isNull())
                    .and(Condition.column(Category$Table.TYPE).is(EXPENSE))
                )
                .querySingle();
    }

    private static Category sGetParentCategoryProfit(String name){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.NAME).eq(name))
                    .and(Condition.column(Category$Table.PARENT_PARENT_ID).isNull())
                    .and(Condition.column(Category$Table.TYPE).is(PROFIT))
                )
                .querySingle();
    }

    public static Category getSubCategory(String name, int type){
        if(type == EXPENSE)
            return getSubCategoryExpense(name);
        else
            return getSubCategoryProfit(name);
    }

    private static Category getSubCategoryExpense(String name){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.NAME).eq(name))
                    .and(Condition.column(Category$Table.PARENT_PARENT_ID).isNotNull())
                    .and(Condition.column(Category$Table.TYPE).is(EXPENSE))
                )
                .querySingle();
    }

    private static Category getSubCategoryProfit(String name){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.NAME).eq(name))
                    .and(Condition.column(Category$Table.PARENT_PARENT_ID).isNotNull())
                    .and(Condition.column(Category$Table.TYPE).is(PROFIT))
                )
                .querySingle();
    }

    public static List<Category> getParentCategories(int type){
        if(type == Category.EXPENSE)
            return getExpenseParentCategories();
        else
            return getProfitParentCategories();
    }

    public static List<Category> getExpenseParentCategories(){
        return new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.PARENT_PARENT_ID).isNull())
                    .and(Condition.column(Category$Table.TYPE).is(EXPENSE)))
                .queryList();
    }

    public static List<Category> getProfitParentCategories(){
        return new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                    .begin(Condition.column(Category$Table.PARENT_PARENT_ID).isNull())
                    .and(Condition.column(Category$Table.TYPE).is(PROFIT)))
                .queryList();
    }

    public static List<Category> getParentCategoriesWithoutEmpty(int type){
        List<Category> categories = getParentCategories(type);
        categories.remove(0);
        return categories;
    }

    public static List<Category> getChildCategories(Category parentId){
        List<Category> childCategories = new ArrayList<>();
        childCategories.add(getById(EMPTY_CHILD_ID));
        childCategories.addAll(
                new Select()
                        .from(Category.class)
                        .where(Condition.column(Category$Table.PARENT_PARENT_ID)
                                .is(parentId))
                        .queryList());
       return childCategories;
    }

    public static boolean isExistParent(String name, int type){
        List<String> existNames = getNamesParentCategories(type);
            for(String existName : existNames){
                if(existName.trim().toLowerCase().equals(name.trim().toLowerCase()))
                    return true;
            }
        return false;
    }

    public static boolean isExistChild(String name, Category parent) {
        List<String> existNames = parent.getNamesChildCategories();
        for(String existName : existNames){
            if(existName.trim().toLowerCase().equals(name.trim().toLowerCase()))
                return true;
        }
        return false;
    }

    public boolean isExistChild(String name){
        return isExistChild(name, this);
    }

    private boolean isParent(){
        return parent == null;
    }

    public static boolean isEmptyChildCategory(long id){
        return id == EMPTY_CHILD_ID;
    }

    public static boolean isEmptyParentCategory(long id){
        return EMPTY_PARENT_EXPENSE_ID == id || id == EMPTY_PARENT_PROFIT_ID;
    }

    public boolean deleteCategory(){
        if(isEmptyParentCategory(id) || isEmptyChildCategory(id))
            return false;
        if(isParent())
            prepToDeleteParent();
        else
            prepToDeleteChild();
        delete();
        return true;
    }

    public void deleteChilds(){
        if(!isParent()){
            return;
        }
        for(Category category : getChilds()){
            category.deleteCategory();
        }
    }

    private void prepToDeleteChild(){
        if(operations != null){
            for(Operation operation : operations){
                operation.setCategory(parent);
                operation.update();
            }
        }
        if(templates != null){
            for(OperationTemplate template : templates){
                template.setCategory(parent);
                template.update();
            }
        }
    }

    private void prepToDeleteParent(){
        List<Operation> operations = getOperationsFromAllHierarchy();
        if(operations.size() != 0){
            for(Operation operation : getOperationsFromAllHierarchy()){
                operation.setCategory(getEmptyParentCategory(type));
                operation.update();
            }
        }
        List<OperationTemplate> templates = getTemplatesFromAllHierarchy();
        if(templates != null) {
            for (OperationTemplate template : templates) {
                template.setCategory(getEmptyParentCategory(type));
                template.update();
            }
        }
    }

    public List<String> getNamesChildCategories(){
        List<String> names = new ArrayList<>();
        for(Category category : getChilds()){
            names.add(category.getName());
        }
        return names;
    }

    public static List<String> getNamesParentCategories(int type){
        if(type == Category.EXPENSE)
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

    public  void updateSubs (){
        List<Category> subs = this.getChilds();
        for(Category sub : subs){
            sub.setParent(this);
            sub.save();
        }
    }

    @Override
    public void delete() {
        setParent(null);
        super.delete();
    }
}
