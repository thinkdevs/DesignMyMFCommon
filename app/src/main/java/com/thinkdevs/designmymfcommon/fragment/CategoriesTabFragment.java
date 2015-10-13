package com.thinkdevs.designmymfcommon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.MainActivity;
import com.thinkdevs.designmymfcommon.adapter.SampleFragmentPagerAdapter;

public class CategoriesTabFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static CategoriesTabFragment newInstance() {

        return new CategoriesTabFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_with_tabs, container, false);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity()));

        if(savedInstanceState != null){
            mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
        }

        // Give the TabLayout the ViewPager

        AppBarLayout appBarLayout = ((MainActivity)getActivity()).getAppBarLayout();

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
}
