package com.thinkdevs.designmymfcommon.samples;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.main.LoadToDataBase;
import com.thinkdevs.designmymfcommon.cashaccounts.FrListCashAccounts;
import com.thinkdevs.designmymfcommon.operationtemplates.FrListOperationTemplates;
import com.thinkdevs.designmymfcommon.operations.FrListOperations;


public class MainNavigationDrawerActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation test_new_design_drawer_view.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    SharedPreferences preferences = null;
    public static final String FIRST_RUN = "firstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        preferences = getSharedPreferences("com.thinkdevs.designmymfcommon", MODE_PRIVATE);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the test_new_design_drawer_view.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        Log.d("tag", "Main Navigation Drawer Activity - 'onCreate' savedInstance = " + savedInstanceState);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position){
            case 0: fragmentManager.beginTransaction()
                    .replace(R.id.container, FrListCashAccounts.newInstance())
                    .commit();
                break;
            case 1: fragmentManager.beginTransaction()
                    .replace(R.id.container, FrListOperations.newInstance())
                    .commit();
                break;
            case 2: fragmentManager.beginTransaction()
                    .replace(R.id.container, FrListOperations.newInstance())
                    .commit();
                break;
            case 3: fragmentManager.beginTransaction()
                    .replace(R.id.container, FrListOperations.newInstance())
                    .commit();
                break;
            case 4: fragmentManager.beginTransaction()
                    .replace(R.id.container, FrListOperationTemplates.newInstance())
                    .commit();
                break;
        }
        Log.d("tag", "Main Navigation Drawer Activity - 'onNavigationDrawerItemSelected' " + position);

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
        Log.d("tag", "Main Navigation Drawer Activity - 'onResume'");
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section_wallets);
                break;
            case 2:
                mTitle = getString(R.string.title_section_expenses);
                break;
            case 3:
                mTitle = getString(R.string.title_section_profits);
                break;
            case 4:
                mTitle = getString(R.string.title_section_categories);
                break;
            case 5:
                mTitle = getString(R.string.title_section_operation_templates);
                break;
        }
        Log.d("tag", "Main Navigation Drawer Activity - 'onSectionAttached' " + number);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        Log.d("tag", "Main Navigation Drawer Activity - 'restoreActionBar'");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("tag", "Main Navigation Drawer Activity - 'onCreateOptionMenu'");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the test_new_design_drawer_view is not showing. Otherwise, let the test_new_design_drawer_view
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("tag", "Main Navigation Drawer Activity - 'onOptionItemSelected'");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in d.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
