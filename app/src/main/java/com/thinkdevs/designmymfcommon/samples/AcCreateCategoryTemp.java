package com.thinkdevs.designmymfcommon.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

    private List<View> mCreateParentViews = new ArrayList<>(); //Список view создания родительской
    private List<View> mCreateChildViews  = new ArrayList<>(); //Список view создания дочерней
    private List<View> mEditParentViews   = new ArrayList<>(); //Список view для категорий
    private List<View> mEditChildViews    = new ArrayList<>(); //Список view для подкатегорий

    private final int INDEX_EXPENSES = 0;
    private final int INDEX_PROFITS  = 1;
    private final int INDEX_ROOT     = 0;
    private final int INDEX_INSERTED = 1;

    private List<Color> mColors;
    private List<Icon> mIcons;

    private Intent mIntent;
    private Bundle mBundle;

    private int mOperations = Category.EXPENSE;
    private int mHierarchy  = Category.PARENT;
    private int mHierarchyOld;
    private int mOpenAs;

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

        mIcons = Icon.getCashAccountIcons();
        Icon defaultIcon = mIcons.get(0);
        mCurrentIconId   = defaultIcon.getId();
        mIvIcon.setImageResource(defaultIcon.getResourceId());

        mColors = Color.getColorsWithoutSystems();
        Color defaultColor = mColors.get(0);
        mCurrentColorId    = defaultColor.getId();
        mIvColor.setColorFilter(getResources().getColor(defaultColor.getResourceId()));


        mCreateParentViews.add(findViewById(R.id.llDecoration));
        mCreateChildViews.add(findViewById(R.id.llParent));

        mEditParentViews = new ArrayList<>();
        mEditParentViews.add(findViewById(R.id.llOperations));
        mEditParentViews.add(findViewById(R.id.llHierarchy));
        mEditParentViews.add(findViewById(R.id.llParent));

        mEditChildViews = new ArrayList<>();
        mEditChildViews.add(findViewById(R.id.llOperations));
        mEditChildViews.add(findViewById(R.id.llHierarchy));
        mEditChildViews.add(findViewById(R.id.llDecoration));

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
                Log.d("test_test", String.valueOf(position));
                switch (position) {
                    case INDEX_ROOT:
                        mHierarchy = Category.PARENT;
                        mHideViews(mCreateChildViews);
                        mShowViews(mCreateParentViews);
                        break;
                    case INDEX_INSERTED:
                        mHierarchy = Category.CHILD;
                        mHideViews(mCreateParentViews);
                        mShowViews(mCreateChildViews);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        if (mBundle != null) {
//            setTitle(mBundle.getString(Constants.ACTIVITY_TITLE));
//            mOpenAs = mBundle.getInt(Constants.OPEN_AS);
//            if (mIsOpenAsEditor()) {
//                setTitle(getResources().getString(R.string.title_activity_category_editing));
//                //Скрываем RadioGroup смены типа
//                mTvRgType.setVisibility(View.GONE);
//                mRgType.setVisibility(View.GONE);
//            }
//            else
//                setTitle(getResources().getString(R.string.title_activity_new_category));
//        }
//
//        mOpenAs();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_new_category, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in d.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        if (id == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
//            return true;
//        }
//
//        if (id == R.id.action_save) {
//            mSave();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
    private void mHideViews(List<View> views){
        for(View view : views){
            view.setVisibility(View.GONE);
        }
    }

    private void mShowViews(List<View> views){
        for(View view : views){
            view.setVisibility(View.VISIBLE);
        }
    }
//
//    private void mCheckRgType (int type){
//        if (type == Category.PROFIT)
//            mRgType.check(R.id.rb_operation_profit);
//        else {
//            mRgType.check(R.id.rb_operation_expense);
//        }
//    }
//
//    private void mOpenAs(){
//        Category category;
//        if(mBundle.containsKey(Constants.CATEGORY_ID)) {
//           category = Category.getById(mBundle.getLong(Constants.CATEGORY_ID));
//        }
//        else
//           category = new Category();
//
//        switch (mOpenAs){
//            case Category.EDIT_PARENT :
//                mOpenParentEditor(category);
//                mHierarchyOld = Category.PARENT;
//                break;
//            case Category.EDIT_CHILD:
//                mOpenChildEditor(category);
//                mHierarchyOld = Category.CHILD;
//                break;
//            case Category.CREATE_CHILD:
//                mOpenChildCreator(category);
//                mHierarchyOld = Category.CHILD;
//                break;
//            default:
//                mHierarchyOld = Category.PARENT;
//        }
//    }
//
//    private void mOpenParentEditor(Category parent){
//        //Тип иерархии
//        mRgHierarchy.check(R.id.rb_parent);
//        //Тип категории
//        mCheckRgType(parent.getType());
//        //Имя
//        mEtName.setText(parent.getName());
//        //Цвет
//        for (int i = 0; i < mColors.size(); i++) {
//            if (mColors.get(i).getId() == parent.getColor().getId()) {
//                mSpColor.setSelection(i);
//                break;
//            }
//        }
//        //Логотип
//        for (int i = 0; i < mIcons.size(); i++) {
//            if (mIcons.get(i).getId() == parent.getIcon().getId()) {
//                mSpLogo.setSelection(i);
//                break;
//            }
//        }
//    }
//
//    private void mOpenChildEditor(Category child){
//        int type = child.getType();
//        List<Category> parentCategories;
//        //Тип иерархии
//        mRgHierarchy.check(R.id.rb_sub);
//        //Имя
//        mEtName.setText(child.getName());
//        //Тип категории.
//        mCheckRgType(type);
//        //Категория
//        parentCategories = Category.getParentCategoriesWithoutEmpty(type);
//        for (int i = 0; i < parentCategories.size(); i++) {
//            if (parentCategories.get(i).getId() == child.getParent().getId()) {
//                mSpCategory.setSelection(i);
//                break;
//            }
//        }
//    }
//
//    private void mOpenChildCreator(Category parent){
//        int type = parent.getType();
//        List<Category> parentCategories;
//        //Тип иерархии
//        mRgHierarchy.check(R.id.rb_sub);
//        //Тип категории.
//        mCheckRgType(type);
//        //Категория
//        parentCategories = Category.getParentCategoriesWithoutEmpty(type);
//        for (int i = 0; i < parentCategories.size(); i++) {
//            if (parentCategories.get(i).getId() == parent.getId()) {
//                mSpCategory.setSelection(i);
//                break;
//            }
//        }
//    }
//
//    private int mGetSaveAs(){
//        if(mIsHierarchyChanged() && mOpenAs == Category.EDIT_PARENT)
//            return Category.EDIT_CHILD;
//        else if (mIsHierarchyChanged() && mOpenAs == Category.EDIT_CHILD)
//            return Category.EDIT_PARENT;
//        else if(mIsHierarchyChanged() && mOpenAs == Category.CREATE_CHILD)
//            return Category.CREATE_PARENT;
//        else if(mIsHierarchyChanged() && mOpenAs == Category.CREATE_PARENT)
//            return Category.CREATE_CHILD;
//        else
//            return mOpenAs;
//    }
//
//    private void mSave(){
//        switch (mGetSaveAs()){
//            case Category.CREATE_PARENT:
//                mCreateParent();
//                break;
//            case Category.EDIT_PARENT :
//                mEditParent();
//                break;
//            case Category.CREATE_CHILD:
//                mCreateChild();
//                break;
//            case Category.EDIT_CHILD:
//                mEditSub();
//                break;
//        }
//    }
//
//    private void mCreateParent(){
//        mSaveUsParent(new Category());
//    }
//
//    private void mEditParent(){
//        mSaveUsParent(Category.getById((mBundle.getLong(Constants.CATEGORY_ID))));
//    }
//
//    private void mCreateChild(){
//        mSaveUsChild(new Category());
//    }
//
//    private void mEditSub(){
//        mSaveUsChild(Category.getById(mBundle.getLong(Constants.CATEGORY_ID)));
//    }
//
//    private void mSaveUsParent(Category parent){
//        //Имя
//        String name = String.valueOf(mEtName.getText());
//        //Логотип
//        int logoId = ((int)(((ImageView) mSpLogo.getSelectedView().findViewById(R.id.imageView))).getTag());
//        Icon icon = Icon.getByResourceId(logoId);
//        //Цвет
//        int colorId = ((int)(mSpColor.getSelectedView().findViewById(R.id.tv_color)).getTag());
//        Color color = Color.getByResourceId(colorId);
//        // Проверка условий и сохранение
//        if (name == null || name.length() == 0) {
//            Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
//        } else if (Category.isExistParent(name, mOperations) && !mIsOpenAsEditor()) {
//            Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
//        } else {
//            parent.setName(name);
//            parent.setType(mOperations);
//            parent.setColor(color);
//            parent.setIcon(icon);
//            parent.save();
//            parent.updateSubs();
////            NavUtils.navigateUpFromSameTask(this);
//            returnResult(parent.getId());
//        }
//    }
//
//    private void mSaveUsChild(Category sub){
//        //Имя
//        String name = String.valueOf(mEtName.getText());
//        //Родительская категория
//        long parentId = (long)(mSpParent.getSelectedView().findViewById(R.id.cv_parent_category)).getTag(R.string.tag_category_id);
//        Category parent = Category.getById(parentId);
//        // Проверка условий и сохранение
//        if (name == null || name.length() == 0)
//            Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
//        else if (parent.isExistSub(name) && !mIsOpenAsEditor())
//            Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
//        else {
//            sub.setName(name);
//            sub.setParent(parent);
//            sub.save();
//
//            returnResult(sub.getId());
//        }
//    }
//
//    private boolean mIsHierarchyChanged(){
//        return mHierarchyOld != mHierarchy;
//    }
//
//    private boolean mIsOpenAsEditor(){
//        return Category.EDIT_CHILD == mOpenAs || mOpenAs == Category.EDIT_PARENT;
//    }
//
//    private void returnResult (long id){
//        Intent result = new Intent();
//        result.putExtra(Constants.CATEGORY_ID, id);
//        result.putExtra(Constants.CATEGORY_POSITION, mBundle.getInt(Constants.CATEGORY_POSITION));
//        setResult(RESULT_OK, result);
//        finish();
//    }

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
                FrDgDecorChooseIcon.newInstance(this, mCurrentIconId)
                        .show(getFragmentManager(), "chooseDecorIcon");
                break;
        }
    }

    /**
     * Update adapter after change mOperations
     */
    private void setSpParentAdapter(){
        mSpParent.setAdapter(
                new ListCategoriesSpinnerAdapter(
                        AcCreateCategoryTemp.this,
                        Category.getParentCategoriesWithoutEmpty(mOperations)));
    }

}
