package com.thinkdevs.designmymfcommon.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.MainActivityNavigationDrawer;
import com.thinkdevs.designmymfcommon.activity.NewCashAccountActivity;
import com.thinkdevs.designmymfcommon.activitycashaccounts.CashAccountsRecyclerViewAdapter;
import com.thinkdevs.designmymfcommon.database.Cash;

import java.util.List;

public class CashAccountsListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Включаем отображение меню
        setHasOptionsMenu(true);

    }

    public static CashAccountsListFragment newInstance(int sectionNumber) {
        CashAccountsListFragment fragment = new CashAccountsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivityNavigationDrawer) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_accounts_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_cash_accounts);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(CashAccountsListFragment.this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Cash> cashes = new Select().from(Cash.class).queryList();

        // specify an adapter (see also next example)
        mAdapter = new CashAccountsRecyclerViewAdapter(CashAccountsListFragment.this.getActivity(), cashes);
        mRecyclerView.setAdapter(mAdapter);

//        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), NewCashAccountActivity.class));
//            }
//        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.global, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_cash) {
            startActivity(new Intent(getActivity(), NewCashAccountActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
