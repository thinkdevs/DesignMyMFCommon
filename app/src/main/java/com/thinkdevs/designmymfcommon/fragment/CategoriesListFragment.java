package com.thinkdevs.designmymfcommon.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.melnykov.fab.FloatingActionButton;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.MainNavigationDrawerActivity;
import com.thinkdevs.designmymfcommon.activity.NewCashAccountActivity;
import com.thinkdevs.designmymfcommon.activity.NewCategoryActivity;
import com.thinkdevs.designmymfcommon.adapter.RecyclerViewParentCategoriesAdapter;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class CategoriesListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mType = Category.EXPENSE;

    private RadioGroup mRgType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "Operation Templates List Fragment - 'onCreate' savedInstance = " + savedInstanceState);
        super.onCreate(savedInstanceState);

        // Включаем отображение меню
        setHasOptionsMenu(true);
    }

    public static CategoriesListFragment newInstance(int sectionNumber) {
        Log.d("tag", "Operation Templates List Fragment - 'newInstance'");
        CategoriesListFragment fragment = new CategoriesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("tag", "Operation Templates List Fragment - 'onAttach'");
        super.onAttach(activity);
        ((MainNavigationDrawerActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view_categories_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(CategoriesListFragment.this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Category> categoriesList = Category.getExpenseParentCategories();
        mAdapter = new RecyclerViewParentCategoriesAdapter(CategoriesListFragment.this.getActivity(), categoriesList);

        mRgType = ((RadioGroup) view.findViewById(R.id.rg_type));
        if(mRgType.getCheckedRadioButtonId() == R.id.rb_operation_expense)
            mType = Category.EXPENSE;
        else
            mType = Category.PROFIT;

        mRecyclerView.setAdapter(mAdapter);

        mRgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rb_type_expense:
                        mType = Category.EXPENSE;
                        mAdapter = getAdapter(mType);
                        mRecyclerView.setAdapter(mAdapter);
                        Log.d("categories", "expense");
                        break;
                    case R.id.rb_type_profit:
                        mType = Category.PROFIT;
                        mAdapter = getAdapter(mType);
                        mRecyclerView.setAdapter(mAdapter);
                        Log.d("categories", "profit");
                        break;
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
                intent.putExtra(Constants.OPEN_AS, Category.CREATE_PARENT);
                intent.putExtra(Constants.ACTIVITY_TITLE, R.string.title_activity_new_category);
                startActivity(intent);
            }
        });

        return view;
    }

    private RecyclerViewParentCategoriesAdapter getAdapter(int typeCategory) {
        List<Category> categories = Category.getParentCategories(typeCategory);

        return new RecyclerViewParentCategoriesAdapter(CategoriesListFragment.this.getActivity(), categories);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("tag", "Operation Templates List Fragment - 'onCreateOptionsMenu'");
//        inflater.inflate(R.menu.menu_cashes_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("tag", "Operation Templates List Fragment - 'onOptionsItemSelected'");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in d.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_cash) {
            startActivity(new Intent(getActivity(), NewCashAccountActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}

