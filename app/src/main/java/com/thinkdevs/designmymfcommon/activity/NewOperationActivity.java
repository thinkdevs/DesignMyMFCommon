package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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


    private boolean IS_NEW = true;

    boolean typeOperation; // if TRUE then Expensive

    RadioGroup rgTypeOperation;
    Spinner spCashAccount;
    Spinner    spSubCategory;
    EditText   etAmount;
    EditText   etComment;

    List<SubCategory> listSubCategoryExpense;
    List<SubCategory> listSubCategoryProfits;
    List<CashAccount> listCashAccounts;

    List<String> listNamesSubCategoriesExpense; // Для адаптера
    List<String> listNamesSubCategoriesProfit; // Для адаптера
    List<String> listCashAccountNames; // Для адаптера

    ArrayAdapter<String> adapterExpense; // Адаптер расхода
    ArrayAdapter<String> adapterProfit;  // Адаптер дохода

    Intent intent;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            setTitle(extras.getString(NamesOfParametrs.ACTIVITY_TITLE));


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rgTypeOperation = ((RadioGroup) findViewById(R.id.radioGroup_type_operation));
        typeOperation = (rgTypeOperation.getCheckedRadioButtonId() == R.id.rb_operation_expense);

        spCashAccount = ((Spinner) findViewById(R.id.spinner_cash));
        spSubCategory = ((Spinner) findViewById(R.id.spinner_category));
        etAmount      = ((EditText) findViewById(R.id.editText_sum));
        etComment     = ((EditText) findViewById(R.id.editText_comment));

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

        listCashAccounts = new Select().from(CashAccount.class).queryList();
        listCashAccountNames = new ArrayList<>();
        if(listCashAccounts.size() != 0){
            for(CashAccount cash : listCashAccounts){
                listCashAccountNames.add(cash.getName());
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

        spSubCategory.setAdapter(adapterExpense);

        spCashAccount.setAdapter(
                new ArrayAdapter<String>(NewOperationActivity.this,
                        android.R.layout.simple_list_item_1,
                        listCashAccountNames));

        rgTypeOperation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_operation_expense:
                        typeOperation = true;
                        spSubCategory.setAdapter(adapterExpense);
                        break;
                    case R.id.rb_operation_profit:
                        typeOperation = false;
                        spSubCategory.setAdapter(adapterProfit);
                        break;
                }
            }
        });

        intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null){
            if(Operation.TYPE_EXPENSE.equals(bundle.getString(NamesOfParametrs.TYPE))){
                typeOperation = true;
                rgTypeOperation.check(R.id.rb_operation_expense);
            }
            else{
                typeOperation = false;
                rgTypeOperation.check(R.id.rb_operation_profit);
            }

            for(int i = 0; i < listCashAccountNames.size(); i++){
                if(listCashAccountNames.get(i).equals(bundle.getString(NamesOfParametrs.NAME)))
                    spCashAccount.setSelection(i);
            }
        }

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
            String stringSubCategory = String.valueOf(((((TextView) spSubCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));
            SubCategory subCategory = typeOperation
                    ? SubCategory.getExpenseSubCategoryByName(stringSubCategory)
                    : SubCategory.getProfitSubCategoryByName(stringSubCategory);

            // Получаем кошелек
            String stringCash = String.valueOf(((((TextView) spCashAccount.getSelectedView().findViewById(android.R.id.text1))).getText()));
            CashAccount cashAccount = CashAccount.getCashAccountByName(stringCash);

            // Получаем стоимость
            String amountString = String.valueOf(etAmount.getText());
            float amount;
            if(amountString.length() == 0)
                amount = 0;
            else
                amount = Float.parseFloat(String.valueOf(amountString));

            // Получаем комментарий
            String comment = String.valueOf(etComment.getText());
            if(amountString.length() == 0)
                comment = "";

            // Сохраняем операцию
            operation.setType(typeOperation ? Operation.TYPE_EXPENSE : Operation.TYPE_PROFIT);
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
