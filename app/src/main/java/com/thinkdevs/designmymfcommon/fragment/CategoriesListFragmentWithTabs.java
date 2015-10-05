package com.thinkdevs.designmymfcommon.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.MainNavigationDrawerActivity;
import com.thinkdevs.designmymfcommon.adapter.RecyclerViewParentCategoriesAdapter;
import com.thinkdevs.designmymfcommon.database.Category;

import java.util.List;

public class CategoriesListFragmentWithTabs extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAB_TAG_EXPENSE    = "expense_categories";
    private static final String TAB_TAG_PROFIT     = "profit_categories";

    private RecyclerView mRvExpense;
    private RecyclerView mRvProfit;
    private RecyclerView.Adapter mAdapterExpense;
    private RecyclerView.Adapter mAdapterProfit;

    private RecyclerView.LayoutManager mLayoutManagerExpense;
    private RecyclerView.LayoutManager mLayoutManagerProfit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "Operation Templates List Fragment - 'onCreate' savedInstance = " + savedInstanceState);
        super.onCreate(savedInstanceState);

        // Включаем отображение меню
        setHasOptionsMenu(true);
    }

    public static CategoriesListFragmentWithTabs newInstance(int sectionNumber) {
        Log.d("tag", "Operation Templates List Fragment - 'newInstance'");
        CategoriesListFragmentWithTabs fragment = new CategoriesListFragmentWithTabs();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_with_tabs, container, false);

        TabHost tabHost = ((TabHost) view.findViewById(R.id.tabHost));
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec(TAB_TAG_EXPENSE);
        tabSpec.setIndicator(getResources().getString(R.string.operation_expense));
        tabSpec.setContent(R.id.rv_expense);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(TAB_TAG_PROFIT);
        tabSpec.setIndicator(getResources().getString(R.string.operation_profit));
        tabSpec.setContent(R.id.rv_profit);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag(TAB_TAG_EXPENSE);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });

        mRvExpense = (RecyclerView) view.findViewById(R.id.rv_expense);
        mRvProfit = (RecyclerView) view.findViewById(R.id.rv_profit);

        mRvExpense.setHasFixedSize(true);
        mRvProfit.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerExpense = new LinearLayoutManager(CategoriesListFragmentWithTabs.this.getActivity());
        mLayoutManagerProfit = new LinearLayoutManager(CategoriesListFragmentWithTabs.this.getActivity());
        mRvExpense.setLayoutManager(mLayoutManagerExpense);
        mRvProfit.setLayoutManager(mLayoutManagerProfit);

        List<Category> expenseCategories
                = Category.getParentCategoriesWithoutEmpty(Category.EXPENSE);
        List<Category> profitCategories
                = Category.getParentCategoriesWithoutEmpty(Category.PROFIT);

        mAdapterExpense =
                new RecyclerViewParentCategoriesAdapter(CategoriesListFragmentWithTabs.this.getActivity(), expenseCategories);
        mAdapterProfit =
                new RecyclerViewParentCategoriesAdapter(CategoriesListFragmentWithTabs.this.getActivity(), profitCategories);

        mRvExpense.setAdapter(mAdapterExpense);
        mRvProfit.setAdapter(mAdapterProfit);

//        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
//                intent.putExtra(Constants.OPEN_AS, Category.CREATE_PARENT);
//                intent.putExtra(Constants.ACTIVITY_TITLE, R.string.title_activity_new_category);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("tag", "Operation Templates List Fragment - 'onAttach'");
        super.onAttach(activity);
        ((MainNavigationDrawerActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
