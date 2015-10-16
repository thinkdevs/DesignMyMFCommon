package com.thinkdevs.designmymfcommon.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.cashaccounts.FrListCashAccounts;
import com.thinkdevs.designmymfcommon.categories.FrTabCategories;
import com.thinkdevs.designmymfcommon.operationtemplates.FrListOperationTemplates;
import com.thinkdevs.designmymfcommon.operations.FrListOperations;

public class AcMain extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private AppBarLayout appBarLayout;
    private NavigationView mNvDrawer;

    SharedPreferences preferences = null;
    public static final String FIRST_RUN = "firstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_new_design_navigation_drawer_layout);

        preferences = getSharedPreferences("com.thinkdevs.designmymfcommon", MODE_PRIVATE);

        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        findViewById(R.id.tabs).setVisibility(View.GONE);

        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(mNvDrawer);

        selectDrawerItem(mNvDrawer.getMenu().getItem(0));

        // Set the menu icon instead of the launcher icon.
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.menu);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(preferences.getBoolean(FIRST_RUN, true)){

            //*****************************************

            LoadToDataBase.main(); //TODO test data

            //*****************************************

            preferences.edit().putBoolean(FIRST_RUN, false).apply();
        }
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.cash_accounts:
                fragmentClass = FrListCashAccounts.class;
                break;
            case R.id.operations:
                fragmentClass = FrListOperations.class;
                break;
            case R.id.operation_templates:
                fragmentClass = FrListOperationTemplates.class;
                break;
            case R.id.categories:
                fragmentClass = FrTabCategories.class;
                break;
            default:
                fragmentClass = FrListCashAccounts.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

}
