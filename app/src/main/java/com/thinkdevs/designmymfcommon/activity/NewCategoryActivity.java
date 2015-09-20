package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
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
import com.thinkdevs.designmymfcommon.database.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class NewCategoryActivity extends Activity {

    RadioGroup radioGroupType;
    RadioGroup radioGroupTypeCategory;
    TextView textViewCategory;
    Spinner spinnerCategory;
    EditText editTextTitle;
    TextView textViewLogo;
    Spinner spinnerLogo;
    TextView textViewColor;
    Spinner spinnerColor;

    List<View> listViewsCategory; // Список элементов управления категорий
    List<View> listViewsSubCategory; // Cписок элементов управления подкатегорий

    List<Category> listCategoryExpense;
    List<String> listNamesCategoriesExpense;
    List<Category> listCategoryProfit;
    List<String> listNamesCategoriesProfit;
    List<Color> listColor;
    List<Logo> listLogoCategory;

    boolean type; //if TRUE then CategoryInterface
    boolean typeCategory ; // if TRUE then Expensive


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        radioGroupType = ((RadioGroup) findViewById(R.id.radioGroup_type));
        type = (radioGroupType.getCheckedRadioButtonId() == R.id.radioButton_category);

        radioGroupTypeCategory = ((RadioGroup) findViewById(R.id.radioGroup_type_category));
        typeCategory = (radioGroupTypeCategory.getCheckedRadioButtonId() == R.id.radioButton_expense);

        textViewCategory = ((TextView) findViewById(R.id.textView_category));
        spinnerCategory = ((Spinner) findViewById(R.id.spinner_category));

        editTextTitle = ((EditText) findViewById(R.id.editText_title));

        textViewLogo = ((TextView) findViewById(R.id.textView_logo));
        spinnerLogo = ((Spinner) findViewById(R.id.spinner_logo));

        textViewColor = ((TextView) findViewById(R.id.textView_color));
        spinnerColor = ((Spinner) findViewById(R.id.spinner_color));

        listViewsCategory = new ArrayList<>();
        listViewsCategory.add(textViewCategory);
        listViewsCategory.add(spinnerCategory);

        listViewsSubCategory = new ArrayList<>();
        listViewsSubCategory.add(textViewLogo);
        listViewsSubCategory.add(spinnerLogo);
        listViewsSubCategory.add(textViewColor);
        listViewsSubCategory.add(spinnerColor);

        listCategoryExpense = Category.getExpenseCategories();
        listCategoryProfit = Category.getProfitCategories();
        listColor = Color.getColors();
        listLogoCategory = Logo.getAllCategoryLogos();

        listNamesCategoriesExpense = new ArrayList<>();
        if(listCategoryExpense.size() != 0){
            for(Category category : listCategoryExpense){
                listNamesCategoriesExpense.add(category.getTitle());
            }
        }

        spinnerCategory.setAdapter(new ArrayAdapter<String>(NewCategoryActivity.this, android.R.layout.simple_list_item_1, listNamesCategoriesExpense));

        listNamesCategoriesProfit = new ArrayList<>();
        if(listCategoryProfit.size() != 0){
            for(Category category : listCategoryProfit){
                listNamesCategoriesProfit.add(category.getTitle());
            }
        }

        spinnerColor.setAdapter(new ListColorAdapter(this, listColor));
        spinnerLogo.setAdapter(new ListLogoCategorySpinnerAdapter(this, listLogoCategory));

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_category:
                        type = true;
                        hideAndShowViews(listViewsCategory, listViewsSubCategory);
                        break;
                    case R.id.radioButton_subCategory:
                        type = false;
                        hideAndShowViews(listViewsSubCategory, listViewsCategory);
                        break;
                }
            }
        });

        radioGroupTypeCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_expense:
                        typeCategory = true;
                        spinnerCategory.setAdapter(new ArrayAdapter<String>(NewCategoryActivity.this, android.R.layout.simple_list_item_1, listNamesCategoriesExpense));
                        break;
                    case R.id.radioButton_profit:
                        typeCategory = false;
                        spinnerCategory.setAdapter(new ArrayAdapter<String>(NewCategoryActivity.this, android.R.layout.simple_list_item_1, listNamesCategoriesProfit));
                        break;
                }
            }
        });
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
            if(type){
                Category category = new Category();
                String title = String.valueOf(editTextTitle.getText());
                String categoryType  = typeCategory ? Category.TYPE_EXPENSE : Category.TYPE_PROFIT;
                category.setType(categoryType);

                int  logoCategoryId = ((int) (((ImageView) spinnerLogo.getSelectedView().findViewById(R.id.imageView))).getTag());
                Logo logoCategory = Logo.getLogoByResourceId(logoCategoryId);

                int   colorId = ((int) ((TextView) spinnerColor.getSelectedView().findViewById(android.R.id.text1)).getTag());
                Color color   = Color.getColorByResourceId(colorId);

                // Проверка условий и сохранение
                if(title == null || title.length() == 0){
                    Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
                }
                else if(Category.isExist(title, categoryType)){
                    Toast.makeText(this, "Категория с таким именем уже существует", Toast.LENGTH_LONG).show();
                }
                else {
                    category.setTitle(title);
                    category.setType(categoryType);
                    category.setColor(color);
                    category.setLogo(logoCategory);
                    category.save();
                }
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            // Сохранение как подкатегории
            else {
                SubCategory subCategory = new SubCategory();
                String title = String.valueOf(editTextTitle.getText());
                String categoryString = String.valueOf(((TextView) spinnerCategory.getSelectedView().findViewById(android.R.id.text1)).getText());
                Category category  = typeCategory ? Category.getExpenseCategoryByTitle(title) : Category.getProfitCategoryByTitle(title);
                // Проверка условий и сохранение
                if(title == null || title.length() == 0){
                    Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
                }
                else if(SubCategory.isExist(title, category)){
                    Toast.makeText(this, "Подкатегория с таким именем уже существует", Toast.LENGTH_LONG).show();
                }
                else {
                    subCategory.setName(title);
                    subCategory.setCategory(category);
                    subCategory.save();
                }
                NavUtils.navigateUpFromSameTask(this);
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
}
