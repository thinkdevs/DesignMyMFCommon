package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
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
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.ArrayList;
import java.util.List;

public class NewCategoryActivity extends Activity {

    RadioGroup rgTypeHierarchy;
    RadioGroup rgTypeCategory;
    TextView tvCategory;
    Spinner spCategory;
    EditText etName;
    TextView tvLogo;
    Spinner spLogo;
    TextView tvColor;
    Spinner spColor;

    List<View> listViewsCategory; // Список элементов управления категорий
    List<View> listViewsSubCategory; // Cписок элементов управления подкатегорий

    List<Category> listCategoryExpense;
    List<String> listNamesCategoriesExpense;
    List<Category> listCategoryProfit;
    List<String> listNamesCategoriesProfit;
    List<Color> listColor;
    List<Logo> listLogoCategory;

    boolean typeHierarchy; //if TRUE then Category
    boolean typeCategory ; // if TRUE then Expensive

    Intent intent;
    Bundle bundle;
    private boolean IS_NEW = true;
    private boolean oldTypeHierarchy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null)
            setTitle(bundle.getString(NamesOfParametrs.ACTIVITY_TITLE));


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        rgTypeHierarchy = ((RadioGroup) findViewById(R.id.rg_type_hierarchy));
        typeHierarchy = (rgTypeHierarchy.getCheckedRadioButtonId() == R.id.rb_category);

        rgTypeCategory = ((RadioGroup) findViewById(R.id.rg_type_category));
        typeCategory = (rgTypeCategory.getCheckedRadioButtonId() == R.id.rb_operation_expense);

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

        listCategoryExpense = Category.getExpenseCategories();
        listCategoryProfit = Category.getProfitCategories();
        listColor = Color.getColors();
        listLogoCategory = Logo.getAllCategoryLogos();

        listNamesCategoriesExpense = new ArrayList<>();
        if(listCategoryExpense.size() != 0){
            for(Category category : listCategoryExpense){
                listNamesCategoriesExpense.add(category.getName());
            }
        }

        spCategory.setAdapter(new ArrayAdapter<String>(
                NewCategoryActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesCategoriesExpense));

        listNamesCategoriesProfit = new ArrayList<>();
        if(listCategoryProfit.size() != 0){
            for(Category category : listCategoryProfit){
                listNamesCategoriesProfit.add(category.getName());
            }
        }

        spColor.setAdapter(new ListColorAdapter(this, listColor));
        spLogo.setAdapter(new ListLogoCategorySpinnerAdapter(this, listLogoCategory));

        rgTypeHierarchy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_category:
                        typeHierarchy = true;
                        hideAndShowViews(listViewsCategory, listViewsSubCategory);
                        break;
                    case R.id.radioButton_subCategory:
                        typeHierarchy = false;
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
                        typeCategory = true;
                        spCategory.setAdapter(new ArrayAdapter<String>(
                                NewCategoryActivity.this,
                                android.R.layout.simple_list_item_1,
                                listNamesCategoriesExpense));
                        break;
                    case R.id.rb_operation_profit:
                        typeCategory = false;
                        spCategory.setAdapter(new ArrayAdapter<String>(
                                NewCategoryActivity.this,
                                android.R.layout.simple_list_item_1,
                                listNamesCategoriesProfit));
                        break;
                }
            }
        });

        if(bundle != null) {
            IS_NEW = bundle.getBoolean(NamesOfParametrs.IS_NEW);
            //Редактирование
            if (!IS_NEW) {
                //Как подкатегория
                if (bundle.getBoolean(NamesOfParametrs.IS_SUB_CATEGORY)) {
                    //Тип иерархии
                    oldTypeHierarchy = false;
                    rgTypeHierarchy.check(R.id.radioButton_subCategory);
                    SubCategory subCategory =
                            SubCategory.getSubCategoryById(
                                    bundle.getLong(NamesOfParametrs.SUB_CATEGORY_ID));
                    //Имя
                    etName.setText(subCategory.getName());

                    //Тип категории. Категория.
                    if (!subCategory.getCategory().isExpense()) {
                        rgTypeCategory.check(R.id.rb_operation_profit);
                        for (int i = 0; i < listCategoryProfit.size(); i++) {
                            if (listCategoryProfit.get(i).getId() == subCategory.getCategory().getId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < listCategoryExpense.size(); i++) {
                            if (listCategoryExpense.get(i).getId() == subCategory.getCategory().getId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                }
                //Как категория
                else {
                    Category category =
                            Category.getCategoryById(bundle.getLong(NamesOfParametrs.CATEGORY_ID));
                    //Тип иерархии
                    rgTypeHierarchy.check(R.id.rb_category);
                    //Имя
                    etName.setText(category.getName());
                    //Цвет
                    for (int i = 0; i < listColor.size(); i++) {
                        if (listColor.get(i).getId() == category.getColor().getId()) {
                            spColor.setSelection(i);
                            break;
                        }
                    }
                    //Тип категории и категория
                    if (!category.isExpense()) {
                        rgTypeCategory.check(R.id.rb_operation_profit);
                        for (int i = 0; i < listCategoryProfit.size(); i++) {
                            if (listCategoryProfit.get(i).getId() == category.getId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < listCategoryExpense.size(); i++) {
                            if (listCategoryExpense.get(i).getId() == category.getId()) {
                                spCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                    //Логотип
                    for (int i = 0; i < listLogoCategory.size(); i++) {
                        if (listLogoCategory.get(i).getId() == category.getLogo().getId()) {
                            spLogo.setSelection(i);
                            break;
                        }
                    }
                }
            }
            // Новая подкатегория с изветсной категорией
            else {
                Category category =
                        Category.getCategoryById(bundle.getLong(NamesOfParametrs.CATEGORY_ID));
                //Тип иерархии
                rgTypeHierarchy.check(R.id.radioButton_subCategory);

                //Тип категории и категория
                if (!category.isExpense()) {
                    rgTypeCategory.check(R.id.rb_operation_profit);
                    for (int i = 0; i < listCategoryProfit.size(); i++) {
                        if (listCategoryProfit.get(i).getId() == category.getId()) {
                            spCategory.setSelection(i);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < listCategoryExpense.size(); i++) {
                        if (listCategoryExpense.get(i).getId() == category.getId()) {
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

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        //*****************************Сохранение Категории/Подкатегории***********************
        if(id == R.id.action_save){

            // Сохранение как категории
            if(typeHierarchy){
                Category category;
                if(IS_NEW)
                    category = new Category();
                else if (isTypeHierarchyChanged()){
                    SubCategory.getSubCategoryById(bundle.getLong(NamesOfParametrs.SUB_CATEGORY_ID)).delete();
                    category = new Category();
                }
                else
                    category = Category.getCategoryById(bundle.getLong(NamesOfParametrs.CATEGORY_ID));
                String title = String.valueOf(etName.getText());
                String categoryType  = typeCategory ? Category.TYPE_EXPENSE : Category.TYPE_PROFIT;
                category.setType(categoryType);

                int  logoCategoryId = ((int) (((ImageView) spLogo.getSelectedView().findViewById(R.id.imageView))).getTag());
                Logo logoCategory = Logo.getLogoByResourceId(logoCategoryId);

                int   colorId = ((int) (spColor.getSelectedView().findViewById(R.id.tv_color)).getTag());
                Color color   = Color.getColorByResourceId(colorId);

                // Проверка условий и сохранение
                if(title == null || title.length() == 0){
                    Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
                }
                else if(Category.isExist(title, categoryType)){
                    Toast.makeText(this, "Категория с таким именем уже существует", Toast.LENGTH_LONG).show();
                }
                else {
                    category.setName(title);
                    category.setType(categoryType);
                    category.setColor(color);
                    category.setLogo(logoCategory);
                    category.save();

                    NavUtils.navigateUpFromSameTask(this);
                    Log.d("tag", "New Category Activity - 'save'");
                }

                return true;
            }
            // Сохранение как подкатегории
            else {
                SubCategory subCategory;
                if(IS_NEW)
                    subCategory = new SubCategory();
                else if (isTypeHierarchyChanged()){
                    Category.getCategoryById(bundle.getLong(NamesOfParametrs.CATEGORY_ID)).delete();
                    subCategory = new SubCategory();
                }
                else
                    subCategory = SubCategory.getSubCategoryById(bundle.getLong(NamesOfParametrs.SUB_CATEGORY_ID));

                String name = String.valueOf(etName.getText());
                String categoryName = String.valueOf(((TextView) spCategory.getSelectedView().findViewById(android.R.id.text1)).getText());
                Category category  = typeCategory
                        ? Category.getExpenseCategoryByName(categoryName)
                        : Category.getProfitCategoryByName(categoryName);
                // Проверка условий и сохранение
                if(name == null || name.length() == 0){
                    Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
                }
                else if(SubCategory.isExist(name, category)){
                    Toast.makeText(this, "Подкатегория с таким именем уже существует", Toast.LENGTH_LONG).show();
                }
                else {
                    subCategory.setName(name);
                    subCategory.setCategory(category);
                    subCategory.save();
                    NavUtils.navigateUpFromSameTask(this);
                    Log.d("tag", "New Category Activity - 'save'");
                }

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
}
