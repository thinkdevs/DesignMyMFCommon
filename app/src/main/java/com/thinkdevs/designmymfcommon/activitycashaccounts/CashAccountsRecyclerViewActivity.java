package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.mock.CashAccount;

import java.util.List;

public class CashAccountsRecyclerViewActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_accounts_recycler_view_activity);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_cash_accounts);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

       List<CashAccount> accounts = Initializator.initializeCashAccounts();

        // specify an adapter (see also next example)
        mAdapter = new CashAccountsRecyclerViewAdapter(CashAccountsRecyclerViewActivity.this, accounts);
        mRecyclerView.setAdapter(mAdapter);
    }
}
