package com.thinkdevs.designmymfcommon.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCashAccountActivity;
import com.thinkdevs.designmymfcommon.adapter.RecyclerViewParentCategoriesAdapter;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class CategoriesListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "Operation Templates List Fragment - 'onCreate' savedInstance = " + savedInstanceState);
        super.onCreate(savedInstanceState);

        // Включаем отображение меню
        setHasOptionsMenu(true);
    }

    public static CategoriesListFragment newInstance(int type) {
        Log.d("tag", "Operation Templates List Fragment - 'newInstance'" );
        Log.d("test", String.valueOf(type));
        CategoriesListFragment fragment = new CategoriesListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CATEGORY_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args != null)
            mType = args.getInt(Constants.CATEGORY_TYPE);

        View view = inflater.inflate(R.layout.fragment_recycler_view_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(CategoriesListFragment.this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Log.d("test", String.valueOf(mType));

        List<Category> categoriesList = Category.getParentCategoriesWithoutEmpty(mType);

        mAdapter = new RecyclerViewParentCategoriesAdapter(CategoriesListFragment.this.getActivity(), categoriesList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
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

