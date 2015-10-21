package com.thinkdevs.designmymfcommon.samples;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

    private final String LOG_TAG = "AcCreateCategory";
    private static boolean mDebug = true;

    private Spinner    mSpHierarchy;
    private Spinner    mSpTypeOperations;
    private Spinner    mSpParent;
    private EditText   mEtName;
    private ImageView  mIvIcon;
    private ImageView  mIvColor;

    private List<View> mEditCategoryViews = new ArrayList<>(); //Список view скрываемых при редактировании
    private List<View> mCreateParentViews = new ArrayList<>(); //Список view создания родительской
    private List<View> mCreateChildViews  = new ArrayList<>(); //Список view создания дочерней

    //Позиции в spinners
    private final int INDEX_EXPENSES = 0;
    private final int INDEX_PROFITS  = 1;
    private final int INDEX_PARENT   = 0;
    private final int INDEX_CHILD    = 1;

    //Элементы декора
    private List<Color> mColors;
    private List<Icon>  mIcons;

    private Bundle mExtras;

    private int  mTypeOperations = Category.EXPENSE;
    private int  mHierarchy      = Category.PARENT;
    private int  mOpenAs;
    private int  mHierarchyOld;
    private long mParentId;

    private long mCurrentIconId;
    private long mCurrentColorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_create_category);

        mExtras = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mExtras != null) {
            mOpenAs = mExtras.getInt(Constants.OPEN_AS);
            toolbar.setTitle(mExtras.getString(Constants.ACTIVITY_TITLE));
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //Название
            mEtName = ((EditText) findViewById(R.id.et_name));
            //Тип операций
            mSpTypeOperations = (Spinner) findViewById(R.id.spOperations);
            mSpTypeOperations.setAdapter(
                    new ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.operations)
                    ));
            mSpTypeOperations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mDebug)
                        Log.d(LOG_TAG, "mSpTypeOperations 'onItemSelected()' " + position);
                    switch (position) {
                        case INDEX_EXPENSES:
                            if (mDebug)
                                Log.d(LOG_TAG, "    set mTypeOperations = Expenses");
                            mTypeOperations = Category.EXPENSE;
                            break;
                        case INDEX_PROFITS:
                            if (mDebug)
                                Log.d(LOG_TAG, "    set mTypeOperations = Profits");
                            mTypeOperations = Category.PROFIT;
                            break;
                    }
                    setSpParentAdapter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Тип иерархии
            mSpHierarchy = (Spinner) findViewById(R.id.spHierarchy);
            mSpHierarchy.setAdapter(
                    new ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.hierarchy)
                    ));
            mSpHierarchy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mDebug)
                        Log.d(LOG_TAG, "mSpHierarchy 'onItemSelected()' " + position);
                    switch (position) {
                        case INDEX_PARENT:
                            if (mDebug)
                                Log.d(LOG_TAG, "    set mHierarchy = Parent");
                            mHierarchy = Category.PARENT;
                            hideViews(mCreateChildViews);
                            showViews(mCreateParentViews);
                            break;
                        case INDEX_CHILD:
                            mHierarchy = Category.CHILD;
                            if (mDebug)
                                Log.d(LOG_TAG, "    set mHierarchy = Child");
                            hideViews(mCreateParentViews);
                            showViews(mCreateChildViews);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Корневая категория
            mSpParent = ((Spinner) findViewById(R.id.spParent));
            setSpParentAdapter();
            if(savedInstanceState != null){
                mSpParent.setSelection(savedInstanceState.getInt("position_parent"));
            }

            //Иконки
            mIvIcon = (ImageView) findViewById(R.id.ivIcon);
            mIvIcon.setColorFilter(getResources().getColor(R.color.grey));
            mIvIcon.setOnClickListener(this);
            //Список иконок
            mIcons = Icon.getCategoryIcons();
            //Иконка по умолчанию
            Icon defaultIcon = mIcons.get(0);
            mCurrentIconId = defaultIcon.getId();
            mIvIcon.setImageResource(defaultIcon.getResourceId());

            //Цвет
            mIvColor = (ImageView) findViewById(R.id.ivColor);
            mIvColor.setOnClickListener(this);
            //Список цветов
            mColors = Color.getColorsWithoutSystems();
            //Цвет по умолчанию
            Color defaultColor = mColors.get(0);
            mCurrentColorId = defaultColor.getId();
            mIvColor.setColorFilter(getResources().getColor(defaultColor.getResourceId()));

            //Списки view для смены вида активити
            mEditCategoryViews.add(findViewById(R.id.llOperations));
            mCreateParentViews.add(findViewById(R.id.llDecoration));
            mCreateChildViews.add(findViewById(R.id.llParent));


            requestFocus(findViewById(R.id.ll_new_category));

            openAs();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mDebug)
            Log.d(LOG_TAG, "********** 'onSaveInstanceState()' **********");
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("position_parent", mSpParent.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mDebug)
            Log.d(LOG_TAG, "********** 'onRestoreInstanceState()' **********");
        super.onRestoreInstanceState(savedInstanceState);
        mSpParent.setSelection(savedInstanceState.getInt("position_parent"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDebug)
            Log.d(LOG_TAG, "********** 'onDestroy()' **********");
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
        if (mDebug)
            Log.d(LOG_TAG, "'changeType()' type = " + type);
        switch (type) {
            case Category.PROFIT:
                mSpTypeOperations.setSelection(INDEX_PROFITS);
                break;
            case Category.EXPENSE:
                mSpTypeOperations.setSelection(INDEX_EXPENSES);
                break;
        }
    }

    private void hideViews(final List<View> views){
        if (mDebug)
            Log.d(LOG_TAG, "'hideViews()' " + views);
        if(views == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(View view : views){
                    view.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showViews(final List<View> views){
        if (mDebug)
            Log.d(LOG_TAG, "'showViews()' " + views);
        if(views == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(View view : views){
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void openAs(){
        if(mDebug)
            Log.d(LOG_TAG, "'openAs()' mOpenAs = " + mOpenAs);

        Category category;
        if(mExtras.containsKey(Constants.CATEGORY_ID)) {
            category = Category.getById(mExtras.getLong(Constants.CATEGORY_ID));
        }
        else {category = new Category();}

        if(mExtras.containsKey(Constants.CATEGORY_TYPE)){
            changeType(mExtras.getInt(Constants.CATEGORY_TYPE));
        }

        switch (mOpenAs){
            case Category.CREATE_CATEGORY :
                if(mDebug)
                    Log.d(LOG_TAG, "    CREATE_CATEGORY");
                break;
            case Category.EDIT_PARENT :
                if(mDebug)
                    Log.d(LOG_TAG, "    EDIT_PARENT");
                openParentEditor(category);
                break;
            case Category.EDIT_CHILD:
                if(mDebug)
                    Log.d(LOG_TAG, "    EDIT_CHILD");
                openChildEditor(category);
                break;
            case Category.CREATE_CHILD:
                if(mDebug)
                    Log.d(LOG_TAG, "    CREATE_CHILD");
                openChildCreator(category);
                break;
        }
    }

    private void openParentEditor(Category parent){
        if(mDebug)
            Log.d(LOG_TAG, "'openParentEditor()' parent = " + parent.getName());
        //Тип иерархии
        mSpHierarchy.setSelection(INDEX_PARENT);
        //Тип категории
        mTypeOperations = parent.getType();
        changeType(mTypeOperations);
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
        //Построение вида активити
        hideViews(mEditCategoryViews);
        hideViews(mCreateChildViews);
        showViews(mCreateParentViews);
    }

    private void openChildEditor(Category child){
        if(mDebug)
            Log.d(LOG_TAG, "'openChildEditor()' child = " + child.getName());
        //Имя
        mEtName.setText(child.getName());
        //Тип иерархии
        mSpHierarchy.setSelection(INDEX_CHILD);
        //Тип категории.
        mTypeOperations = child.getType();
        setSpParentAdapter();
        //Категория
        List<Category> parentCategories = Category.getParentCategoriesWithoutEmpty(mTypeOperations);
        for (int i = 0; i < parentCategories.size(); i++) {
            if (parentCategories.get(i).getId() == child.getParent().getId()) {
                mSpParent.setSelection(i);
                break;
            }
        }
        //Построение вида активити
        hideViews(mEditCategoryViews);
        hideViews(mCreateParentViews);
        showViews(mCreateChildViews);
    }

    private void openChildCreator(Category parent){
        if(mDebug)
            Log.d(LOG_TAG, "'openChildCreator()' parent = " + parent.getName());
        //Тип иерархии
        mSpHierarchy.setSelection(INDEX_CHILD);
        //Тип операций.
        mTypeOperations = parent.getType();
        setSpParentAdapter();
        //Категория
        List<Category> parentCategories = Category.getParentCategoriesWithoutEmpty(mTypeOperations);
        for (int i = 0; i < parentCategories.size(); i++) {
            if (parentCategories.get(i).getId() == parent.getId()) {
                mSpParent.setSelection(i);
                break;
            }
        }
        hideViews(mCreateParentViews);
        showViews(mCreateChildViews);
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

    private void createParent() {
        saveAsParent(new Category());
    }

    private void updateParent(){
        saveAsParent(Category.getById((mExtras.getLong(Constants.CATEGORY_ID))));
    }

    private void createChild(){
        saveAsChild(new Category());
    }

    private void updateChild(){
        saveAsChild(Category.getById(mExtras.getLong(Constants.CATEGORY_ID)));
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
        } else if (Category.isExistParent(name, mTypeOperations) && !isOpenAsEditor()) {
            Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
        } else {
            parent.setName(name);
            parent.setType(mTypeOperations);
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
        result.putExtra(Constants.CATEGORY_POSITION, mExtras.getInt(Constants.CATEGORY_POSITION));
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
     * Update adapter after change mTypeOperations
     */
    private void setSpParentAdapter(){
        if(mDebug)
            Log.d(LOG_TAG, "'setSpParentAdapter()', current mTypeOperations = " + mTypeOperations);
        mSpParent.setAdapter(
                new ListCategoriesSpinnerAdapter(
                        AcCreateCategoryTemp.this,
                        Category.getParentCategoriesWithoutEmpty(mTypeOperations)));
    }

    private void requestFocus(View view) {
        if(mDebug)
            Log.d(LOG_TAG, "'requestFocus()', " + view);
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
