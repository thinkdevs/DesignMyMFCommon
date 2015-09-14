package com.thinkdevs.designmymfcommon.activitymain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.MainActivityNavigationDrawer;

public class MainActivity extends Activity implements View.OnClickListener {


    Button buttonCashAccout;
    Button buttonEmpty2;
    Button buttonEmpty3;
    Button buttonEmpty4;
    Button buttonEmpty5;
    Button buttonEmpty6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCashAccout = (Button) findViewById(R.id.button_cash_account);
        buttonEmpty2 = ((Button) findViewById(R.id.button2_empty));
        buttonEmpty3 = ((Button) findViewById(R.id.button3_empty));
        buttonEmpty4 = ((Button) findViewById(R.id.button4_empty));
        buttonEmpty5 = ((Button) findViewById(R.id.button5_empty));
        buttonEmpty6 = ((Button) findViewById(R.id.button6_empty));

        buttonCashAccout.setOnClickListener(this);
        buttonEmpty2.setOnClickListener(this);
        buttonEmpty3.setOnClickListener(this);
        buttonEmpty4.setOnClickListener(this);
        buttonEmpty5.setOnClickListener(this);
        buttonEmpty6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.button_cash_account:
                startActivity(new Intent(MainActivity.this, MainActivityNavigationDrawer.class));
                break;
            case R.id.button2_empty:
//                startActivity(new Intent());
                break;
            case R.id.button3_empty:
//                startActivity(new Intent());
                break;
            case R.id.button4_empty:
//                startActivity(new Intent());
                break;
            case R.id.button5_empty:
//                startActivity(new Intent());
                break;
            case R.id.button6_empty:
//                startActivity(new Intent());
                break;
        }
    }
}
