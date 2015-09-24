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

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Category;
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
    Spinner    spCashAccount;
    Spinner    spCategory;
    Spinner    spSubCategory;
    EditText   etAmount;
    EditText   etComment;
    EditText   etTime;
    EditText   etDate;


    List<Category> listCategoriesExpense;
    List<Category> listCategoriesProfit;
    List<SubCategory> listSubCategoryExpense;
    List<SubCategory> listSubCategoryProfits;
    List<CashAccount> listCashAccounts;

    List<String> listNamesCategoriesExpense;
    List<String> listNamesCategoriesProfit;
    List<String> listNamesSubCategoriesExpense; // Для адаптера
    List<String> listNamesSubCategoriesProfit; // Для адаптера
    List<String> listCashAccountNames; // Для адаптера

    ArrayAdapter<String> adapterExpense; // Адаптер расхода
    ArrayAdapter<String> adapterProfit;  // Адаптер дохода

    Intent intent;
    Bundle bundle;
    long time;
    float oldAmount;
    String oldCashAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);


        intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null)
            setTitle(bundle.getString(NamesOfParametrs.ACTIVITY_TITLE));


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rgTypeOperation = ((RadioGroup) findViewById(R.id.rg_type_operation));
        typeOperation = (rgTypeOperation.getCheckedRadioButtonId() == R.id.rb_operation_expense);

        spCashAccount = ((Spinner) findViewById(R.id.sp_cash));
        spCategory    = ((Spinner) findViewById(R.id.sp_category));
        spSubCategory = ((Spinner) findViewById(R.id.sp_subCategory));
        etAmount      = ((EditText) findViewById(R.id.et_amount));
        etComment     = ((EditText) findViewById(R.id.et_comment));
        etTime        = ((EditText) findViewById(R.id.et_time));
        etDate        = ((EditText) findViewById(R.id.et_date));



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
                listNamesCategoriesExpense);

        adapterProfit = new ArrayAdapter<String>(
                NewOperationActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesCategoriesProfit);

        spCategory.setAdapter(adapterExpense);


        spSubCategory.setAdapter(new ArrayAdapter<String>(
                NewOperationActivity.this,
                android.R.layout.simple_list_item_1,
                getListNamesSubCategoriesByCategory(listCategoriesExpense.get(0))));

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
                        spCategory.setAdapter(adapterExpense);
                        spSubCategory.setAdapter(new ArrayAdapter<String>(
                                NewOperationActivity.this,
                                android.R.layout.simple_list_item_1,
                                getListNamesSubCategoriesByCategory(listCategoriesExpense.get(0))));
                        break;
                    case R.id.rb_operation_profit:
                        typeOperation = false;
                        spCategory.setAdapter(adapterProfit);
                        spSubCategory.setAdapter(new ArrayAdapter<String>(
                                NewOperationActivity.this,
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
                if(typeOperation)
                    listNames = getListNamesSubCategoriesByCategory(listCategoriesExpense.get(position));
                else
                    listNames = getListNamesSubCategoriesByCategory(listCategoriesProfit.get(position));
                spSubCategory.setAdapter(
                        new ArrayAdapter<String>(
                                NewOperationActivity.this,
                                android.R.layout.simple_list_item_1,
                                listNames));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(bundle != null){
            Log.d("oper", "whith bundle");
            if(bundle.containsKey(NamesOfParametrs.IS_NEW))
                IS_NEW = bundle.getBoolean(NamesOfParametrs.IS_NEW);

            if(Operation.TYPE_EXPENSE.equals(bundle.getString(NamesOfParametrs.TYPE))){
                typeOperation = true;
                rgTypeOperation.check(R.id.rb_operation_expense);

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
                rgTypeOperation.check(R.id.rb_operation_profit);
                for(int i = 0; i < listNamesCategoriesProfit.size(); i++) {
                    if (listNamesCategoriesProfit.get(i).equals(bundle.getString(NamesOfParametrs.CATEGORY_NAME)))
                        spCategory.setSelection(i);
                }
                for(int i = 0; i < listNamesSubCategoriesProfit.size(); i++) {
                    if (listNamesSubCategoriesProfit.get(i).equals(bundle.getString(NamesOfParametrs.SUB_CATEGORY_NAME)))
                        spSubCategory.setSelection(i);
                }
            }

            oldCashAccount = bundle.getString(NamesOfParametrs.CASH_ACCOUNT_NAME);
            for(int i = 0; i < listCashAccountNames.size(); i++){
                if(listCashAccountNames.get(i).equals(oldCashAccount))
                    spCashAccount.setSelection(i);
            }

            time = bundle.getLong(NamesOfParametrs.DATE);

            if(bundle.getString(NamesOfParametrs.AMOUNT) != null){
                oldAmount = Float.parseFloat(bundle.getString(NamesOfParametrs.AMOUNT));
                etAmount.setText(bundle.getString(NamesOfParametrs.AMOUNT).substring(1));
            }
        }
        Log.d("oper", "create Activite");
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
            Log.d("oper", "сохраняем");
            Operation operation;
            if(IS_NEW)
                operation = new Operation();
            else
                operation = Operation.getOperationByTime(time);
            // Получаем подкатегорию
            String stringSubCategory = String.valueOf(((((TextView) spSubCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));
            SubCategory subCategory = typeOperation
                    ? SubCategory.getExpenseSubCategoryByName(stringSubCategory)
                    : SubCategory.getProfitSubCategoryByName(stringSubCategory);
            Log.d("oper", "получаем категорию =" + subCategory);

            // Получаем кошелек
            String stringCashAccount = String.valueOf(((((TextView) spCashAccount.getSelectedView().findViewById(android.R.id.text1))).getText()));
            CashAccount cashAccount = CashAccount.getCashAccountByName(stringCashAccount);
            Log.d("oper", "получаем счет =" + cashAccount);

            // Получаем стоимость
            String amountString = String.valueOf(etAmount.getText());
            Log.d("oper", "получаем стоимость =" + amountString);
            float amount;
            if(amountString.length() == 0)
                amount = 0;
            else
                amount = Float.parseFloat(String.valueOf(amountString));

            // Получаем комментарий
            String comment = String.valueOf(etComment.getText());
            Log.d("oper", "получаем стоимость =" + comment);
            if(comment.length() == 0)
                comment = "";

            // Сохраняем операцию
            operation.setType(typeOperation ? Operation.TYPE_EXPENSE : Operation.TYPE_PROFIT);
            operation.setCashAccount(cashAccount);
            operation.setDate(new Date(System.currentTimeMillis()));
            operation.setSubCategory(subCategory);
            operation.setAmount(amount);
            operation.setComment(comment);

            if(IS_NEW){
                operation.save();
                float newCashAccountAmount;
                newCashAccountAmount = typeOperation
                        ? (cashAccount.getAmount() - amount)
                        : (cashAccount.getAmount() + amount);
                cashAccount.setAmount(newCashAccountAmount);
                cashAccount.update();
                Log.d("tag", "New Operation Activity - 'save'");
            }
            else {
                operation.update();
                float oldCashAccountAmount = CashAccount.getCashAccountByName(oldCashAccount).getAmount();
                CashAccount oldCash = CashAccount.getCashAccountByName(oldCashAccount);
                Log.d("oper", String.valueOf(oldCashAccountAmount - oldAmount));
                oldCash.setAmount(oldCashAccountAmount - oldAmount);
                oldCash.update();

                float newCashAccountAmount;
                newCashAccountAmount = typeOperation
                        ? (cashAccount.getAmount() - amount)
                        : (cashAccount.getAmount() + amount);
                cashAccount.setAmount(newCashAccountAmount);
                cashAccount.update();
                Log.d("tag", "New Operation Activity - 'update'");
            }

            NavUtils.navigateUpFromSameTask(this);
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
