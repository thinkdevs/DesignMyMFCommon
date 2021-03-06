package com.thinkdevs.designmymfcommon.categories;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdFrStPgCategories extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Расходы", "Доходы" };
    private Context context;

    public AdFrStPgCategories(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return FrListCategories.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
