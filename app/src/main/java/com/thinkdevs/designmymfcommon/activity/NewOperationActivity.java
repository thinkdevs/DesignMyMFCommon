package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class NewOperationActivity extends Activity {


    boolean typeOperation; // if TRUE then Expensive

    RadioGroup radioGroupType;
    Spinner spinnerCash;
    Spinner spinnerSubCategory;
    EditText editTextAmount;
    EditText editTextComment;

    List<SubCategory> listSubCategoryExpense;
    List<SubCategory> listSubCategoryProfits;
    List<CashAccount> listCashes;

    List<String> listNamesSubCategoriesExpense; // Для адаптера
    List<String> listNamesSubCategoriesProfit; // Для адаптера
    List<String> listNamesCash; // Для адаптера

    ArrayAdapter<String> adapterExpense; // Адаптер расхода
    ArrayAdapter<String> adapterProfit;  // Адаптер дохода


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            setTitle(extras.getString(NamesOfParametrs.ACTIVITY_TITLE));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        radioGroupType = ((RadioGroup) findViewById(R.id.radioGroup_type_operation));
        typeOperation = (radioGroupType.getCheckedRadioButtonId() == R.id.radioButton_expense);

        spinnerCash = ((Spinner) findViewById(R.id.spinner_cash));
        spinnerSubCategory = ((Spinner) findViewById(R.id.spinner_category));
        editTextAmount = ((EditText) findViewById(R.id.editText_sum));
        editTextComment = ((EditText) findViewById(R.id.editText_comment));

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

        listCashes = new Select().from(CashAccount.class).queryList();
        listNamesCash = new ArrayList<>();
        if(listCashes.size() != 0){
            for(CashAccount cash : listCashes){
                listNamesCash.add(cash.getTitle());
            }
        }

        adapterExpense = new ArrayAdapter<String>(
                NewOperationActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesSubCategoriesExpense);

        adapterProfit = new ArrayAdapter<String>(
                NewOperationActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesSubCategoriesProfit);

        spinnerSubCategory.setAdapter(adapterExpense);

        spinnerCash.setAdapter(
                new ArrayAdapter<String>(NewOperationActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesCash));

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_expense:
                        typeOperation = true;
                        spinnerSubCategory.setAdapter(adapterExpense);
                        break;
                    case R.id.radioButton_profit:
                        typeOperation = false;
                        spinnerSubCategory.setAdapter(adapterProfit);
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_operation, menu);
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

        // ***************************** Сохранение операции ************************************
        if(id == R.id.action_save){

            Operation operation = new Operation();
            // Получаем подкатегорию
            String stringSubCategory = String.valueOf(((((TextView) spinnerSubCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));
            SubCategory subCategory = typeOperation
                    ? SubCategory.getExpenseSubCategoryByTitle(stringSubCategory)
                    : SubCategory.getProfitSubCategoryByTitle(stringSubCategory);

            // Получаем кошелек
            String stringCash = String.valueOf(((((TextView) spinnerCash.getSelectedView().findViewById(android.R.id.text1))).getText()));
            CashAccount cashAccount = CashAccount.getCashAccountByTitle(stringCash);

            // Получаем стоимость
            String amountString = String.valueOf(editTextAmount.getText());
            float amount;
            if(amountString.length() == 0)
                amount = 0;
            else
                amount = Float.parseFloat(String.valueOf(amountString));

            // Получаем комментарий
            String comment = String.valueOf(editTextComment.getText());
            if(amountString.length() == 0)
                comment = "";

            // Сохраняем операцию
            operation.setCashAccount(cashAccount);
            operation.setDate(new Date(System.currentTimeMillis()));
            operation.setSubCategory(subCategory);
            operation.setAmount(amount);
            operation.setComment(comment);

            operation.save();
            float newCashAccountAmount;
            newCashAccountAmount = typeOperation
                    ? (cashAccount.getAmount() - amount)
                    : (cashAccount.getAmount() + amount);
            cashAccount.setAmount(newCashAccountAmount);
            cashAccount.save();

            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
