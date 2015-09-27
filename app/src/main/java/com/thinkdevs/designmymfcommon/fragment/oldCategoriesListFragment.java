package com.thinkdevs.designmymfcommon.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;

import com.melnykov.fab.FloatingActionButton;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.MainNavigationDrawerActivity;
import com.thinkdevs.designmymfcommon.activity.NewCategoryActivity;
import com.thinkdevs.designmymfcommon.adapter.ExpandableListCategoriesAdapter;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.SubCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class oldCategoriesListFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";


    final String ATTRIBUTE_NAME_LOGO = "logo";
    final String ATTRIBUTE_NAME_TITLE = "title";

    boolean typeCategory = true; // if TRUE then Expensive

    RadioGroup radioGroupType;
    ExpandableListView expandableListView;

    List<Category> categoryExpenses; //Список категорий расходов
    List<Category> categoryProfits; //Список категорий доходов

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "Categories List Fragment - 'onCreate' savedInstance = " + savedInstanceState);
        super.onCreate(savedInstanceState);
        // Включаем отображение меню
        setHasOptionsMenu(true);
    }

    public static oldCategoriesListFragment newInstance(int sectionNumber) {
        Log.d("tag", "Categories List Fragment - 'newInstance'");
        oldCategoriesListFragment fragment = new oldCategoriesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("tag", "Categories List Fragment - 'onAttach'");
        super.onAttach(activity);
        ((MainNavigationDrawerActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Log.d("tag", "Categories List Fragment - 'onCreateView'");
            View view = inflater.inflate(R.layout.fragment_list_categories, container, false);


            radioGroupType = ((RadioGroup) view.findViewById(R.id.rg_type_category));
            typeCategory = (radioGroupType.getCheckedRadioButtonId() == R.id.rb_operation_expense);

            expandableListView = ((ExpandableListView) view.findViewById(R.id.expandableListView));
            expandableListView.setAdapter(getAdapter(typeCategory));

            radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    ExpandableListCategoriesAdapter adapter;

                    switch (checkedId) {
                        case R.id.rb_operation_expense:
                            typeCategory = true;
                            adapter = getAdapter(typeCategory);
                            expandableListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                        case R.id.rb_operation_profit:
                            typeCategory = false;
                            adapter = getAdapter(typeCategory);
                            expandableListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                }
            });

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewCategoryActivity.class));
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("tag", "Categories List Fragment - 'onCreateOptionsMenu'");
//        inflater.inflate(R.menu.global, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("tag", "Categories List Fragment - 'onOptionsItemSelected'");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in d.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_category) {
            startActivity(new Intent(getActivity(), NewCategoryActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Get Expandable list adapter
     * @param typeCategory
     * @return
     */
    private ExpandableListCategoriesAdapter getAdapter(boolean typeCategory){

        List<Integer> colors = new ArrayList<>();
        List<Category> categories;

        if(typeCategory){
            categories = Category.getExpenseCategories();
        }
        else
            categories = Category.getProfitCategories();

        // Collection for Categories
        ArrayList<Map<String, Object>> categoryData;
        // Collection for items of the same category
        ArrayList<Map<String, Object>> subCategoryDataItem;
        // Collection subcategories
        ArrayList<ArrayList<Map<String, Object>>> subCategoryData;
        // Collection attributes
        Map<String, Object> atr;


        String[] categoryFrom = {ATTRIBUTE_NAME_TITLE};
        int[] categoryTo = {android.R.id.text1};

        String[] subCategoryFrom = {ATTRIBUTE_NAME_TITLE};
        int[] subCategoryTo = {android.R.id.text1};

        categoryData = new ArrayList<>();
        subCategoryData = new ArrayList<>();

        for(Category category : categories){
            atr = new HashMap<>();
//            atr.put(ATTRIBUTE_NAME_LOGO, category.getLogo().getResourceId());
            atr.put(ATTRIBUTE_NAME_TITLE, category.getName());
            colors.add(category.getColor().getResourceId());
            categoryData.add(atr);

            subCategoryDataItem = new ArrayList<>();

            for(SubCategory subCategory : category.getSubCategories()){
                atr = new HashMap<>();
                atr.put(ATTRIBUTE_NAME_TITLE, subCategory.getName());
                subCategoryDataItem.add(atr);
            }
            subCategoryData.add(subCategoryDataItem);
        }

        return new ExpandableListCategoriesAdapter(getActivity(),
                categoryData,
                android.R.layout.simple_expandable_list_item_1,
                categoryFrom,
                categoryTo,
                subCategoryData,
                android.R.layout.simple_expandable_list_item_1,
                subCategoryFrom,
                subCategoryTo,
                colors);
    }
}
