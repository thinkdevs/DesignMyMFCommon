package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.ArrayList;
import java.util.List;


public class NewOperationActivity extends Activity {


    private boolean IS_NEW = true;

    int typeOperation;

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
    List<Category> listSubCategoryExpense;
    List<Category> listSubCategoryProfits;
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
    String cashAccountName;
    float oldAmount;
    String oldCashAccountName;
    int oldTypeOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);

        intent = getIntent();
        bundle = intent.getExtras();
        if(bundle != null)
            setTitle(bundle.getString(Constants.ACTIVITY_TITLE));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rgTypeOperation = ((RadioGroup) findViewById(R.id.rg_type_operation));
        if(rgTypeOperation.getCheckedRadioButtonId() == R.id.rb_operation_expense)
            typeOperation = Category.EXPENSE;
        else
            typeOperation = Category.PROFIT;

        spCashAccount = ((Spinner) findViewById(R.id.sp_cash));
        spCategory    = ((Spinner) findViewById(R.id.sp_category));
        spSubCategory = ((Spinner) findViewById(R.id.sp_subCategory));
        etAmount      = ((EditText) findViewById(R.id.et_amount));
        etComment     = ((EditText) findViewById(R.id.et_comment));
        etTime        = ((EditText) findViewById(R.id.et_time));
        etDate        = ((EditText) findViewById(R.id.et_date));

        listNamesSubCategoriesProfit = new ArrayList<>();
        listNamesSubCategoriesExpense = new ArrayList<>();
        listCashAccountNames = new ArrayList<>();

        listCategoriesExpense = Category.getExpenseParentCategories();
        listNamesCategoriesExpense = new ArrayList<>();
        if(listCategoriesExpense.size() != 0){
            for(Category parentCategoryExpense : listCategoriesExpense){
                listNamesCategoriesExpense.add(parentCategoryExpense.getName());
            }
        }

        listCategoriesProfit = Category.getProfitParentCategories();
        listNamesCategoriesProfit = new ArrayList<>();
        if(listCategoriesProfit.size() != 0){
            for(Category parentCategoryProfit : listCategoriesProfit){
                listNamesCategoriesProfit.add(parentCategoryProfit.getName());
            }
        }

        if(listCategoriesExpense.size() != 0) {
            listSubCategoryExpense = listCategoriesExpense.get(0).getSubCategories();
            if (listSubCategoryExpense.size() != 0) {
                for (Category subCategoryExpense : listSubCategoryExpense) {
                    listNamesSubCategoriesExpense.add(subCategoryExpense.getName());
                }
            }
        }
        if(listCategoriesProfit.size() != 0) {
            listSubCategoryProfits = listCategoriesProfit.get(0).getSubCategories();
            if (listSubCategoryProfits.size() != 0) {
                for (Category subCategoryProfit : listSubCategoryProfits) {
                    listNamesSubCategoriesProfit.add(subCategoryProfit.getName());
                }
            }
        }

        listCashAccounts = new Select().from(CashAccount.class).queryList();
        if(listCashAccounts.size() != 0){
            for(CashAccount cash : listCashAccounts){
                listCashAccountNames.add(cash.getName());
            }
        }

        adapterExpense = new ArrayAdapter<String>(
                NewOperationActivity.this,
                android.R.layout.simple_list_item_1,
                Category.getNamesParentCategories(Category.EXPENSE));

        adapterProfit = new ArrayAdapter<String>(
                NewOperationActivity.this,
                android.R.layout.simple_list_item_1,
                Category.getNamesParentCategories(Category.PROFIT));

        spCategory.setAdapter(adapterExpense);


        List<String> subCategoryNames;
        if(listCategoriesExpense.size() != 0)
            subCategoryNames = listCategoriesExpense.get(0).getNamesSubCategories();
        else
            subCategoryNames = new ArrayList<>();

        spSubCategory.setAdapter(new ArrayAdapter<String>(
                NewOperationActivity.this,
                android.R.layout.simple_list_item_1,
                subCategoryNames));

        spCashAccount.setAdapter(
                new ArrayAdapter<String>(NewOperationActivity.this,
                        android.R.layout.simple_list_item_1,
                        listCashAccountNames));

        rgTypeOperation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                List<String> subCategoryNameList;
                switch (checkedId) {
                    case R.id.rb_operation_expense:
                        typeOperation = Category.EXPENSE;
                        spCategory.setAdapter(adapterExpense);

                        if(listCategoriesExpense.size() != 0)
                            subCategoryNameList = listCategoriesExpense.get(0).getNamesSubCategories();
                        else
                            subCategoryNameList = new ArrayList<String>();
                        spSubCategory.setAdapter(new ArrayAdapter<String>(
                                NewOperationActivity.this,
                                android.R.layout.simple_list_item_1,
                                subCategoryNameList));
                        break;
                    case R.id.rb_operation_profit:
                        typeOperation = Category.PROFIT;
                        if(listCategoriesProfit.size() != 0)
                            subCategoryNameList = listCategoriesProfit.get(0).getNamesSubCategories();
                        else
                            subCategoryNameList = new ArrayList<String>();
                        spCategory.setAdapter(adapterProfit);
                        spSubCategory.setAdapter(new ArrayAdapter<String>(
                                NewOperationActivity.this,
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
                    listNames = listCategoriesExpense.get(position).getNamesSubCategories();
                else
                    listNames = listCategoriesProfit.get(position).getNamesSubCategories();
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
            if(bundle.containsKey(Constants.OPEN_AS))
                IS_NEW = bundle.getBoolean(Constants.OPEN_AS);

            Operation operation = Operation.getByID(bundle.getLong(Constants.OPERATION_ID));

            if(operation.getType() == Category.EXPENSE){
                oldTypeOperation = Category.EXPENSE;
                rgTypeOperation.check(R.id.rb_operation_expense);

                for(int i = 0; i < listNamesCategoriesExpense.size(); i++) {
                    if(operation.getCategory().getHierarchy() == Category.PARENT) {
                        if (listNamesCategoriesExpense.get(i).equals(operation.getCategory().getName()))
                            spCategory.setSelection(i);
                    }
                    else {
                        if (listNamesCategoriesExpense.get(i).
                                equals((operation.getCategory()).getParent().getName()))
                            spCategory.setSelection(i);
                    }
                }


            }
            else{
                typeOperation = Category.PROFIT;
                rgTypeOperation.check(R.id.rb_operation_profit);
                for(int i = 0; i < listNamesCategoriesProfit.size(); i++) {
                    if (listNamesCategoriesProfit.get(i).equals(bundle.getString(Constants.CATEGORY_NAME)))
                        spCategory.setSelection(i);
                }
                for(int i = 0; i < listNamesSubCategoriesProfit.size(); i++) {
                    if (listNamesSubCategoriesProfit.get(i).equals(bundle.getString(Constants.SUB_CATEGORY_NAME)))
                        spSubCategory.setSelection(i);
                }
            }

            oldCashAccountName = bundle.getString(Constants.CASH_ACCOUNT_NAME);
            for(int i = 0; i < listCashAccountNames.size(); i++){
                if(listCashAccountNames.get(i).equals(oldCashAccountName))
                    spCashAccount.setSelection(i);
            }

            time = bundle.getLong(Constants.DATE);

            if(bundle.getString(Constants.AMOUNT) != null){
                oldAmount = Float.parseFloat(bundle.getString(Constants.AMOUNT).substring(1));
                etAmount.setText(bundle.getString(Constants.AMOUNT).substring(1));
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
//            Log.d("oper", "сохраняем");
//            Operation operation;
//            if(OPEN_AS)
//                operation = new Operation();
//            else
//                operation = Operation.getByID(time);
//            // Получаем подкатегорию
//            String stringSubCategory = String.valueOf(((((TextView) spSubCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));
//            SubCategory subCategory = typeOperation
//                    ? SubCategory.getExpenseSubCategoryByName(stringSubCategory)
//                    : SubCategory.getProfitSubCategoryByName(stringSubCategory);
//            Log.d("oper", "получаем категорию =" + subCategory);
//
//            // Получаем кошелек
//            String stringCashAccount = String.valueOf(((((TextView) spCashAccount.getSelectedView().findViewById(android.R.id.text1))).getText()));
//            CashAccount cashAccount = CashAccount.getByName(stringCashAccount);
//            cashAccountName = cashAccount.getName();
//            Log.d("oper", "получаем счет =" + cashAccount);
//
//            // Получаем стоимость
//            String amountString = String.valueOf(etAmount.getText());
//            Log.d("oper", "получаем стоимость =" + amountString);
//            float amount;
//            if(amountString.length() == 0)
//                amount = 0;
//            else
//                amount = Float.parseFloat(String.valueOf(amountString));
//
//            // Получаем комментарий
//            String comment = String.valueOf(etComment.getText());
//            Log.d("oper", "получаем стоимость =" + comment);
//            if(comment.length() == 0)
//                comment = "";
//
//            // Сохраняем операцию
//            operation.setType(typeOperation ? Operation.EXPENSE : Operation.PROFIT);
//            operation.setCashAccount(cashAccount);
//            operation.setDate(new Date(System.currentTimeMillis()));
//            operation.setCategory(subCategory);
//            operation.setAmount(amount);
//            operation.setComment(comment);
//
//            if(OPEN_AS){
//                operation.save();
//                cashAccount.setAmount(cashAccount.getAmount() + getReallyAmount(amount, typeOperation));
//                cashAccount.update();
//                Log.d("tag", "New Operation Activity - 'save'");
//            }
//            else {
//                operation.update();
//
//                CashAccount oldCashAccount = CashAccount.getByName(oldCashAccountName);
//
//                Log.d("operation", "oldTypeOperation = " + oldTypeOperation);
//                Log.d("operation", "oldAmountOperation = " + oldAmount);
//
//                Log.d("operation", "oldCashAccount = " + oldCashAccountName);
//                Log.d("operation", "oldAmountCashAccount = " + oldCashAccount.getAmount());
//
//
//                Log.d("operation", "newTypeOperation = " + typeOperation);
//                Log.d("operation", "newAmountOperation = " + amount);
//                Log.d("operation", "newCashAccount = " + cashAccountName);
//                Log.d("operation", "newAmountCashAccount = " + cashAccount.getAmount());
//
//
//                if(isChangedType()){
//                    changeType(oldCashAccount, oldAmount);
//                }
//
//                //Изменили средства
//                if(isChangedAmount(amount)) {
//                    changeAmount(oldCashAccount, oldAmount, amount);
//                    cashAccount.update();
//                }
//
//                //Изменился счет
//                if(isChangedCashAccount()) {
//                    changeCashAccount(oldCashAccount, cashAccount, oldAmount);
//                }
//
//                cashAccount.update();
//                oldCashAccount.update();
//            }
//
//            NavUtils.navigateUpFromSameTask(this);
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private boolean isChangedCashAccount(){
//        return !oldCashAccountName.equals(cashAccountName);
//    }
//
//    private void changeCashAccount(CashAccount oldCashAccount,
//            CashAccount newCashAccount, float oldAmount){
//        float amountReally = getReallyAmount(oldAmount, oldTypeOperation);
//        oldCashAccount.setAmount(oldCashAccount.getAmount() - amountReally);
//        newCashAccount.setAmount(newCashAccount.getAmount() + amountReally);
//        Log.d("operation", "after changeCashAccount --> oldCashAccountAmount = " + oldCashAccount.getAmount());
//        Log.d("operation", "after changeCashAccount --> newCashAccountAmount = " + newCashAccount.getAmount());
//    }
//
//    private void changeAmount(CashAccount cashAccount, float oldAmount, float newAmount){
//        float oldAmountReally = getReallyAmount(oldAmount, oldTypeOperation);
//        float newAmountReally = getReallyAmount(newAmount, typeOperation);
//        cashAccount.setAmount(cashAccount.getAmount() - oldAmountReally + newAmountReally);
//        Log.d("operation", "after changeAmount --> newCashAmount = " + cashAccount.getAmount());
//    }
//
//    private void changeType (CashAccount cashAccount, float oldAmount){
//        float oldAmountReally = getReallyAmount(oldAmount, oldTypeOperation);
//        cashAccount.setAmount(cashAccount.getAmount() - oldAmountReally * 2);
//        Log.d("operation", "after changeType --> newCashAmount = " + cashAccount.getAmount());
//    }
//
//    private float getReallyAmount (float amount, boolean typeOperation){
//        return typeOperation ? amount* -1 : amount;
//    }
//
//    private boolean isChangedType(){
//        return oldTypeOperation != typeOperation;
//    }
//
//    private boolean isChangedAmount(float newAmount){
//        return oldAmount != newAmount;
//    }
}
