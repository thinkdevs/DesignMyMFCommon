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
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Cash;
import com.thinkdevs.designmymfcommon.database.Cash$Table;
import com.thinkdevs.designmymfcommon.database.ExpenseFavorite;
import com.thinkdevs.designmymfcommon.database.ExpenseFavorite$Table;
import com.thinkdevs.designmymfcommon.database.OperationFavorite;
import com.thinkdevs.designmymfcommon.database.ProfitFavorite;
import com.thinkdevs.designmymfcommon.database.ProfitFavorite$Table;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.database.SubCategoryExpense;
import com.thinkdevs.designmymfcommon.database.SubCategoryExpense$Table;
import com.thinkdevs.designmymfcommon.database.SubCategoryProfit;
import com.thinkdevs.designmymfcommon.database.SubCategoryProfit$Table;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.ArrayList;
import java.util.List;


public class NewFavoriteOperationActivity extends Activity {

    private boolean FLAG_NEW = true;

    boolean typeOperation; // if TRUE then Expensive

    EditText etTitle;
    RadioGroup radioGroupType;
    Spinner spSubCategory;
    EditText etAmount;

    List<SubCategoryExpense> listSubCategoryExpense;
    List<SubCategoryProfit> listSubCategoryProfits;

    List<String> listNamesSubCategoriesExpense; // Для адаптера
    List<String> listNamesSubCategoriesProfit; // Для адаптера

    ArrayAdapter<String> adapterExpense; // Адаптер расхода
    ArrayAdapter<String> adapterProfit;  // Адаптер дохода

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_favorite_operation);

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

        listSubCategoryExpense = new Select().from(SubCategoryExpense.class).queryList();
        listNamesSubCategoriesExpense = new ArrayList<>();
        if(listSubCategoryExpense.size() != 0){
            for(SubCategoryExpense subCategoryExpense : listSubCategoryExpense){
                listNamesSubCategoriesExpense.add(subCategoryExpense.getName());
            }
        }

        listSubCategoryProfits = new Select().from(SubCategoryProfit.class).queryList();
        listNamesSubCategoriesProfit = new ArrayList<>();
        if(listSubCategoryProfits.size() != 0){
            for(SubCategoryProfit subCategoryProfit : listSubCategoryProfits){
                listNamesSubCategoriesProfit.add(subCategoryProfit.getName());
            }
        }

        adapterExpense = new ArrayAdapter<String>(
                NewFavoriteOperationActivity.this,
                android.R.layout.simple_list_item_1,
                listNamesSubCategoriesExpense);

        adapterProfit = new ArrayAdapter<String>(
                NewFavoriteOperationActivity.this,
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

        Bundle bundle = intent.getExtras();
        if(bundle != null){
            FLAG_NEW = false;
            etTitle.        setText(bundle.getString(NamesOfParametrs.CASH_TITLE));
            typeOperation = bundle.getBoolean(NamesOfParametrs.TYPE_OPERATION);
            if(typeOperation)
                radioGroupType.check(R.id.rb_expense);
            else
                radioGroupType. check(R.id.rb_profit);
            etAmount.       setText(bundle.getString(NamesOfParametrs.AMOUNT));

            String categoryName = bundle.getString(NamesOfParametrs.NAME_CATEGORY);

            if(typeOperation){
                for(int i = 0; i < listSubCategoryExpense.size(); i++){
                    if(listSubCategoryExpense.get(i).getName() == categoryName)
                        spSubCategory.setSelection(i);
                }
            }
            else {
                for(int i = 0; i < listSubCategoryProfits.size(); i++){
                    if(listSubCategoryProfits.get(i).getName() == categoryName)
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
        // as you specify a parent activity in AndroidManifest.xml.
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

            // Получаем категорию
            String stringSubCategory = String.valueOf(((((TextView) spSubCategory.getSelectedView().findViewById(android.R.id.text1))).getText()));
            SubCategory subCategory;
            if(typeOperation)
                subCategory = new Select()
                        .from(SubCategoryExpense.class)
                        .where(Condition.column(SubCategoryExpense$Table.NAME)
                                .eq(stringSubCategory))
                        .querySingle();
            else
                subCategory = new Select()
                        .from(SubCategoryProfit.class)
                        .where(Condition.column(SubCategoryProfit$Table.NAME)
                                .eq(stringSubCategory))
                        .querySingle();

            // Получаем сумму
            String amountString = String.valueOf(etAmount.getText());

            // Проверка условий и сохранение
            if(title == null || title.length() == 0){
                Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
            }
            else if (amountString == null || amountString.length() == 0){
                Toast.makeText(this, "Введите сумму", Toast.LENGTH_LONG).show();
            }
            else if(new Select()
                    .from(Cash.class)
                    .where(Condition.column(Cash$Table.NAME).eq(title))
                    .querySingle() != null){
                Toast.makeText(this, "Шаблон с таким именем уже существует", Toast.LENGTH_LONG).show();
            }
            else {
                OperationFavorite operationFavorite;
                if(FLAG_NEW)
                    if(typeOperation)
                        operationFavorite = new ExpenseFavorite();
                    else
                        operationFavorite = new ProfitFavorite();
                else
                    if(typeOperation)
                        operationFavorite = new Select()
                                .from(ExpenseFavorite.class)
                                .where(Condition.column(ExpenseFavorite$Table.NAME).is(title))
                                .querySingle();
                    else
                        operationFavorite = new Select()
                                        .from(ExpenseFavorite.class)
                                        .where(Condition.column(ProfitFavorite$Table.NAME).is(title))
                                        .querySingle();

                operationFavorite.setTitle(title);

                float amount;
                if(amountString.length() == 0)
                    amount = 0;
                else
                    amount = Float.parseFloat(String.valueOf(amountString));
                operationFavorite.setAmount(amount);

                operationFavorite.setSubCategory(subCategory);

                if(FLAG_NEW)
                    operationFavorite.save();
                else
                    operationFavorite.update();

                NavUtils.navigateUpFromSameTask(this);

                }

            // Возвращаемся назад после сохранения
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
