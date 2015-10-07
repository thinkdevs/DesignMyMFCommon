package com.thinkdevs.designmymfcommon.fragment;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.thinkdevs.designmymfcommon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Fragment used for managing interactions for and presentation of a navigation test_new_design_drawer_view.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the test_new_design_drawer_view on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Attributes names for Simple adapter
     */
    private static final String ATTRIBUTE_ICON = "logo";
    private static final String ATTRIBUTE_TITLE = "title";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation test_new_design_drawer_view.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // test_new_design_drawer_view. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
        Log.d("tag", "Navigation Drawer Fragment - 'onCreate' savedInstance = " + savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
        Log.d("tag", "Navigation Drawer Fragment - 'onActivityCreated' savedInstanceState = " + savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });


        String[] titles = {
                getString(R.string.title_section_wallets),
                getString(R.string.title_section_expenses),
                getString(R.string.title_section_profits),
                getString(R.string.title_section_categories),
                getString(R.string.title_section_operation_templates)};

        int[] icons = {
                R.drawable.wallet,
                R.drawable.expense,
                R.drawable.profit,
                R.drawable.category,
                R.drawable.ic_description_black_24dp};

        ArrayList<Map<String,Object>> data = new ArrayList<>();
        Map<String,Object> attr;
        for(int i = 0; i < titles.length; i++){
            attr = new HashMap<>();
            attr.put(ATTRIBUTE_ICON, icons[i]);
            attr.put(ATTRIBUTE_TITLE, titles[i]);
            data.add(attr);
        }

        String[] from = {
                ATTRIBUTE_ICON,
                ATTRIBUTE_TITLE};

        int[] to = {
                R.id.iv_icon,
                R.id.tv_title};

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                data,
                R.layout.item_navigation_drawer,
                from,
                to);

        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        Log.d("tag", "Navigation Drawer Fragment - 'onCreateView'");
        return mDrawerListView;

    }

    public boolean isDrawerOpen() {
        Log.d("tag", "NavigationDrawerFragment - 'isDrawerOpen'");
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation test_new_design_drawer_view interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {

        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the test_new_design_drawer_view opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the test_new_design_drawer_view's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation test_new_design_drawer_view and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.menu,             /* nav test_new_design_drawer_view image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open test_new_design_drawer_view" description for accessibility */
                R.string.navigation_drawer_close  /* "close test_new_design_drawer_view" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                Log.d("tag", "Navigation Drawer Fragment - 'onDrawerClosed'");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the test_new_design_drawer_view; store this flag to prevent auto-showing
                    // the navigation test_new_design_drawer_view automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                Log.d("tag", "Navigation Drawer Fragment - 'onDrawerOpened'");
            }
        };

        // If the user hasn't 'learned' about the test_new_design_drawer_view, open it to introduce them to the test_new_design_drawer_view,
        // per the navigation test_new_design_drawer_view design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        Log.d("tag", "Navigation Drawer Fragment - 'setUp'");
    }

    private void selectItem(int position) {

        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
        Log.d("tag", "Navigation Drawer Fragment - 'selectItem' " + position);
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
        Log.d("tag", "Navigation Drawer Fragment - 'onAttach'");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        Log.d("tag", "Navigation Drawer Fragment - 'onDetach'");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        Log.d("tag", "Navigation Drawer Fragment - 'onSaveInstanceState' " + mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the test_new_design_drawer_view toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
        Log.d("tag", "Navigation Drawer Fragment - 'onConfigurationChanged'");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the test_new_design_drawer_view is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("tag", "Navigation Drawer Fragment - 'onCreateOptionsMenu'");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Log.d("tag", "Navigation Drawer Fragment - 'onOptionsItemSelected'");
        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation test_new_design_drawer_view design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
        Log.d("tag", "Navigation Drawer Fragment - 'showGlobalContextActionBar'");
    }

    private ActionBar getActionBar() {
        Log.d("tag", "Navigation Drawer Fragment - 'getActionBar'");
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation test_new_design_drawer_view is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
