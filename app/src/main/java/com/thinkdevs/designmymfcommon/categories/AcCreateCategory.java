package com.thinkdevs.designmymfcommon.categories;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.thinkdevs.designmymfcommon.common.FrDgNotice;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Icon;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.ArrayList;
import java.util.List;

public class AcCreateCategory extends AppCompatActivity
        implements View.OnClickListener, FrDgDecorChooseColor.ChooseColorDialogListener,
                   FrDgDecorChooseIcon.ChooseIconDialogListener{

    private static final String LOG_TAG = "AcCreateCategory";
    private static boolean mDebug = true;

    public static final int RESULT_CREATE_PARENT       = 1; //Создали родительскую
    public static final int RESULT_CREATE_CHILD        = 2; //Создали дочернюю
    public static final int RESULT_UPDATE_PARENT       = 3; //Изменили имя, цвет или иконку
    public static final int RESULT_UPDATE_CHILD        = 4; //Изменили имя
    public static final int RESULT_PARENT_TO_CHILD     = 5; //Родительская стала дочерней
    public static final int RESULT_CHILD_TO_PARENT     = 6; //Дочерняя стала родительской
    public static final int RESULT_CHILD_CHANGE_PARENT = 7; //Сменили родителя для дочерней

    private SpHierarchy mSpHierarchy;
    private Spinner     mSpTypeOperations;
    private Spinner     mSpParent;
    private EditText    mEtName;
    private ImageView   mIvIcon;
    private ImageView   mIvColor;

    private List<View> mEditCategoryViews = new ArrayList<>(); //Список view скрываемых при редактировании
    private List<View> mCreateParentViews = new ArrayList<>(); //Список view создания родительской
    private List<View> mCreateChildViews  = new ArrayList<>(); //Список view создания дочерней

    //Позиции в spinners
    private final int INDEX_EXPENSES = 0;
    private final int INDEX_PROFITS  = 1;

    //Элементы декора
    private List<Color> mColors;
    private List<Icon>  mIcons;

    private Bundle mExtras;

    private int  mTypeOperations = Category.EXPENSE;
    private int  mHierarchy      = Category.PARENT;
    private int  mOpenAs;
    private int  mHierarchyOld;

    private long mCurrentIconId;
    private long mCurrentColorId;

    //Id`s for return result and update lists
    private int  mResult; // Возвращаемый результат
    private long mCategoryId;
    private long mParentIdUpd1;
    private long mParentIdUpd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mDebug)
            Log.d(LOG_TAG, "********** 'onCreate()' **********");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_create_category);

        mExtras = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mExtras != null) {
            mOpenAs = mExtras.getInt(Constants.OPEN_AS);
            mTypeOperations = mExtras.getInt(Constants.CATEGORY_TYPE);
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
                        Log.d(LOG_TAG, "mSpTypeOperations 'onItemSelected()' " + position + " " + Thread.currentThread().getName());
                    switch (position) {
                        case INDEX_EXPENSES:
                            if (mDebug)
                                Log.d(LOG_TAG, "    set mTypeOperations = Expenses");
                            break;
                        case INDEX_PROFITS:
                            if (mDebug)
                                Log.d(LOG_TAG, "    set mTypeOperations = Profits");
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if(mTypeOperations == Category.PROFIT){
                mSpTypeOperations.setSelection(INDEX_PROFITS);
            }

            //Тип иерархии
            mSpHierarchy = (SpHierarchy) findViewById(R.id.spHierarchy);
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
                        Log.d(LOG_TAG, "mSpHierarchy 'onItemSelected()' " + position + " " + Thread.currentThread().getName());
                    switch (position) {
                        case SpHierarchy.INDEX_PARENT:
                            if (mDebug)
                                Log.d(LOG_TAG, "    set mHierarchy = Parent");
                            mHierarchy = Category.PARENT;
                            hideViews(mCreateChildViews);
                            showViews(mCreateParentViews);
                            break;
                        case SpHierarchy.INDEX_CHILD:
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
            mSpParent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mDebug) {
                        Log.d(LOG_TAG, "mSpParent 'onItemSelected()' " + position + " " + Thread.currentThread().getName());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
        mSpParent.setSelection(savedInstanceState.getInt("position_parent"), true);
    }

    @Override
    protected void onStart() {
        if (mDebug) {
            Log.d(LOG_TAG, "********** 'onStart()' **********");
        }
        super.onStart();
    }

    @Override
    protected void onPause() {
        if (mDebug) {
            Log.d(LOG_TAG, "********** 'onPause()' **********");
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mDebug) {
            Log.d(LOG_TAG, "********** 'onDestroy()' **********");
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (mDebug) {
            Log.d(LOG_TAG, "********** 'onResume()' **********");
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        if (mDebug) {
            Log.d(LOG_TAG, "********** 'onRestart()' **********");
        }
        super.onRestart();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mDebug) {
            Log.d(LOG_TAG, "********** 'onConfigurationChanged' **********");
        }
        super.onConfigurationChanged(newConfig);
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
        if (mDebug) {
            Log.d(LOG_TAG, "'changeType()' type = " + type);
        }
        switch (type) {
            case Category.PROFIT:
                mTypeOperations = Category.PROFIT;
                mSpTypeOperations.setSelection(INDEX_PROFITS, true);
                break;
            case Category.EXPENSE:
                mTypeOperations = Category.EXPENSE;
                mSpTypeOperations.setSelection(INDEX_EXPENSES, true);
                break;
        }
        setSpParentAdapter();
    }

    private void hideViews(final List<View> views){
        if (mDebug) {
            Log.d(LOG_TAG, "'hideViews()' " + views);
        }
        if(views == null)
            return;
        for(View view : views){
            view.setVisibility(View.GONE);
        }
    }

    private void showViews(final List<View> views){
        if (mDebug) {
            Log.d(LOG_TAG, "'showViews()' " + views);
        }
        if(views == null)
            return;
        for(View view : views){
            view.setVisibility(View.VISIBLE);
        }
    }

    private void openAs(){
        if(mDebug) {
            Log.d(LOG_TAG, "'openAs()' mOpenAs = " + mOpenAs);
        }
        Category category;
        if(mExtras.containsKey(Constants.CATEGORY_ID)) {
            category = Category.getById(mExtras.getLong(Constants.CATEGORY_ID));
        }
        else {category = new Category();}

        switch (mOpenAs){
            case Category.CREATE_CATEGORY :
                if(mDebug) {
                    Log.d(LOG_TAG, "    CREATE_CATEGORY");
                }
                break;
            case Category.EDIT_PARENT :
                if(mDebug) {
                    Log.d(LOG_TAG, "    EDIT_PARENT");
                }
                mHierarchyOld = Category.PARENT;
                openParentEditor(category);
                mSpHierarchy.setAllowNotice(true); // Allow show notice see SpHierarchy.class
                break;
            case Category.EDIT_CHILD:
                if(mDebug) {
                    Log.d(LOG_TAG, "    EDIT_CHILD");
                }
                mHierarchyOld = Category.CHILD;
                openChildEditor(category);
                break;
            case Category.CREATE_CHILD:
                if(mDebug) {
                    Log.d(LOG_TAG, "    CREATE_CHILD");
                }
                mHierarchyOld = Category.CHILD;
                openChildCreator(category);
                break;
        }
    }

    private void openParentEditor(Category parent){
        if(mDebug)
            Log.d(LOG_TAG, "'openParentEditor()' parent = " + parent.getName());

        mCategoryId = parent.getId();

        //Тип иерархии
        mSpHierarchy.setSelection(SpHierarchy.INDEX_PARENT);
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
        if(mDebug) {
            Log.d(LOG_TAG, "'openChildEditor()' child = " + child.getName());
        }
        //Имя
        mEtName.setText(child.getName());
        //Тип иерархии
        mSpHierarchy.setSelection(SpHierarchy.INDEX_CHILD, true);
        //Тип операций.
        mTypeOperations = child.getType();
        changeType(mTypeOperations);
        //Категория
        List<Category> parentCategories = Category.getParentCategoriesWithoutEmpty(mTypeOperations);
        for (int i = 0; i < parentCategories.size(); i++) {
            long parentId = child.getParent().getId();
            if (parentCategories.get(i).getId() == parentId) {
                mSpParent.setSelection(i, true);
                mParentIdUpd1 = parentId; //Родитель для обновления
                break;
            }
        }
        //Построение вида активити
        hideViews(mEditCategoryViews);
        hideViews(mCreateParentViews);
        showViews(mCreateChildViews);
    }

    private void openChildCreator(Category parent){
        if(mDebug) {
            Log.d(LOG_TAG, "'openChildCreator()' parent = " + parent.getName());
        }
        //Тип иерархии
        mSpHierarchy.setSelection(SpHierarchy.INDEX_CHILD, true);
        //Тип операций.
        changeType(parent.getType());
        //Категория
        List<Category> parentCategories = Category.getParentCategoriesWithoutEmpty(mTypeOperations);
        for (int i = 0; i < parentCategories.size(); i++) {
            if(mDebug)
                Log.d(LOG_TAG, "    parent  = " + parentCategories.get(i).getName());
            if (parentCategories.get(i).getId() == parent.getId()) {
                if(mDebug)
                    Log.d(LOG_TAG, "    parent position = " + i);
                mSpParent.setSelection(i, true);
                break;
            }
        }
    }

    private void save(){
        if(mDebug){
            Log.d(LOG_TAG, "'save()' ");
        }
        switch (getSaveAs()){
            case Category.CREATE_PARENT:
                if(mDebug)
                    Log.d(LOG_TAG, "    CREATE_PARENT");
                createParent();
                break;
            case Category.EDIT_PARENT :
                if(mDebug)
                    Log.d(LOG_TAG, "    EDIT_PARENT");
                updateParent();
                break;
            case Category.CREATE_CHILD:
                if(mDebug)
                    Log.d(LOG_TAG, "    CREATE_CHILD");
                createChild();
                break;
            case Category.EDIT_CHILD:
                if(mDebug)
                    Log.d(LOG_TAG, "    EDIT_CHILD");
                updateChild();
                break;
        }
    }

    private int getSaveAs() {
        if(mDebug){
            Log.d(LOG_TAG, "'getSaveAs()' ");
        }
        boolean isChangingHierarchy = isChangingHierarchy();
        if (mOpenAs == Category.CREATE_CATEGORY)
            return mHierarchy == Category.CHILD ? Category.CREATE_CHILD : Category.CREATE_PARENT;
        else if(isChangingHierarchy && mHierarchyOld == Category.PARENT)
            return Category.EDIT_CHILD;
        else if(isChangingHierarchy && mHierarchyOld == Category.CHILD)
            return Category.EDIT_PARENT;
        else
            return mOpenAs;
    }

    private void createParent() {
        if(mDebug){
            Log.d(LOG_TAG, "'createParent()' ");
        }
        mResult = RESULT_CREATE_PARENT;
        saveAsParent(new Category());
    }

    private void updateParent(){
        if(mDebug) {
            Log.d(LOG_TAG, "'updateParent()' ");
        }
        mResult = RESULT_UPDATE_PARENT;
        saveAsParent(Category.getById((mExtras.getLong(Constants.CATEGORY_ID))));
    }

    private void createChild(){
        if(mDebug) {
            Log.d(LOG_TAG, "'createChild()' ");
        }
        mResult = RESULT_CREATE_CHILD;
        saveAsChild(new Category());
    }

    private void updateChild(){
        if(mDebug) {
            Log.d(LOG_TAG, "'updateChild()' ");
        }
        mResult = RESULT_UPDATE_CHILD;
        saveAsChild(Category.getById(mExtras.getLong(Constants.CATEGORY_ID)));
    }

    private void saveAsParent(Category parent){
        if(mDebug) {
            Log.d(LOG_TAG, "'saveAsParent()' ");
        }
        //Имя
        String name = String.valueOf(mEtName.getText());
        //Логотип
        Icon icon = Icon.getById(mCurrentIconId);
        //Цвет
        Color color = Color.getById(mCurrentColorId);
        // Проверка условий и сохранение
        if (name == null || name.length() == 0) {
            Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
        }
        // Если создается новая и с таким именем уже существует
        else if (Category.isExistParent(name, mTypeOperations) && !isOpenAsEditor()) {
            if(mDebug) {
                Log.d(LOG_TAG, "if create NEW and exist with current name");
            }
            Toast.makeText(this, R.string.msg_parent_category_exist, Toast.LENGTH_LONG).show();
        }
        // Если меняется тип и с таким именем уже существует
        else if (isChangingHierarchy() && isOpenAsEditor() && Category.isExistParent(name, mTypeOperations)) {
            if(mDebug) {
                Log.d(LOG_TAG, "if change HIERARCHY and exist with current name");
            }
            Toast.makeText(this, R.string.msg_parent_category_exist, Toast.LENGTH_LONG).show();
        }
        else {
            if(mDebug) {
                Log.d(LOG_TAG, "conditions is 'OK'");
            }
            parent.setName(name);
            parent.setType(mTypeOperations);
            parent.setColor(color);
            //Eсли менялся тип устанавливаем пустую категорию
            if(isChangingHierarchy() && isOpenAsEditor()){
                parent.setParent(null);
                mResult = RESULT_CHILD_TO_PARENT;
            }
            parent.setIcon(icon);
            parent.save();
            parent.updateSubs();
            mCategoryId = parent.getId();
            returnResult();
        }
    }

    private void saveAsChild(Category child){
        if(mDebug)
            Log.d(LOG_TAG, "'saveAsChild()' ");
        //Имя
        String name = String.valueOf(mEtName.getText());
        //Родительская категория
        long parentId =
                (long)(mSpParent.getSelectedView()
                .findViewById(R.id.cv_parent_category))
                .getTag(R.string.tag_category_id);
        mParentIdUpd2 = parentId;
        Category parent = Category.getById(parentId);

        // Проверка условий и сохранение
        if (name == null || name.length() == 0) {
            Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
        }
        // Если создается новая и с таким именем уже существует
        else if (parent.isExistChild(name) && !isOpenAsEditor()) {
            Toast.makeText(this, R.string.msg_child_category_exist, Toast.LENGTH_LONG).show();
        }
        // Если меняется тип и с таким именем уже существует
        else if (isChangingHierarchy() && isOpenAsEditor() && parent.isExistChild(name)) {
            Toast.makeText(this, R.string.msg_child_category_exist, Toast.LENGTH_LONG).show();
        }
        else {
            //Eсли менялся тип иерархии
            if(isChangingHierarchy() && isOpenAsEditor()){
                mResult = RESULT_PARENT_TO_CHILD;
                mParentIdUpd1 = parentId;
                child.deleteChilds();
            }
            if(isChangingParent() && isOpenAsEditor()){
                mResult = RESULT_CHILD_CHANGE_PARENT;
            }
            child.setName(name);
            child.setParent(parent);
            child.save();
            mCategoryId = child.getId();
            returnResult();
        }
    }

    private boolean isOpenAsEditor(){
        if(mDebug) {
            Log.d(LOG_TAG, "'isOpenAsEditor()' ");
        }
        return Category.EDIT_CHILD == mOpenAs || mOpenAs == Category.EDIT_PARENT;
    }

    private boolean isChangingHierarchy(){
        if(mDebug) {
            Log.d(LOG_TAG, "'isChangingHierarchy()' ");
        }
        return mHierarchy != mHierarchyOld;
    }

    private boolean isChangingParent(){
        if(mDebug) {
            Log.d(LOG_TAG, "'isChangingParent()' ");
        }
        return 0 != mParentIdUpd1 && mParentIdUpd1 != mParentIdUpd2;
    }


    private void returnResult(){
        if(mDebug){
            Log.d(LOG_TAG, "'returnResult()' ");
        }
        if(RESULT_CREATE_PARENT == mResult || mResult == RESULT_CREATE_CHILD){
            if(mDebug){
                Log.d(LOG_TAG, "            RESULT_CREATE_***");
            }
            returnResult(mResult, mCategoryId, 0, 0);
        }
        else if(RESULT_UPDATE_PARENT == mResult || mResult == RESULT_UPDATE_CHILD) {
            if(mDebug){
                Log.d(LOG_TAG, "            RESULT_UPDATE_***");
            }
            returnResult(mResult, mCategoryId, 0, 0);
        }
        else if(RESULT_CHILD_TO_PARENT == mResult || mResult == RESULT_PARENT_TO_CHILD){
            if(mDebug){
                Log.d(LOG_TAG, "            RESULT_***_TO_***");
            }
            returnResult(mResult, mCategoryId, mParentIdUpd1, 0 );
        }
        else if(RESULT_CHILD_CHANGE_PARENT == mResult){
            if(mDebug){
                Log.d(LOG_TAG, "            RESULT_CHILD_CHANGE_PARENT");
            }
            returnResult(mResult, mCategoryId, mParentIdUpd1, mParentIdUpd2);
        }
    }

    /**
     * Return activity result
     * @param result code of result see AcCreateCategory.class
     * @param categoryId id category for: add, update or update and delete together when
     *                   has been change hierarchy
     * @param parentIdUpd1 id parent category for update after delete child when child becomes
     *                     is parent
     * @param parentIdUpd2 id parent category when become new parent child after change parent.
     */
    private void returnResult(int result, long categoryId, long parentIdUpd1, long parentIdUpd2) {
        if(mDebug){
            Log.d(LOG_TAG, "'returnResult()'");
            Log.d(LOG_TAG, "    result             : " + result);
            Log.d(LOG_TAG, "    categoryId   (name): " + Category.getById(categoryId).getName());
            if(parentIdUpd1 != 0) {
                Log.d(LOG_TAG, "    parentIdUpd1 (name): " + Category.getById(parentIdUpd1).getName());
            }
            if(parentIdUpd2 != 0) {
                Log.d(LOG_TAG, "    parentIdUpd2 (name): " + Category.getById(parentIdUpd2).getName());
            }
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.RESULT, result);
        resultIntent.putExtra(Constants.CATEGORY_ID, categoryId);
        resultIntent.putExtra(Constants.PARENT_ID_UPDATE_1, parentIdUpd1);
        resultIntent.putExtra(Constants.PARENT_ID_UPDATE_2, parentIdUpd2);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onChooseColor(long colorId) {
        if(mDebug)
            Log.d(LOG_TAG, "'onChooseColor()' ");
        Color color = Color.getById(colorId);
        mIvColor.setColorFilter(getResources().getColor(color.getResourceId()));
        mCurrentColorId = colorId;
    }

    @Override
    public void onChooseIcon(long iconId) {
        if(mDebug)
            Log.d(LOG_TAG, "'onChooseIcon()' ");
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
        if(mDebug) {
            Log.d(LOG_TAG, "'setSpParentAdapter()', current mTypeOperations = " + mTypeOperations);
        }
        List<Category> categories = Category.getParentCategoriesWithoutEmpty(mTypeOperations);

        // Если нет родительских категорий, то создать можно только родителя
        if(categories == null || categories.size() == 0) {
            if(mDebug) {
                Log.d(LOG_TAG, "    no parent categories");
            }
            mSpHierarchy.setEnabled(false);
        }
        else {
            mSpHierarchy.setEnabled(true);
        }


        if(mOpenAs == Category.EDIT_PARENT){
            long id = mExtras.getLong(Constants.CATEGORY_ID);
            deleteById(categories, id);
        }

        mSpParent.setAdapter(
                new ListCategoriesSpinnerAdapter(
                        AcCreateCategory.this,
                        categories));
    }

    /**
     * Remove self Category from adapter
     * @param list for adapter
     * @param id to delete
     */
    private void deleteById (List<Category> list, long id){
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == id){
                list.remove(i);
                break;
            }
        }
    }

    private void requestFocus(View view) {
        if(mDebug)
            Log.d(LOG_TAG, "'requestFocus()', " + view);
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



}
