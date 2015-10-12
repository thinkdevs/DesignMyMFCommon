package com.thinkdevs.designmymfcommon.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.ListCurrencyAdapter;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Currency;
import com.thinkdevs.designmymfcommon.database.Icon;
import com.thinkdevs.designmymfcommon.dialog.ChooseDecorColorDialogFragment;
import com.thinkdevs.designmymfcommon.dialog.ChooseDecorIconDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewCashAccountActivity extends AppCompatActivity
        implements View.OnClickListener, ChooseDecorColorDialogFragment.ChooseColorDialogListener,
                   ChooseDecorIconDialogFragment.ChooseIconDialogListener {

    private EditText  mEtName;
    private EditText  mEtDescription;
    private EditText  mEtAmount;
    private Spinner   mSpCurrency;
    private ImageView mIvColor;
    private ImageView mIvIcon;
    
    private Intent mIntent;
    private Bundle mBundle;
    private String oldCashAccountName;

    private int  mOpenAs;
    private long mCurrentIconId;
    private long mCurrentColorId;

    private List<Color> mColors;
    private List<Icon> mIcons;
    private List<Currency> mCurrencies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cash_account);

        mIntent = getIntent();
        mBundle = mIntent.getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mBundle != null) {
            mOpenAs = mBundle.getInt(Constants.OPEN_AS);
            toolbar.setTitle(mBundle.getString(Constants.ACTIVITY_TITLE));
        }
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mEtName = (EditText)findViewById(R.id.et_title);
        mEtDescription = (EditText)findViewById(R.id.et_description);
        mEtAmount = (EditText)findViewById(R.id.et_amount);
        mSpCurrency = (Spinner)findViewById(R.id.sp_currency);
        mIvColor = (ImageView)findViewById(R.id.ivColor);
        mIvIcon = (ImageView)findViewById(R.id.ivIcon);
        mIvIcon.setColorFilter(getResources().getColor(R.color.grey));

        mIvColor.setOnClickListener(this);
        mIvIcon.setOnClickListener(this);

        mIcons = Icon.getCashAccountIcons();
        Icon defaultIcon = mIcons.get(0);
        mCurrentIconId   = defaultIcon.getId();
        mIvIcon.setImageResource(defaultIcon.getResourceId());

        mColors = Color.getColorsWithoutSystems();
        Color defaultColor = mColors.get(0);
        mCurrentColorId    = defaultColor.getId();
        mIvColor.setColorFilter(getResources().getColor(defaultColor.getResourceId()));

        mCurrencies = new Select().from(Currency.class).queryList();
        ListCurrencyAdapter currencyAdapter = new ListCurrencyAdapter(this, mCurrencies);
        mSpCurrency.setAdapter(currencyAdapter);

        setupFloatingLabelError();

        mEtAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(12, 2)});

        requestFocus(findViewById(R.id.ll_new_cash_account));

        mOpenAs();
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

        if(id == R.id.action_save){
            mSave();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onChooseColor(long colorId) {
        Color color = Color.getById(colorId);
        mIvColor.setColorFilter(getResources().getColor(color.getResourceId()));
        mCurrentColorId = colorId;
    }

    @Override
    public void onChooseIcon(long iconId) {
        Icon icon = Icon.getById(iconId);
        mIvIcon.setImageResource(icon.getResourceId());
        mCurrentIconId = iconId;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivColor:
                ChooseDecorColorDialogFragment.newInstance(this, mCurrentColorId)
                        .show(getFragmentManager(), "chooseDecorColor");
                break;
            case R.id.ivIcon:
                ChooseDecorIconDialogFragment.newInstance(this, mCurrentIconId)
                        .show(getFragmentManager(), "chooseDecorIcon");
                break;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void mOpenAs(){
        switch (mOpenAs){
            case CashAccount.CREATE:
                mOpenCreator();
                break;
            case CashAccount.EDIT:
                mOpenEditor();
                break;
        }
    }

    private void mOpenCreator(){
    }

    private void mOpenEditor(){
        CashAccount cashAccount = CashAccount.getByID(mBundle.getLong(Constants.CASH_ACCOUNT_ID));
        mEtName. setText(cashAccount.getName());
        oldCashAccountName = cashAccount.getName();
        mEtDescription.  setText(cashAccount.getComment());
        mEtAmount.setText(String.valueOf(cashAccount.getAmount()));
        mCurrentIconId  = cashAccount.getIcon().getId();
        mCurrentColorId = cashAccount.getColor().getId();
        long currencyId = cashAccount.getCurrency().getId();

        for(int i = 0; i < mIcons.size(); i++){
            Icon icon = mIcons.get(i);
            if(icon.getId() == mCurrentIconId){
                mIvIcon.setImageResource(icon.getResourceId());
            }
        }

        for(int i = 0; i < mColors.size(); i++){
            Color color = mColors.get(i);
            if(color.getId() == mCurrentColorId) {
                mIvColor.setColorFilter(getResources().getColor(color.getResourceId()));
            }
        }

        for(int i = 0; i < mCurrencies.size(); i++){
            if(mCurrencies.get(i).getId() == (currencyId))
                mSpCurrency.setSelection(i);
        }
    }

    private void mSave(){

        String name = String.valueOf(mEtName.getText());
        String description = String.valueOf(mEtDescription.getText());

        long currencyId =
                (long) mSpCurrency.getSelectedView().findViewById(android.R.id.text1)
                        .getTag(R.string.tag_currency_id);
        Currency currency = Currency.getById(currencyId);

        Icon  icon = Icon.getById(mCurrentIconId);
        Color color = Color.getById(mCurrentColorId);

        String amountString = String.valueOf(mEtAmount.getText());
        float amountFloat;
        if(amountString.length() == 0)
            amountFloat = 0;
        else
            amountFloat = Float.parseFloat(String.valueOf(mEtAmount.getText()));

        long amount =(long)amountFloat * 100;

        if(name == null || name.length() == 0){
            Toast.makeText(this, getResources().getString(R.string.msg_write_name), Toast.LENGTH_LONG).show();
            requestFocus(mEtName);
        }
        else if(CashAccount.isExist(name) && !name.equals(oldCashAccountName)){
            Toast.makeText(this, getResources().getString(R.string.msg_cash_account_exist), Toast.LENGTH_LONG).show();
        }
        else {
            CashAccount cashAccount;
            if(mOpenAsCreator())
                cashAccount = new CashAccount();
            else
                cashAccount = CashAccount.getByID(mBundle.getLong(Constants.CASH_ACCOUNT_ID));
            cashAccount.setIcon(icon);
            cashAccount.setColor(color);
            cashAccount.setCurrency(currency);
            cashAccount.setName(name);
            if(description == null)
                cashAccount.setDescription("");
            else
                cashAccount.setDescription(description);

            cashAccount.setAmount(amount);

            if(mOpenAsCreator())
                cashAccount.save();
            else
                cashAccount.update();
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    private boolean mOpenAsCreator(){
        return mOpenAs == CashAccount.CREATE;
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


    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern= Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
}
