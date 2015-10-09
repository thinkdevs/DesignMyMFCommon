package com.thinkdevs.designmymfcommon.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.ListCurrencyAdapter;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Currency;
import com.thinkdevs.designmymfcommon.database.Logo;
import com.thinkdevs.designmymfcommon.dialog.ChooseDecorColorDialogFragment;
import com.thinkdevs.designmymfcommon.dialog.ChooseDecorIconDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;


public class NewCashAccountActivity extends AppCompatActivity
        implements View.OnClickListener, ChooseDecorColorDialogFragment.ChooseColorDialogListener,
                   ChooseDecorIconDialogFragment.ChooseIconDialogListener {

    private boolean IS_NEW = true;
    private long mCurrentIconId;
    private long mCurrentColorId;

    EditText etTitle;
    EditText etComment;
    EditText etAmount;
    Spinner  spCurrency;
    Spinner  spUnits;

    ImageView ivColor;
    ImageView ivIcon;
    
    Intent intent;
    Bundle bundle;
    String oldCashAccountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cash_account_new);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            setTitle(extras.getString(Constants.ACTIVITY_TITLE));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Новый счет");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        setupFloatingLabelError();

        etTitle    = (EditText)findViewById(R.id.et_title);
        etComment  = (EditText)findViewById(R.id.et_type);
        etAmount   = (EditText)findViewById(R.id.et_amount);
        spCurrency = (Spinner)findViewById(R.id.sp_currency);
        spUnits    = (Spinner)findViewById(R.id.sp_units);
        ivColor    = (ImageView)findViewById(R.id.ivColor);
        ivIcon     = (ImageView)findViewById(R.id.ivIcon);
        ivIcon.setColorFilter(getResources().getColor(R.color.grey));

        ivColor.setOnClickListener(this);
        ivIcon.setOnClickListener(this);

        List<Logo> logosCashAccountList = Logo.getAllCashAccountLogos();
        Logo logoDefault = logosCashAccountList.get(0);
        ivIcon.setImageResource(logoDefault.getResourceId());
        mCurrentIconId = logoDefault.getId();

        List<Color> colorList = Color.getColorsWithoutSystems();
        Color colorDefault = colorList.get(0);
        ivColor.setColorFilter(getResources().getColor(colorDefault.getResourceId()));
        mCurrentColorId = colorDefault.getId();

        List<Currency> currencyList = new Select().from(Currency.class).queryList();
        
        ListCurrencyAdapter currencyAdapter =
                new ListCurrencyAdapter(this, currencyList);
        
        String [] unitsList = getResources().getStringArray(R.array.currency_units);

        spUnits.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, unitsList));
        
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
            mCurrentIconId  = cashAccount.getLogo().getId();
            mCurrentColorId = cashAccount.getColor().getId();
            long currencyId = cashAccount.getCurrency().getId();

            for(int i = 0; i < logosCashAccountList.size(); i++){
                Logo logo = logosCashAccountList.get(i);
                if(logo.getId() == mCurrentIconId){
                    ivIcon.setImageResource(logo.getResourceId());
                }
            }

            for(int i = 0; i < colorList.size(); i++){
                Color color = colorList.get(i);
                if(color.getId() == mCurrentColorId) {
                    ivColor.setColorFilter(getResources().getColor(color.getResourceId()));
                }
            }

            for(int i = 0; i < currencyList.size(); i++){
                if(currencyList.get(i).getId() == (currencyId))
                    spCurrency.setSelection(i);
            }
        }

        requestFocus(etAmount);
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

            // Получение Logo
            Logo cashAccountLogo = Logo.getById(mCurrentIconId);

            // Получение Color
            Color color = Color.getById(mCurrentColorId);

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
                requestFocus(etTitle);
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

    private void setupFloatingLabelError() {
        final TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.tilName);
        floatingUsernameLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() > 10) {
                    floatingUsernameLabel.setError("Рекомендуемая длинна 10 символов");
                    floatingUsernameLabel.setErrorEnabled(true);
                } else {
                    floatingUsernameLabel.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onChooseColor(long colorId) {
        Color color = Color.getById(colorId);
        ivColor.setColorFilter(getResources().getColor(color.getResourceId()));
        mCurrentColorId = colorId;
    }

    @Override
    public void onChooseIcon(long iconId) {
        Logo logo = Logo.getById(iconId);
        ivIcon.setImageResource(logo.getResourceId());
        mCurrentIconId = iconId;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivColor:
                ChooseDecorColorDialogFragment.newInstance(this, mCurrentColorId).show(getFragmentManager(), "chooseDecorColor");
                break;
            case R.id.ivIcon:
                ChooseDecorIconDialogFragment.newInstance(this, mCurrentIconId).show(getFragmentManager(), "chooseDecorIcon");
                break;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
