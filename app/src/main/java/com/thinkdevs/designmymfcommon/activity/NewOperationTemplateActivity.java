package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.ArrayList;
import java.util.List;


public class NewOperationTemplateActivity extends Activity {

    private boolean IS_NEW = true;
    private String LOG_TAG = "mylog";

    boolean typeOperation; // if TRUE then Expensive

    EditText etTitle;
    RadioGroup radioGroupType;
    Spinner spSubCategory;
    EditText etAmount;


    List<SubCategory> listSubCategoryExpense;
    List<SubCategory> listSubCategoryProfits;

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
        spSubCategory  = ((Spinner) findViewById(R.id.sp_category));
        etAmount       = (EditText)findViewById(R.id.et_amount);

        listSubCategoryExpense = SubCategory.getExpenseSubCategories();
        listNamesSubCategoriesExpense = new ArrayList<>();
        if(listSubCategoryExpense.size() != 0){
            for(SubCategory subCategoryExpense : listSubCategoryExpense){
                listNamesSubCategoriesExpense.add(subCategoryExpense.getName());
            }
        }

        listSubCategoryProfits = SubCategory.getProfitSubCategories();
        listNamesSubCategoriesProfit = new ArrayList<>();
        if(listSubCategoryProfits.size() != 0){
            for(SubCategory subCategoryProfit : listSubCategoryProfits){
                listNamesSubCategoriesProfit.add(subCategoryProfit.getName());
            }
        }

        adapterExpense = new ArrayAdapter<String>(
                NewOperationTemplateActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesSubCategoriesExpense);

        adapterProfit = new ArrayAdapter<String>(
                NewOperationTemplateActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesSubCategoriesProfit);

        spSubCategory.setAdapter(adapterExpense);

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_expense:
                        typeOperation = true;
                        spSubCategory.setAdapter(adapterExpense);
                        break;
                    case R.id.rb_profit:
                        typeOperation = false;
                        spSubCategory.setAdapter(adapterProfit);
                        break;
                }
            }
        });

        intent = getIntent();

        bundle = intent.getExtras();
        if(bundle != null){
            IS_NEW = false;
            etTitle.        setText(bundle.getString(NamesOfParametrs.NAME));
            typeOperation = bundle.getBoolean(NamesOfParametrs.TYPE);
            if(typeOperation)
                radioGroupType.check(R.id.rb_expense);
            else
                radioGroupType. check(R.id.rb_profit);

            etAmount.setText(bundle.getString(NamesOfParametrs.AMOUNT));

            String categoryName = bundle.getString(NamesOfParametrs.CATEGORY_NAME);


            if(typeOperation){
                for(int i = 0; i < listSubCategoryExpense.size(); i++){
                    if(listSubCategoryExpense.get(i).getName().equals(categoryName))
                        spSubCategory.setSelection(i);
                }
            }
            else {
                for(int i = 0; i < listSubCategoryProfits.size(); i++){
                    if(listSubCategoryProfits.get(i).getName().equals(categoryName))
                        spSubCategory.setSelection(i);
                }
            }
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

            // Получаем подкатегорию
            String stringSubCategory = String.valueOf(((((TextView) spSubCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));
            SubCategory subCategory = typeOperation
                    ? SubCategory.getExpenseSubCategoryByName(stringSubCategory)
                    : SubCategory.getProfitSubCategoryByName(stringSubCategory);

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
}
