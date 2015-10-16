package com.thinkdevs.designmymfcommon.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.main.AcMain;

import java.util.List;

public class FrTabCategories extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static FrTabCategories newInstance() {

        return new FrTabCategories();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_with_tabs, container, false);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new AdFrStPgCategories(getActivity().getSupportFragmentManager(),
                getActivity()));

        if(savedInstanceState != null){
            mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
        }

        // Give the TabLayout the ViewPager

        AppBarLayout appBarLayout = ((AcMain)getActivity()).getAppBarLayout();

        mTabLayout = (TabLayout)appBarLayout.findViewById(R.id.tabs);
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public static String POSITION = "POSITION";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabLayout.getSelectedTabPosition());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTabLayout.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callOnActivityResultOnChildFragments(this, requestCode, resultCode, data);
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
}
