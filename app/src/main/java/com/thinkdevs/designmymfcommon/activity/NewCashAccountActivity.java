package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.ListColorAdapter;
import com.thinkdevs.designmymfcommon.adapter.ListCurrencyAdapter;
import com.thinkdevs.designmymfcommon.adapter.ListLogosCashAccountSpinnerAdapter;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Currency;
import com.thinkdevs.designmymfcommon.database.Logo;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;


public class NewCashAccountActivity extends Activity {

    private boolean IS_NEW = true;

    EditText etTitle;
    EditText etComment;
    EditText etAmount;
    Spinner  spLogo;
    Spinner  spCurrency;
    Spinner  spColor;

    Intent intent;
    Bundle bundle;
    String oldCashAccountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cash_account);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            setTitle(extras.getString(Constants.ACTIVITY_TITLE));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        etTitle    = (EditText)findViewById(R.id.et_title);
        etComment  = (EditText)findViewById(R.id.et_type);
        etAmount   = (EditText)findViewById(R.id.et_amount);
        spLogo     = (Spinner)findViewById(R.id.sp_logos);
        spColor    = (Spinner)findViewById(R.id.sp_colors);
        spCurrency = (Spinner)findViewById(R.id.sp_currency);

        List<Logo> logosCashAccountList = Logo.getAllCashAccountLogos();
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
        bundle = intent.getExtras();
        if(bundle != null){
            IS_NEW = false;
            CashAccount cashAccount = CashAccount.getByID(bundle.getLong(Constants.CASH_ACCOUNT_ID));
            etTitle. setText(cashAccount.getName());
            oldCashAccountName = cashAccount.getName();
            etComment.  setText(cashAccount.getComment());
            etAmount.setText(String.valueOf(cashAccount.getAmount()));
            long logoId  = cashAccount.getLogo().getId();
            long colorId = bundle.getInt(Constants.COLOR);
            long currencyId = cashAccount.getCurrency().getId();

            for(int i = 0; i < logosCashAccountList.size(); i++){
                if(logosCashAccountList.get(i).getId() == logoId)
                    spLogo.setSelection(i);
            }

            for(int i = 0; i < colorList.size(); i++){
                if(colorList.get(i).getId() == colorId)
                    spColor.setSelection(i);
            }

            for(int i = 0; i < currencyList.size(); i++){
                if(currencyList.get(i).getId() == (currencyId))
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

        //**************************** Сохранение счета *****************************//

        if(id == R.id.action_save){

            // Получение LogoInterface
            int logoId = (int) spLogo.getSelectedView().findViewById(R.id.imageView).getTag();
            Logo cashAccountLogo = Logo.getLogoByResourceId(logoId);

            // Получение Color
            int colorId = (int) spColor.getSelectedView().findViewById(R.id.tv_color).getTag();
            Color color = Color.getColorByResourceId(colorId);

            // Получение Currency
            String strSymbol = String.valueOf(((TextView) spCurrency.getSelectedView().findViewById(android.R.id.text1)).getText());
            Currency currency = Currency.getCurrencyByStrSymbol(strSymbol);

            // Получение Названия
            String title = String.valueOf(etTitle.getText());

            // Получение комментария
            String comment = String.valueOf(etComment.getText());

            // Получение Средств
            String amountString = String.valueOf(etAmount.getText());
            float amount;
            if(amountString.length() == 0)
                amount = 0;
            else
             amount = Float.parseFloat(String.valueOf(etAmount.getText()));

            // Проверка условий и сохранение
            if(title == null || title.length() == 0){
                Toast.makeText(this, getResources().getString(R.string.msg_write_name), Toast.LENGTH_LONG).show();
            }
            else if(CashAccount.isExist(title) && !title.equals(oldCashAccountName)){
                Toast.makeText(this, getResources().getString(R.string.msg_cash_account_exist), Toast.LENGTH_LONG).show();
            }
            else {
                CashAccount cashAccount;
                if(IS_NEW)
                    cashAccount = new CashAccount();
                else
                    cashAccount = CashAccount.getByID(bundle.getLong(Constants.CASH_ACCOUNT_ID));
                cashAccount.setLogo(cashAccountLogo);
                cashAccount.setColor(color);
                cashAccount.setCurrency(currency);
                cashAccount.setName(title);
                if(comment == null)
                    cashAccount.setComment("");
                else
                    cashAccount.setComment(comment);
                cashAccount.setAmount(amount);

                if(IS_NEW)
                    cashAccount.save();
                else
                    cashAccount.update();
                NavUtils.navigateUpFromSameTask(this);
            }

            // Возвращаемся назад после сохранения
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
