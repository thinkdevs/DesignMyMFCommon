package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.ListColorAdapter;
import com.thinkdevs.designmymfcommon.adapter.ListLogoCategorySpinnerAdapter;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Logo;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.ArrayList;
import java.util.List;

public class NewCategoryActivity extends Activity {

    RadioGroup rgTypeHierarchy;
    RadioGroup rgTypeCategory;
    TextView   tvCategory;
    Spinner    spCategory;
    EditText   etName;
    TextView   tvLogo;
    Spinner    spLogo;
    TextView   tvColor;
    Spinner    spColor;

    List<View> listViewsCategory; // Список элементов управления категорий
    List<View> listViewsSubCategory; // Cписок элементов управления подкатегорий

    List<Category> listParentCategoryExpense;
    List<Category> listParentCategoryProfit;
    List<Color>    listColor;
    List<Logo>     listLogoCategory;

    int typeHierarchy;
    int typeCategory ;

    Intent intent;
    Bundle bundle;
    private boolean IS_NEW = true;
    private int oldTypeHierarchy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null)
            setTitle(bundle.getString(Constants.ACTIVITY_TITLE));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        //Тип иерархии
        rgTypeHierarchy = ((RadioGroup) findViewById(R.id.rg_type_hierarchy));
        if(rgTypeHierarchy.getCheckedRadioButtonId() == R.id.rb_category)
            typeHierarchy = Category.PARENT;
        else
            typeHierarchy = Category.SUB;

        //Тип категории
        rgTypeCategory = ((RadioGroup) findViewById(R.id.rg_type_category));
        if(rgTypeCategory.getCheckedRadioButtonId() == R.id.rb_operation_expense)
            typeCategory = Category.TYPE_EXPENSE;
        else
            typeCategory = Category.TYPE_PROFIT;

        tvCategory = ((TextView) findViewById(R.id.tv_category));
        spCategory = ((Spinner) findViewById(R.id.sp_category));
        etName = ((EditText) findViewById(R.id.editText_title));
        tvLogo = ((TextView) findViewById(R.id.textView_logo));
        spLogo = ((Spinner) findViewById(R.id.spinner_logo));
        tvColor = ((TextView) findViewById(R.id.textView_color));
        spColor = ((Spinner) findViewById(R.id.spinner_color));

        listViewsCategory = new ArrayList<>();
        listViewsCategory.add(tvCategory);
        listViewsCategory.add(spCategory);

        listViewsSubCategory = new ArrayList<>();
        listViewsSubCategory.add(tvLogo);
        listViewsSubCategory.add(spLogo);
        listViewsSubCategory.add(tvColor);
        listViewsSubCategory.add(spColor);

        listColor = Color.getColors();
        listLogoCategory = Logo.getAllCategoryLogos();

        listParentCategoryExpense = Category.getExpenseParentCategories();
        listParentCategoryProfit  = Category.getProfitParentCategories();

        spCategory.setAdapter(new ArrayAdapter<String>(
                NewCategoryActivity.this,
                android.R.layout.simple_list_item_1,
                Category.getNamesParentCategories(typeCategory)));

        spColor.setAdapter(new ListColorAdapter(this, listColor));
        spLogo.setAdapter(new ListLogoCategorySpinnerAdapter(this, listLogoCategory));

        rgTypeHierarchy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_category:
                        typeHierarchy = Category.PARENT;
                        hideAndShowViews(listViewsCategory, listViewsSubCategory);
                        break;
                    case R.id.radioButton_subCategory:
                        typeHierarchy = Category.SUB;
                        hideAndShowViews(listViewsSubCategory, listViewsCategory);
                        break;
                }
            }
        });

        rgTypeCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_operation_expense:
                        typeCategory = Category.TYPE_EXPENSE;
                        break;
                    case R.id.rb_operation_profit:
                        typeCategory = Category.TYPE_PROFIT;
                        break;
                }
                spCategory.setAdapter(new ArrayAdapter<String>(
                        NewCategoryActivity.this,
                        android.R.layout.simple_list_item_1,
                        Category.getNamesParentCategories(typeCategory)));
            }
        });

        if(bundle != null) {
            IS_NEW = bundle.getBoolean(Constants.IS_NEW);
            //Редактирование
            if (!IS_NEW) {
                //Как подкатегория
                if (bundle.getInt(Constants.CATEGORY_HIERARCHY) == Category.SUB){
                    //Тип иерархии
                    oldTypeHierarchy = Category.SUB;
                    rgTypeHierarchy.check(R.id.radioButton_subCategory);
                    Category subCategory =
                            Category.getById(
                                    bundle.getLong(Constants.CATEGORY_ID));
                    //Имя
                    etName.setText(subCategory.getName());

                    //Тип категории. Категория.
                    if (subCategory.getType() == Category.TYPE_PROFIT) {
                        rgTypeCategory.check(R.id.rb_operation_profit);
                        for (int i = 0; i < listParentCategoryProfit.size(); i++) {
                            if (listParentCategoryProfit.get(i).getId() == subCategory.getParent().getId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < listParentCategoryExpense.size(); i++) {
                            if (listParentCategoryExpense.get(i).getId() == subCategory.getParent().getId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                }
                //Как категория
                else {
                    Category parentCategory =
                            Category.getById(bundle.getLong(Constants.CATEGORY_ID));
                    //Тип иерархии
                    rgTypeHierarchy.check(R.id.rb_category);
                    //Имя
                    etName.setText(parentCategory.getName());
                    //Цвет
                    for (int i = 0; i < listColor.size(); i++) {
                        if (listColor.get(i).getId() == parentCategory.getColor().getId()) {
                            spColor.setSelection(i);
                            break;
                        }
                    }
                    //Тип категории и категория
                    if (parentCategory.getType() == Category.TYPE_PROFIT) {
                        rgTypeCategory.check(R.id.rb_operation_profit);
                        for (int i = 0; i < listParentCategoryProfit.size(); i++) {
                            if (listParentCategoryProfit.get(i).getId() == parentCategory.getId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < listParentCategoryExpense.size(); i++) {
                            if (listParentCategoryExpense.get(i).getId() == parentCategory.getId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                    //Логотип
                    for (int i = 0; i < listLogoCategory.size(); i++) {
                        if (listLogoCategory.get(i).getId() == parentCategory.getLogo().getId()) {
                            spLogo.setSelection(i);
                            break;
                        }
                    }
                }
            }
            // Новая подкатегория с изветсной категорией
            else {
                Category parentCategory =
                        Category.getById(bundle.getLong(Constants.CATEGORY_ID));
                //Тип иерархии
                rgTypeHierarchy.check(R.id.radioButton_subCategory);

                //Тип категории и категория
                if (parentCategory.getType() == Category.TYPE_PROFIT) {
                    rgTypeCategory.check(R.id.rb_operation_profit);
                    for (int i = 0; i < listParentCategoryProfit.size(); i++) {
                        if (listParentCategoryProfit.get(i).getId() == parentCategory.getId()) {
                            spCategory.setSelection(i);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < listParentCategoryExpense.size(); i++) {
                        if (listParentCategoryExpense.get(i).getId() == parentCategory.getId()) {
                            spCategory.setSelection(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in d.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        //*****************************Сохранение Категории/Подкатегории***********************
        if (id == R.id.action_save) {

            // Сохранение как категории
            if (typeHierarchy == Category.PARENT) {
                Category parentCategory;
                Category subCategoryOld = null;
                if (IS_NEW)
                    parentCategory = new Category();
                else if (isTypeHierarchyChanged()) {
                    subCategoryOld = Category.getById(bundle.getLong(Constants.SUB_CATEGORY_ID));
                    parentCategory = new Category();
                } else
                    parentCategory = Category.getById(bundle.getLong(Constants.CATEGORY_ID));
                String title = String.valueOf(etName.getText());
                parentCategory.setType(typeCategory);

                int logoCategoryId = ((int) (((ImageView) spLogo.getSelectedView().findViewById(R.id.imageView))).getTag());
                Logo logoCategory = Logo.getLogoByResourceId(logoCategoryId);

                int colorId = ((int) (spColor.getSelectedView().findViewById(R.id.tv_color)).getTag());
                Color color = Color.getColorByResourceId(colorId);

                // Проверка условий и сохранение
                if (title == null || title.length() == 0) {
                    Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
                } else if (Category.isExistParent(title, typeCategory)) {
                    Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
                } else {
                    parentCategory.setName(title);
                    parentCategory.setType(typeCategory);
                    parentCategory.setColor(color);
                    parentCategory.setLogo(logoCategory);
                    parentCategory.save();
                    NavUtils.navigateUpFromSameTask(this);
                }
                if (subCategoryOld != null) {
                    changeTypeHierarchySubCategory(subCategoryOld, parentCategory);
                }
                return true;
            }

        // Сохранение как подкатегории
        else {
            Category subCategory;
            Category oldParentCategory = null;
            if (IS_NEW)
                subCategory = new Category();
            else if (isTypeHierarchyChanged()) {
                oldParentCategory = Category.getById(bundle.getLong(Constants.CATEGORY_ID));
                subCategory = new Category();
            } else
                subCategory = Category.getById(bundle.getLong(Constants.SUB_CATEGORY_ID));

            String name = String.valueOf(etName.getText());
            String categoryName = String.valueOf(((TextView) spCategory.getSelectedView().findViewById(android.R.id.text1)).getText());
            Category parentCategory = Category.getParentCategoryByName(categoryName, typeCategory);
            // Проверка условий и сохранение
            if (name == null || name.length() == 0)
                Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
            else if (Category.isExistSub(name, typeCategory))
                Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
            else {
                subCategory.setName(name);
                subCategory.setParent(parentCategory);
                subCategory.save();
                NavUtils.navigateUpFromSameTask(this);
            }

            if (oldParentCategory != null)
                changeTypeHierarchyParentCategory(oldParentCategory, subCategory);

            return true;
          }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Hide and show views
     * @param hide Views will be hide
     * @param show Views will be show
     */
    private void hideAndShowViews (List<View> hide, List<View> show){
        for(View view : hide){
            view.setVisibility(View.GONE);
        }
        for(View view : show){
            view.setVisibility(View.VISIBLE);
        }
    }

    private boolean isTypeHierarchyChanged(){
        return typeHierarchy != oldTypeHierarchy;
    }

    private void changeTypeHierarchySubCategory(Category subCategory, Category parentCategory){

        //Замена категорий
        List<Operation> operations = subCategory.getOperations();
        for(Operation operation : operations){
            operation.setCategory(parentCategory);
            operation.update();
        }
        List<OperationTemplate> templates = subCategory.getTemplates();
        for(OperationTemplate template : templates){
            template.setCategory(parentCategory);
            template.update();
        }

        subCategory.delete();
    }

    private void changeTypeHierarchyParentCategory(Category parentCategory, Category subCategory){

        //Замена категорий
        List<Operation> operations = parentCategory.getOperationsFromAllHierarchy();
        for(Operation operation : operations){
            operation.setCategory(subCategory);
            operation.update();
        }
        List<OperationTemplate> templates = parentCategory.getTemplatesFromAllHierarchy();
        for(OperationTemplate template : templates){
            template.setCategory(subCategory);
            template.update();
        }
        parentCategory.delete();
    }
}
