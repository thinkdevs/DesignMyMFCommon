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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.ArrayList;
import java.util.List;


public class NewOperationTemplateActivity extends Activity {

    private boolean IS_NEW = true;
    private String LOG_TAG = "mylog";

    boolean typeOperation; // if TRUE then Expensive

    EditText   etTitle;
    RadioGroup radioGroupType;
    Spinner    spCategory;
    Spinner    spSubCategory;
    EditText   etAmount;


    List<Category> listCategoriesExpense;
    List<Category> listCategoriesProfit;
    List<SubCategory> listSubCategoryExpense;
    List<SubCategory> listSubCategoryProfits;

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
        if(extras != null)
            setTitle(extras.getString(NamesOfParametrs.ACTIVITY_TITLE));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        etTitle        = (EditText)findViewById(R.id.et_title);
        radioGroupType = ((RadioGroup) findViewById(R.id.rg_type_operation));
        typeOperation  = (radioGroupType.getCheckedRadioButtonId() == R.id.rb_expense);
        spCategory     = ((Spinner) findViewById(R.id.sp_category));
        spSubCategory  = ((Spinner) findViewById(R.id.sp_subCategory));
        etAmount       = (EditText)findViewById(R.id.et_amount);


        listCategoriesExpense = Category.getExpenseCategories();
        listNamesCategoriesExpense = new ArrayList<>();
        if(listCategoriesExpense.size() != 0){
            for(Category categoryExpense : listCategoriesExpense){
                listNamesCategoriesExpense.add(categoryExpense.getName());
            }
        }

        listCategoriesProfit = Category.getProfitCategories();
        listNamesCategoriesProfit = new ArrayList<>();
        if(listCategoriesProfit.size() != 0){
            for(Category categoryProfit : listCategoriesProfit){
                listNamesCategoriesProfit.add(categoryProfit.getName());
            }
        }

        listSubCategoryExpense = listCategoriesExpense.get(0).getSubCategories();
        listNamesSubCategoriesExpense = new ArrayList<>();
        if(listSubCategoryExpense.size() != 0){
            for(SubCategory subCategoryExpense : listSubCategoryExpense){
                listNamesSubCategoriesExpense.add(subCategoryExpense.getName());
            }
        }

        listSubCategoryProfits = listCategoriesProfit.get(0).getSubCategories();
        listNamesSubCategoriesProfit = new ArrayList<>();
        if(listSubCategoryProfits.size() != 0){
            for(SubCategory subCategoryProfit : listSubCategoryProfits){
                listNamesSubCategoriesProfit.add(subCategoryProfit.getName());
            }
        }

        adapterExpense = new ArrayAdapter<String>(
                NewOperationTemplateActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesCategoriesExpense);

        adapterProfit = new ArrayAdapter<String>(
                NewOperationTemplateActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesCategoriesProfit);

        spCategory.setAdapter(adapterExpense);

        spSubCategory.setAdapter(new ArrayAdapter<String>(
                NewOperationTemplateActivity.this,
                android.R.layout.simple_list_item_1,
                getListNamesSubCategoriesByCategory(listCategoriesExpense.get(0))));

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_expense:
                        typeOperation = true;
                        spCategory.setAdapter(adapterExpense);
                        spSubCategory.setAdapter(new ArrayAdapter<String>(
                                NewOperationTemplateActivity.this,
                                android.R.layout.simple_list_item_1,
                                getListNamesSubCategoriesByCategory(listCategoriesExpense.get(0))));
                        break;
                    case R.id.rb_profit:
                        typeOperation = false;
                        spCategory.setAdapter(adapterProfit);
                        spSubCategory.setAdapter(new ArrayAdapter<String>(
                                NewOperationTemplateActivity.this,
                                android.R.layout.simple_list_item_1,
                                getListNamesSubCategoriesByCategory(listCategoriesProfit.get(0))));
                        break;
                }
            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> listNames;
                if (typeOperation)
                    listNames = getListNamesSubCategoriesByCategory(listCategoriesExpense.get(position));
                else
                    listNames = getListNamesSubCategoriesByCategory(listCategoriesProfit.get(position));
                spSubCategory.setAdapter(
                        new ArrayAdapter<String>(
                                NewOperationTemplateActivity.this,
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
            etTitle.        setText(bundle.getString(NamesOfParametrs.NAME));

            if(Operation.TYPE_EXPENSE.equals(bundle.getString(NamesOfParametrs.TYPE))){
                typeOperation = true;
                radioGroupType.check(R.id.rb_expense);

                Log.d("oper", String.valueOf(listNamesCategoriesExpense.size()));
                for(int i = 0; i < listNamesCategoriesExpense.size(); i++) {
                    Log.d("oper", String.valueOf(listNamesCategoriesExpense.get(i)));
                    if (listNamesCategoriesExpense.get(i).equals(bundle.getString(NamesOfParametrs.CATEGORY_NAME)))
                        spCategory.setSelection(i);
                }

                Log.d("oper", String.valueOf(listNamesSubCategoriesExpense.size()));
                for(int i = 0; i < listNamesSubCategoriesExpense.size(); i++) {
                    if (listNamesSubCategoriesExpense.get(i).equals(bundle.getString(NamesOfParametrs.SUB_CATEGORY_NAME)))
                        spSubCategory.setSelection(i);
                }
            }
            else{
                typeOperation = false;
                radioGroupType.check(R.id.rb_profit);
                for(int i = 0; i < listNamesCategoriesProfit.size(); i++) {
                    if (listNamesCategoriesProfit.get(i).equals(bundle.getString(NamesOfParametrs.CATEGORY_NAME)))
                        spCategory.setSelection(i);
                }
                for(int i = 0; i < listNamesSubCategoriesProfit.size(); i++) {
                    if (listNamesSubCategoriesProfit.get(i).equals(bundle.getString(NamesOfParametrs.SUB_CATEGORY_NAME)))
                        spSubCategory.setSelection(i);
                }
            }

            etAmount.setText(bundle.getString(NamesOfParametrs.AMOUNT));
            String categoryName = bundle.getString(NamesOfParametrs.CATEGORY_NAME);
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
            Category category = typeOperation
                    ? Category.getExpenseCategoryByName(stringCategory)
                    : Category.getProfitCategoryByName(stringCategory);

            // Получаем подкатегорию
            String stringSubCategory = String.valueOf(((((TextView) spSubCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));
            SubCategory subCategory = SubCategory.getSubCategoryByName(stringSubCategory, category);

            // Получаем сумму
            String amountString = String.valueOf(etAmount.getText());

            String strTypeOperation = typeOperation
                    ? OperationTemplate.TYPE_EXPENSE
                    : OperationTemplate.TYPE_PROFIT;

            // Проверка условий и сохранение
            if(title == null || title.length() == 0){
                Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
            }
            else if(IS_NEW && OperationTemplate.isExist(title, strTypeOperation)){
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
                    String type = typeOperation
                            ? OperationTemplate.TYPE_EXPENSE
                            : OperationTemplate.TYPE_PROFIT;
                    operationTemplate = OperationTemplate.getOperationTemplateByName(bundle.getString(NamesOfParametrs.NAME), type);
                }

                operationTemplate.setName(title);
                operationTemplate.setType(typeOperation ? OperationTemplate.TYPE_EXPENSE : OperationTemplate.TYPE_PROFIT);

                float amount;
                if(amountString.length() == 0)
                    amount = 0;
                else
                    amount = Float.parseFloat(String.valueOf(amountString));
                operationTemplate.setAmount(amount);

                operationTemplate.setSubCategory(subCategory);

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

    private List<String> getListNamesSubCategoriesByCategory(Category category){
        ArrayList<String> result = new ArrayList<>();
        List<SubCategory> subCategories = category.getSubCategories();
        if(subCategories.size() != 0){
            for(SubCategory subCategory : subCategories){
                result.add(subCategory.getName());
            }
        }
        return result;
    }
}
