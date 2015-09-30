package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

public class Category extends BaseModel {

    public static final int HIERARCHY_PARENT = 0;
    public static final int HIERARCHY_SUB    = 1;

    public static final int TYPE_EXPENSE   = 1;
    public static final int TYPE_PROFIT    = 2;

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    int type;

    @Column
    int hierarchy;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(
            columnName = "parentCategory_id",
            columnType = Long.class,
            foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Category parentCategory;

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

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE},
            variableName = "subCategories")
    public List<SubCategory> getSubCategories(){
        if(subCategories == null){
            subCategories = new Select()
                    .from(Category.class)
                    .where(Condition.column(Category$Table.PARENTCATEGORY_ID).is(this.id))
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
        return hierarchy;
    }

    public void setHierarchy(int hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
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

    public static SubCategory getById(long id) {
        return new Select()
                .from(Category.class)
                .where(Condition.column(Category$Table.ID).is(id))
                .querySingle();
    }

    public static Category getByName(String title){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                                .begin(Condition.column(Category$Table.NAME).eq(title))
                                .and(Condition.column(Category$Table.HYERARCHY).is(HIERARCHY_PARENT))
                )
                .querySingle();
    }

    public static Category getByName(String title, Category parentCategory){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                                .begin(Condition.column(Category$Table.NAME).eq(title))
                                .and(Condition.column(Category$Table.PARENT).is(parentCategory.getId()))
                )
                .querySingle();
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

    public static SubCategory getExpenseSubCategories(Category parentCategory){
        return null;
    }

    public static SubCategory getProfitSubCategories(Category parentCategory){
        return null;
    }

    public static boolean isExist(String name, Category parentCategory){
        return  new Select()
                .from(Category.class)
                .where(Condition.CombinedCondition
                        .begin(Condition.column(Category$Table.NAME).eq(name))
                        .and  (Condition.column(Category$Table.PARENTCATEGORY_PARENTCATEGORY_ID).is(parentCategory.getId())))
                .querySingle() != null;
    }

}
