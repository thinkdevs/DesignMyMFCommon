package com.thinkdevs.designmymfcommon.operationtemplates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.ArrayList;
import java.util.List;


public class AcCreateOperationTemplate extends AppCompatActivity {

    private boolean IS_NEW = true;

    int typeOperation;

    EditText   etTitle;
    RadioGroup radioGroupType;
    Spinner    spCategory;
    Spinner    spSubCategory;
    EditText   etAmount;


    List<Category> listCategoriesExpense;
    List<Category> listCategoriesProfit;
    List<Category> listSubCategoryExpense;
    List<Category> listSubCategoryProfits;

    List<String> listNamesCategoriesExpense;
    List<String> listNamesCategoriesProfit;
    List<String> listNamesSubCategoriesExpense; // Для адаптера
    List<String> listNamesSubCategoriesProfit; // Для адаптера

    ArrayAdapter<String> adapterExpense; // Адаптер расхода
    ArrayAdapter<String> adapterProfit;  // Адаптер дохода

    Intent intent;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation_template);

        Bundle extras = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(extras != null)
            toolbar.setTitle(extras.getString(Constants.ACTIVITY_TITLE));
        else
            toolbar.setTitle(getResources().getString(R.string.title_activity_new_operation_template));

        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        etTitle        = (EditText)findViewById(R.id.et_title);
        radioGroupType = ((RadioGroup) findViewById(R.id.rg_type_operation));

        //Тип операции
        if(radioGroupType.getCheckedRadioButtonId() == R.id.rb_expense)
            typeOperation = Category.EXPENSE;
        else
            typeOperation = Category.PROFIT;

        spCategory     = ((Spinner) findViewById(R.id.sp_category));
        spSubCategory  = ((Spinner) findViewById(R.id.sp_subCategory));
        etAmount       = (EditText)findViewById(R.id.et_amount);

        listNamesSubCategoriesProfit = new ArrayList<>();
        listNamesSubCategoriesExpense = new ArrayList<>();


        listCategoriesExpense = Category.getParentCategories(typeOperation);
        listNamesCategoriesExpense = new ArrayList<>();
        if(listCategoriesExpense.size() != 0){
            for(Category parentCategoryExpense : listCategoriesExpense){
                listNamesCategoriesExpense.add(parentCategoryExpense.getName());
            }
        }

        listCategoriesProfit = Category.getParentCategories(typeOperation);
        listNamesCategoriesProfit = new ArrayList<>();
        if(listCategoriesProfit.size() != 0){
            for(Category parentCategoryProfit : listCategoriesProfit){
                listNamesCategoriesProfit.add(parentCategoryProfit.getName());
            }
        }

        if(listCategoriesExpense.size() != 0) {
            listSubCategoryExpense = listCategoriesExpense.get(0).getChilds();
            listNamesSubCategoriesExpense.add("");
            if (listSubCategoryExpense.size() != 0) {
                for (Category subCategoryExpense : listSubCategoryExpense) {
                    listNamesSubCategoriesExpense.add(subCategoryExpense.getName());
                }
            }
        }
        if(listCategoriesProfit.size() != 0) {
            listSubCategoryProfits = listCategoriesProfit.get(0).getChilds();
            listNamesSubCategoriesProfit.add("");
            if (listSubCategoryProfits.size() != 0) {
                for (Category subCategoryProfit : listSubCategoryProfits) {
                    listNamesSubCategoriesProfit.add(subCategoryProfit.getName());
                }
            }
        }

        adapterExpense = new ArrayAdapter<String>(
                AcCreateOperationTemplate.this,
                android.R.layout.simple_list_item_1,
                listNamesCategoriesExpense);

        adapterProfit = new ArrayAdapter<String>(
                AcCreateOperationTemplate.this,
                android.R.layout.simple_list_item_1,
                listNamesCategoriesProfit);

        spCategory.setAdapter(adapterExpense);

        List<String> subCategoryNames;
        if(listCategoriesExpense.size() != 0)
            subCategoryNames = listCategoriesExpense.get(0).getNamesChildCategories();
        else
            subCategoryNames = new ArrayList<>();

        spSubCategory.setAdapter(new ArrayAdapter<String>(
                AcCreateOperationTemplate.this,
                android.R.layout.simple_list_item_1,
                subCategoryNames));

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                List<String> subCategoryNameList;
                switch (checkedId) {
                    case R.id.rb_expense:
                        typeOperation = Category.EXPENSE;
                        spCategory.setAdapter(adapterExpense);
                        if(listCategoriesExpense.size() != 0)
                            subCategoryNameList = listCategoriesExpense.get(0).getNamesChildCategories();
                        else
                            subCategoryNameList = new ArrayList<String>();
                        spSubCategory.setAdapter(new ArrayAdapter<String>(
                                AcCreateOperationTemplate.this,
                                android.R.layout.simple_list_item_1,
                                subCategoryNameList));
                        break;
                    case R.id.rb_profit:
                        typeOperation = Category.PROFIT;
                        if(listCategoriesExpense.size() != 0)
                            subCategoryNameList = listCategoriesProfit.get(0).getNamesChildCategories();
                        else
                            subCategoryNameList = new ArrayList<String>();
                        spCategory.setAdapter(adapterProfit);
                        spSubCategory.setAdapter(new ArrayAdapter<String>(
                                AcCreateOperationTemplate.this,
                                android.R.layout.simple_list_item_1,
                                subCategoryNameList));
                        break;
                }
            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> listNames;
                if(typeOperation == Category.EXPENSE)
                    listNames = listCategoriesExpense.get(position).getNamesChildCategories();
                else
                    listNames = listCategoriesProfit.get(position).getNamesChildCategories();
                spSubCategory.setAdapter(
                        new ArrayAdapter<String>(
                                AcCreateOperationTemplate.this,
                                android.R.layout.simple_list_item_1,
                                listNames));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null){
            IS_NEW = false;
            etTitle.setText(bundle.getString(Constants.NAME));

            OperationTemplate operationTemplate = OperationTemplate.getByID(bundle.getLong(Constants.OPERATION_TEMPLATE_ID));

            if(Category.EXPENSE == operationTemplate.getType()){
                typeOperation = Category.EXPENSE;
                radioGroupType.check(R.id.rb_expense);

                Log.d("oper", String.valueOf(listNamesCategoriesExpense.size()));
                for(int i = 0; i < listNamesCategoriesExpense.size(); i++) {
                    Log.d("oper", String.valueOf(listNamesCategoriesExpense.get(i)));
                    if (listNamesCategoriesExpense.get(i).equals(bundle.getString(Constants.CATEGORY_NAME)))
                        spCategory.setSelection(i);
                }

                Log.d("oper", String.valueOf(listNamesSubCategoriesExpense.size()));
                for(int i = 0; i < listNamesSubCategoriesExpense.size(); i++) {
                    if (listNamesSubCategoriesExpense.get(i).equals(bundle.getString(Constants.SUB_CATEGORY_NAME)))
                        spSubCategory.setSelection(i);
                }
            }
            else{
                typeOperation = Category.PROFIT;
                radioGroupType.check(R.id.rb_profit);
                for(int i = 0; i < listNamesCategoriesProfit.size(); i++) {
                    if (listNamesCategoriesProfit.get(i).equals(bundle.getString(Constants.CATEGORY_NAME)))
                        spCategory.setSelection(i);
                }
                for(int i = 0; i < listNamesSubCategoriesProfit.size(); i++) {
                    if (listNamesSubCategoriesProfit.get(i).equals(bundle.getString(Constants.SUB_CATEGORY_NAME)))
                        spSubCategory.setSelection(i);
                }
            }

            etAmount.setText(bundle.getString(Constants.AMOUNT));
            String categoryName = bundle.getString(Constants.CATEGORY_NAME);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_cash, menu);
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

        //**************************** Сохранение Шаблона *****************************//

        if(id == R.id.action_save){

            // Получение Названия
            String title = String.valueOf(etTitle.getText());

            String stringCategory = String.valueOf(((((TextView) spCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));

            //Получаем категорию
            Category parentCategory = Category.getParentCategory(stringCategory, typeOperation);

            // Получаем подкатегорию
            String stringSubCategory = String.valueOf(((((TextView) spSubCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));
            Category subCategory = null;
            if(!"".equals(stringSubCategory))
                subCategory = Category.getSubCategory(stringSubCategory, typeOperation);

            // Получаем сумму
            String amountString = String.valueOf(etAmount.getText());

            // Проверка условий и сохранение
            if(title == null || title.length() == 0){
                Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
            }
            else if(IS_NEW && OperationTemplate.isExist(title, typeOperation)){
                Toast.makeText(this, "Шаблон с таким именем уже существует", Toast.LENGTH_LONG).show();
            }
            else if (amountString == null || amountString.length() == 0){
                Toast.makeText(this, "Введите сумму", Toast.LENGTH_LONG).show();
            }
            else {
                OperationTemplate operationTemplate;
                if(IS_NEW)
                        operationTemplate = new OperationTemplate();
                else {
                    operationTemplate = OperationTemplate.getByID(bundle.getLong(Constants.OPERATION_TEMPLATE_ID));
                }

                operationTemplate.setName(title);
                operationTemplate.setType(typeOperation);

                long amount;
                if(amountString.length() == 0)
                    amount = 0;
                else
                    amount = (long)Float.parseFloat(String.valueOf(amountString));
                operationTemplate.setAmount(amount);

                if(subCategory != null)
                    operationTemplate.setCategory(subCategory);
                else
                    operationTemplate.setCategory(parentCategory);

                if(IS_NEW)
                    operationTemplate.save();
                else
                    operationTemplate.update();

                NavUtils.navigateUpFromSameTask(this);
                Log.d("tag", "New Operation Template Activity - 'save'");
                }

            // Возвращаемся назад после сохранения
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
