package com.thinkdevs.designmymfcommon.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.samples.AcCreateCategoryTemp;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class FrListCategories extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    public static final int REQUEST_CODE_ADD          = 1;
    public static final int REQUEST_CODE_CHANGE       = 2;
    public static final int REQUEST_CODE_ADD_CHILD    = 3;
    public static final int REQUEST_CODE_CHANGE_CHILD = 4;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Category> mCategories;

    private long mIdCategoryToUpdate;

    private int mPage;
    private int mType;

    public static FrListCategories newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FrListCategories fragment = new FrListCategories();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mType = mPage == 1 ? Category.EXPENSE : Category.PROFIT;

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(FrListCategories.this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mCategories = Category.getParentCategoriesWithoutEmpty(mType);

        mAdapter = new AdRvParentCategories(FrListCategories.this, mCategories);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        floatingActionButton.attachToRecyclerView(mRecyclerView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AcCreateCategoryTemp.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.OPEN_AS, Category.CREATE_CATEGORY);
                intent.putExtras(bundle);
                getActivityStarterFragment().startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;
        long id;
        int position;
        switch (requestCode){
            case REQUEST_CODE_ADD:
                Log.d("testim", "request code add");
                id = data.getLongExtra(Constants.CATEGORY_ID, 0);
                ((AdRvParentCategories)mAdapter).updateAfterAdd(id);
                break;
            case REQUEST_CODE_CHANGE:
                Log.d("testim", "request code change");
                id = data.getLongExtra(Constants.CATEGORY_ID, 0);
                position = data.getIntExtra(Constants.CATEGORY_POSITION, 0);
                ((AdRvParentCategories)mAdapter).updateAfterChange(id, position);
                break;
        }

        callOnActivityResultOnChildFragments(this, requestCode, resultCode, data);
    }


    private Fragment getActivityStarterFragment() {
        if (getParentFragment() != null) {
            return getParentFragment();
        }
        return this;
    }

    public static void callOnActivityResultOnChildFragments(Fragment parent, int requestCode, int resultCode, Intent data) {
        FragmentManager childFragmentManager = parent.getChildFragmentManager();
        if (childFragmentManager != null) {
            List<Fragment> childFragments = childFragmentManager.getFragments();
            if (childFragments == null) {
                return;
            }
            for (Fragment child : childFragments) {
                if (child != null && !child.isDetached() && !child.isRemoving()) {
                    child.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }
}
