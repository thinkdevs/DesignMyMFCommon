package com.thinkdevs.designmymfcommon.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.ListCategoriesSpinnerAdapter;
import com.thinkdevs.designmymfcommon.common.FrDgDecorChooseColor;
import com.thinkdevs.designmymfcommon.common.FrDgDecorChooseIcon;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Icon;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.ArrayList;
import java.util.List;

public class AcCreateCategoryTemp extends AppCompatActivity
        implements View.OnClickListener, FrDgDecorChooseColor.ChooseColorDialogListener,
                   FrDgDecorChooseIcon.ChooseIconDialogListener {

    private Spinner    mSpHierarchy;
    private Spinner    mSpOperations;
    private Spinner    mSpParent;
    private EditText   mEtName;
    private ImageView  mIvIcon;
    private ImageView  mIvColor;

    private List<View> mAllViews          = new ArrayList<>(); //Список всех view
    private List<View> mCreateParentViews = new ArrayList<>(); //Список view создания родительской
    private List<View> mCreateChildViews  = new ArrayList<>(); //Список view создания дочерней
    private List<View> mEditParentViews   = new ArrayList<>(); //Список view для категорий
    private List<View> mEditChildViews    = new ArrayList<>(); //Список view для подкатегорий

    private final int INDEX_EXPENSES = 0;
    private final int INDEX_PROFITS  = 1;
    private final int INDEX_ROOT     = 0;
    private final int INDEX_INSERTED = 1;

    private List<Color> mColors;
    private List<Icon>  mIcons;

    private Intent mIntent;
    private Bundle mBundle;

    private int  mOperations = Category.EXPENSE;
    private int  mHierarchy  = Category.PARENT;
    private int  mOpenAs;
    private long mParentId;

    private long mCurrentIconId;
    private long mCurrentColorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_create_category);

        mIntent = getIntent();
        mBundle = mIntent.getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mBundle != null) {
            mOpenAs = mBundle.getInt(Constants.OPEN_AS);
            toolbar.setTitle(mBundle.getString(Constants.ACTIVITY_TITLE));
            setTitle(mBundle.getString(Constants.ACTIVITY_TITLE));
            if (isOpenAsEditor()) {
                setTitle(getResources().getString(R.string.title_activity_category_editing));
            }
            else
                setTitle(getResources().getString(R.string.title_activity_new_category));
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Название
        mEtName   = ((EditText) findViewById(R.id.et_name));
        //Тип операций
        mSpOperations = (Spinner) findViewById(R.id.spOperations);
        mSpOperations.setAdapter(
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.operations)
                ));
        //Тип иерархии
        mSpHierarchy = (Spinner) findViewById(R.id.spHierarchy);
        mSpHierarchy.setAdapter(
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.hierarchy)
                ));

        //Корневая категория
        mSpParent = ((Spinner) findViewById(R.id.spParent));
        setSpParentAdapter();

        //Цвет и логотип по умолчанию
        mIvColor = (ImageView)findViewById(R.id.ivColor);
        mIvIcon = (ImageView)findViewById(R.id.ivIcon);
        mIvIcon.setColorFilter(getResources().getColor(R.color.grey));

        mIvColor.setOnClickListener(this);
        mIvIcon.setOnClickListener(this);

        mIcons = Icon.getCategoryIcons();
        Icon defaultIcon = mIcons.get(0);
        mCurrentIconId   = defaultIcon.getId();
        mIvIcon.setImageResource(defaultIcon.getResourceId());

        mColors = Color.getColorsWithoutSystems();
        Color defaultColor = mColors.get(0);
        mCurrentColorId    = defaultColor.getId();
        mIvColor.setColorFilter(getResources().getColor(defaultColor.getResourceId()));

        mAllViews.add(findViewById(R.id.llOperations));
        mAllViews.add(findViewById(R.id.llHierarchy));
        mAllViews.add(findViewById(R.id.llDecoration));
        mAllViews.add(findViewById(R.id.llParent));

        mCreateParentViews.add(findViewById(R.id.llDecoration));

        mCreateChildViews.add(findViewById(R.id.llParent));

        mEditParentViews.add(findViewById(R.id.llDecoration));

        mEditChildViews.add(findViewById(R.id.llParent));

        mSpOperations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test_test", String.valueOf(position));
                switch (position) {
                    case INDEX_EXPENSES:
                        mOperations = Category.EXPENSE;
                        break;
                    case INDEX_PROFITS:
                        mOperations = Category.PROFIT;
                        break;
                }
                setSpParentAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpHierarchy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case INDEX_ROOT:
                        mHierarchy = Category.PARENT;
                        Log.d("testim", "иерархия - Корневая");
                        hideViews(mAllViews);
                        showViews(mCreateParentViews);
                        break;
                    case INDEX_INSERTED:
                        mHierarchy = Category.CHILD;
                        Log.d("testim", "иерархия - Вложенная");
                        hideViews(mAllViews);
                        showViews(mCreateChildViews);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        openAs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in d.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if (id == R.id.action_save) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeType(int type){
        if (type == Category.PROFIT)
            mSpOperations.setSelection(INDEX_PROFITS);
        else {
            mSpOperations.setSelection(INDEX_EXPENSES);
        }
    }

    private void hideViews(List<View> views){
        for(View view : views){
            view.setVisibility(View.GONE);
        }
    }

    private void showViews(List<View> views){
        for(View view : views){
            view.setVisibility(View.VISIBLE);
        }
    }

    private void openAs(){
        Category category;
        if(mBundle.containsKey(Constants.CATEGORY_ID)) {
           category = Category.getById(mBundle.getLong(Constants.CATEGORY_ID));
        }
        else
           category = new Category();

        switch (mOpenAs){
            case Category.CREATE_CATEGORY :
                Log.d("testim", "openAS : " + "create_category");
                break;
            case Category.EDIT_PARENT :
                openParentEditor(category);
                Log.d("testim", "openAS : " + "edit_parent");
                break;
            case Category.EDIT_CHILD:
                openChildEditor(category);
                Log.d("testim", "openAS : " + "edit_child");
                break;
            case Category.CREATE_CHILD:
                openChildCreator(category);
                Log.d("testim", "openAS : " + "create_child");
                break;
        }
    }

    private void openParentEditor(Category parent){
        //Тип иерархии
        mSpHierarchy.setSelection(INDEX_ROOT);
        //Тип категории
        mOperations = parent.getType();
        changeType(mOperations);
        //Имя
        mEtName.setText(parent.getName());
        //Логотип
        mCurrentIconId  = parent.getIcon().getId();
        //Цвет
        mCurrentColorId = parent.getColor().getId();
        //Логотип
        for(int i = 0; i < mIcons.size(); i++){
            Icon icon = mIcons.get(i);
            if(icon.getId() == mCurrentIconId){
                mIvIcon.setImageResource(icon.getResourceId());
            }
        }
        //Цвет
        for(int i = 0; i < mColors.size(); i++){
            Color color = mColors.get(i);
            if(color.getId() == mCurrentColorId) {
                mIvColor.setColorFilter(getResources().getColor(color.getResourceId()));
            }
        }
    }

    private void openChildEditor(Category child){
        //Построение вида активити
        hideViews(mAllViews);
        showViews(mEditChildViews);
        //Имя
        mEtName.setText(child.getName());
        //Тип иерархии
        mHierarchy = Category.CHILD;
        //Тип категории.
        mOperations = child.getType();
        setSpParentAdapter();
        //Категория
        List<Category> parentCategories = Category.getParentCategoriesWithoutEmpty(mOperations);
        for (int i = 0; i < parentCategories.size(); i++) {
            if (parentCategories.get(i).getId() == child.getParent().getId()) {
                mSpParent.setSelection(i);
                break;
            }
        }
        hideViews(mAllViews);
        showViews(mEditChildViews);
    }

    private void openChildCreator(Category parent){
        //Построение вида активити
        hideViews(mAllViews);
        showViews(mEditChildViews);
        //Тип иерархии
        mHierarchy = Category.CHILD;
        //Тип операций.
        mOperations = parent.getType();
        setSpParentAdapter();
        //Категория
        List<Category> parentCategories = Category.getParentCategoriesWithoutEmpty(mOperations);
        for (int i = 0; i < parentCategories.size(); i++) {
            if (parentCategories.get(i).getId() == parent.getId()) {
                mSpParent.setSelection(i);
                break;
            }
        }
    }

    private void save(){
        switch (getSaveAs()){
            case Category.CREATE_PARENT:
                createParent();
                Log.d("testim", "saveAS : " + "create_parent");
                break;
            case Category.EDIT_PARENT :
                updateParent();
                Log.d("testim", "saveAS : " + "edit_parent");
                break;
            case Category.CREATE_CHILD:
                createChild();
                Log.d("testim", "saveAS : " + "create_child");
                break;
            case Category.EDIT_CHILD:
                updateChild();
                Log.d("testim", "saveAS : " + "edit_child");
                break;
        }
    }

    private int getSaveAs() {
        if (mOpenAs == Category.CREATE_CATEGORY)
            return mHierarchy == Category.CHILD ? Category.CREATE_CHILD : Category.CREATE_PARENT;
        else
            return mOpenAs;
    }

    private void createParent(){
        saveAsParent(new Category());
    }

    private void updateParent(){
        saveAsParent(Category.getById((mBundle.getLong(Constants.CATEGORY_ID))));
    }

    private void createChild(){
        saveAsChild(new Category());
    }

    private void updateChild(){
        saveAsChild(Category.getById(mBundle.getLong(Constants.CATEGORY_ID)));
    }

    private void saveAsParent(Category parent){
        //Имя
        String name = String.valueOf(mEtName.getText());
        //Логотип
        Icon icon = Icon.getById(mCurrentIconId);
        //Цвет
        Color color = Color.getById(mCurrentColorId);
        // Проверка условий и сохранение
        if (name == null || name.length() == 0) {
            Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
        } else if (Category.isExistParent(name, mOperations) && !isOpenAsEditor()) {
            Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
        } else {
            parent.setName(name);
            parent.setType(mOperations);
            parent.setColor(color);
            parent.setIcon(icon);
            parent.save();
            parent.updateSubs();
            returnResult(parent.getId());
        }
    }

    private void saveAsChild(Category child){
        //Имя
        String name = String.valueOf(mEtName.getText());
        //Родительская категория
        long parentId = (long)(mSpParent.getSelectedView().findViewById(R.id.cv_parent_category)).getTag(R.string.tag_category_id);
        Category parent = Category.getById(parentId);

        Log.d("testim", "saveAsChild" + " parent = " + parent.getName());

        // Проверка условий и сохранение
        if (name == null || name.length() == 0)
            Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
        else if (parent.isExistChild(name) && !isOpenAsEditor())
            Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
        else {
            child.setName(name);
            child.setParent(parent);
            child.save();
            returnResult(child.getId());
        }
    }

    private boolean isOpenAsEditor(){
        return Category.EDIT_CHILD == mOpenAs || mOpenAs == Category.EDIT_PARENT;
    }

    private void returnResult (long id){
        Intent result = new Intent();
        result.putExtra(Constants.CATEGORY_ID, id);
        result.putExtra(Constants.CATEGORY_POSITION, mBundle.getInt(Constants.CATEGORY_POSITION));
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onChooseColor(long colorId) {
        Color color = Color.getById(colorId);
        mIvColor.setColorFilter(getResources().getColor(color.getResourceId()));
        mCurrentColorId = colorId;
    }

    @Override
    public void onChooseIcon(long iconId) {
        Icon icon = Icon.getById(iconId);
        mIvIcon.setImageResource(icon.getResourceId());
        mCurrentIconId = iconId;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivColor:
                FrDgDecorChooseColor.newInstance(this, mCurrentColorId)
                        .show(getFragmentManager(), "chooseDecorColor");
                break;
            case R.id.ivIcon:
                FrDgDecorChooseIcon.newInstance(this, mCurrentIconId, Icon.TYPE_CATEGORY)
                        .show(getFragmentManager(), "chooseDecorIcon");
                break;
        }
    }

    /**
     * Update adapter after change mOperations
     */
    private void setSpParentAdapter(){
        Log.d("testim", "setSpParentAdapter");
        mSpParent.setAdapter(
                new ListCategoriesSpinnerAdapter(
                        AcCreateCategoryTemp.this,
                        Category.getParentCategoriesWithoutEmpty(mOperations)));
    }

}
