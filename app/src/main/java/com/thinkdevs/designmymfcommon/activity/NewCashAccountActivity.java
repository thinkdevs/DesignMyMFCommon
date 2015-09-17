package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.ListColorAdapter;
import com.thinkdevs.designmymfcommon.adapter.ListCurrencyAdapter;
import com.thinkdevs.designmymfcommon.adapter.ListLogosCashAccountSpinnerAdapter;
import com.thinkdevs.designmymfcommon.database.Cash;
import com.thinkdevs.designmymfcommon.database.Cash$Table;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Color$Table;
import com.thinkdevs.designmymfcommon.database.Currency;
import com.thinkdevs.designmymfcommon.database.Currency$Table;
import com.thinkdevs.designmymfcommon.database.LogoCash;
import com.thinkdevs.designmymfcommon.database.LogoCash$Table;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.List;


public class NewCashAccountActivity extends Activity {

    private boolean FLAG_NEW = true;

    EditText etTitle;
    EditText etType;
    EditText etAmount;
    Spinner  spLogo;
    Spinner  spCurrency;
    Spinner  spColor;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cash_account);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            setTitle(extras.getString(NamesOfParametrs.NEW_CASH_ACTIVITY_TITLE));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        etTitle    = (EditText)findViewById(R.id.et_title);
        etType     = (EditText)findViewById(R.id.et_type);
        etAmount   = (EditText)findViewById(R.id.et_amount);
        spLogo     = (Spinner)findViewById(R.id.sp_logos);
        spColor    = (Spinner)findViewById(R.id.sp_colors);
        spCurrency = (Spinner)findViewById(R.id.sp_currency);

        List<LogoCash> logosCashAccountList = new Select().from(LogoCash.class).queryList();
        List<Color> colorList = new Select().from(Color.class).queryList();
        List<Currency> currencyList = new Select().from(Currency.class).queryList();

        ListLogosCashAccountSpinnerAdapter logosAdapter =
                new ListLogosCashAccountSpinnerAdapter(this, logosCashAccountList);
        ListCurrencyAdapter currencyAdapter =
                new ListCurrencyAdapter(this, currencyList);
        ListColorAdapter colorAdapter =
                new ListColorAdapter(this, colorList);

        spLogo.    setAdapter(logosAdapter);
        spColor.   setAdapter(colorAdapter);
        spCurrency.setAdapter(currencyAdapter);

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            FLAG_NEW = false;
            etTitle. setText(bundle.getString(NamesOfParametrs.CASH_TITLE));
            etType.  setText(bundle.getString(NamesOfParametrs.CASH_TYPE));
            etAmount.setText(bundle.getString(NamesOfParametrs.CASH_AMOUNT));
            int logoId  = bundle.getInt(NamesOfParametrs.CASH_LOGO);
            int colorId = bundle.getInt(NamesOfParametrs.CASH_COLOR);
            String currencyShortHand = bundle.getString(NamesOfParametrs.CASH_CURRENCY_SHORT_HAND);

            for(int i = 0; i < logosCashAccountList.size(); i++){
                if(logosCashAccountList.get(i).getResourceId() == logoId)
                    spLogo.setSelection(i);
            }

            for(int i = 0; i < colorList.size(); i++){
                if(colorList.get(i).getResourceId() == colorId)
                    spColor.setSelection(i);
            }

            for(int i = 0; i < currencyList.size(); i++){
                if(currencyList.get(i).getShortHand().equals(currencyShortHand))
                    spCurrency.setSelection(i);
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

        //**************************** Сохранение счета *****************************//

        if(id == R.id.action_save){

            // Получение Logo
            int logoId = (int) ((ImageView)(spLogo.getSelectedView().findViewById(R.id.imageView))).getTag();
            LogoCash logoCash = new Select().from(LogoCash.class).where(Condition.column(LogoCash$Table.RESOURCEID).eq(logoId)).querySingle();

            // Получение Color
            int colorId = ((int) ((TextView) spColor.getSelectedView().findViewById(R.id.tv_color)).getTag());
            Color color = new Select().from(Color.class).where(Condition.column(Color$Table.RESOURCEID).eq(colorId)).querySingle();

            // Получение Currency
            String currencyString = String.valueOf(((TextView) spCurrency.getSelectedView().findViewById(android.R.id.text1)).getText());
            Currency currency = new Select().from(Currency.class).where(Condition.column(Currency$Table.SHORTHAND).eq(currencyString)).querySingle();

            // Получение Названия
            String title = String.valueOf(etTitle.getText());

            // Получение Типа
            String type = String.valueOf(etType.getText());

            // Получение Средств
            String amountString = String.valueOf(etAmount.getText());
            float amount;
            if(amountString.length() == 0)
                amount = 0;
            else
             amount = Float.parseFloat(String.valueOf(etAmount.getText()));

            // Проверка условий и сохранение
            if(title == null || title.length() == 0){
                Toast.makeText(this, "Введите название", Toast.LENGTH_LONG).show();
            }
            else {
                Cash cash;
                if(FLAG_NEW)
                    cash = new Cash();
                else
                    cash = new Select()
                            .from(Cash.class)
                            .where(Condition.column(Cash$Table.NAME).is(title))
                            .querySingle();
                cash.setLogo(logoCash);
                cash.setColor(color);
                cash.setCurrency(currency);
                cash.setName(title);
                if(type == null)
                    cash.setType("");
                else
                    cash.setType(type);
                cash.setAmount(amount);

                if(FLAG_NEW)
                    cash.save();
                else
                    cash.update();

                NavUtils.navigateUpFromSameTask(this);
            }

            // Возвращаемся назад после сохранения
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
