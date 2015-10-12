package com.thinkdevs.designmymfcommon.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.ListCategoriesSpinnerAdapter;
import com.thinkdevs.designmymfcommon.adapter.ListColorAdapter;
import com.thinkdevs.designmymfcommon.adapter.ListLogoCategorySpinnerAdapter;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Icon;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.ArrayList;
import java.util.List;

public class NewCategoryActivity extends Activity {

    private RadioGroup mRgHierarchy;
    private TextView   mTvRgType;
    private RadioGroup mRgType;
    private TextView   mTvCategory;
    private Spinner    mSpCategory;
    private EditText   mEtName;
    private TextView   mTvLogo;
    private Spinner    mSpLogo;
    private TextView   mTvColor;
    private Spinner    mSpColor;

    private List<View> mCategoryEditViews;    // Список view для категорий
    private List<View> mSubCategoryEditViews; // Список view для подкатегорий

    private List<Color> mColors;
    private List<Icon> mIcons;

    private Intent mIntent;
    private Bundle mBundle;

    private int mType;
    private int mHierarchy;
    private int mHierarchyOld;
    private int mOpenAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        mIntent = getIntent();
        mBundle = mIntent.getExtras();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        //Тип иерархии
        mRgHierarchy = ((RadioGroup) findViewById(R.id.rg_hierarchy));
        if (mRgHierarchy.getCheckedRadioButtonId() == R.id.rb_parent)
            mHierarchy = Category.PARENT;
        else
            mHierarchy = Category.CHILD;

        //Тип категории
        mTvRgType = ((TextView) findViewById(R.id.textView_type_category));
        mRgType = ((RadioGroup) findViewById(R.id.rg_type));
        if (mRgType.getCheckedRadioButtonId() == R.id.rb_operation_expense)
            mType = Category.EXPENSE;
        else
            mType = Category.PROFIT;

        mTvCategory = ((TextView) findViewById(R.id.tv_category));
        mSpCategory = ((Spinner) findViewById(R.id.sp_category));
        mEtName = ((EditText) findViewById(R.id.et_name));
        mTvLogo = ((TextView) findViewById(R.id.textView_logo));
        mSpLogo = ((Spinner) findViewById(R.id.sp_logo));
        mTvColor = ((TextView) findViewById(R.id.textView_color));
        mSpColor = ((Spinner) findViewById(R.id.sp_color));

        mCategoryEditViews = new ArrayList<>();
        mCategoryEditViews.add(mTvCategory);
        mCategoryEditViews.add(mSpCategory);

        mSubCategoryEditViews = new ArrayList<>();
        mSubCategoryEditViews.add(mTvLogo);
        mSubCategoryEditViews.add(mSpLogo);
        mSubCategoryEditViews.add(mTvColor);
        mSubCategoryEditViews.add(mSpColor);

        mColors = Color.getColors();
        mIcons = Icon.getCategoryIcons();

        mSpCategory.setAdapter(new ListCategoriesSpinnerAdapter(
                NewCategoryActivity.this,
                Category.getParentCategoriesWithoutEmpty(mType)));

        mSpColor.setAdapter(new ListColorAdapter(this, mColors));
        mSpLogo.setAdapter(new ListLogoCategorySpinnerAdapter(this, mIcons));

        mRgHierarchy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_parent:
                        mHierarchy = Category.PARENT;
                        mHideViews(mCategoryEditViews);
                        mShowViews(mSubCategoryEditViews);
                        break;
                    case R.id.rb_sub:
                        mHierarchy = Category.CHILD;
                        mHideViews(mSubCategoryEditViews);
                        mShowViews(mCategoryEditViews);
                        break;
                }
            }
        });

        mRgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_operation_expense:
                        mType = Category.EXPENSE;
                        break;
                    case R.id.rb_operation_profit:
                        mType = Category.PROFIT;
                        break;
                }
                mSpCategory.setAdapter(new ListCategoriesSpinnerAdapter(
                        NewCategoryActivity.this,
                        Category.getParentCategoriesWithoutEmpty(mType)));
            }
        });

        if (mBundle != null) {
            setTitle(mBundle.getString(Constants.ACTIVITY_TITLE));
            mOpenAs = mBundle.getInt(Constants.OPEN_AS);
            if (mIsOpenAsEditor()) {
                setTitle(getResources().getString(R.string.title_activity_category_editing));
                //Скрываем RadioGroup смены типа
                mTvRgType.setVisibility(View.GONE);
                mRgType.setVisibility(View.GONE);
            }
            else
                setTitle(getResources().getString(R.string.title_activity_new_category));
        }

        mOpenAs();
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

        //*****************************Сохранение Категории/Подкатегории***********************
        if (id == R.id.action_save) {
            mSave();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

    private void mCheckRgType (int type){
        if (type == Category.PROFIT)
            mRgType.check(R.id.rb_operation_profit);
        else {
            mRgType.check(R.id.rb_operation_expense);
        }
    }

    private void mOpenAs(){
        Category category = Category.getById(mBundle.getLong(Constants.CATEGORY_ID));
        switch (mOpenAs){
            case Category.EDIT_PARENT :
                mOpenParentEditor(category);
                mHierarchyOld = Category.PARENT;
                break;
            case Category.EDIT_CHILD:
                mOpenChildEditor(category);
                mHierarchyOld = Category.CHILD;
                break;
            case Category.CREATE_CHILD:
                mOpenChildCreator(category);
                mHierarchyOld = Category.CHILD;
                break;
            default:
                mHierarchyOld = Category.PARENT;
        }
    }

    private void mOpenParentEditor(Category parent){
        //Тип иерархии
        mRgHierarchy.check(R.id.rb_parent);
        //Тип категории
        mCheckRgType(parent.getType());
        //Имя
        mEtName.setText(parent.getName());
        //Цвет
        for (int i = 0; i < mColors.size(); i++) {
            if (mColors.get(i).getId() == parent.getColor().getId()) {
                mSpColor.setSelection(i);
                break;
            }
        }
        //Логотип
        for (int i = 0; i < mIcons.size(); i++) {
            if (mIcons.get(i).getId() == parent.getIcon().getId()) {
                mSpLogo.setSelection(i);
                break;
            }
        }
    }

    private void mOpenChildEditor(Category child){
        int type = child.getType();
        List<Category> parentCategories;
        //Тип иерархии
        mRgHierarchy.check(R.id.rb_sub);
        //Имя
        mEtName.setText(child.getName());
        //Тип категории.
        mCheckRgType(type);
        //Категория
        parentCategories = Category.getParentCategoriesWithoutEmpty(type);
        for (int i = 0; i < parentCategories.size(); i++) {
            if (parentCategories.get(i).getId() == child.getParent().getId()) {
                mSpCategory.setSelection(i);
                break;
            }
        }
    }

    private void mOpenChildCreator(Category parent){
        int type = parent.getType();
        List<Category> parentCategories;
        //Тип иерархии
        mRgHierarchy.check(R.id.rb_sub);
        //Тип категории.
        mCheckRgType(type);
        //Категория
        parentCategories = Category.getParentCategoriesWithoutEmpty(type);
        for (int i = 0; i < parentCategories.size(); i++) {
            if (parentCategories.get(i).getId() == parent.getId()) {
                mSpCategory.setSelection(i);
                break;
            }
        }
    }

    private int mGetSaveAs(){
        if(mIsHierarchyChanged() && mOpenAs == Category.EDIT_PARENT)
            return Category.EDIT_CHILD;
        else if (mIsHierarchyChanged() && mOpenAs == Category.EDIT_CHILD)
            return Category.EDIT_PARENT;
        else if(mIsHierarchyChanged() && mOpenAs == Category.CREATE_CHILD)
            return Category.CREATE_PARENT;
        else if(mIsHierarchyChanged() && mOpenAs == Category.CREATE_PARENT)
            return Category.CREATE_CHILD;
        else
            return mOpenAs;
    }

    private void mSave(){
        switch (mGetSaveAs()){
            case Category.CREATE_PARENT:
                mCreateParent();
                break;
            case Category.EDIT_PARENT :
                mEditParent();
                break;
            case Category.CREATE_CHILD:
                mCreateChild();
                break;
            case Category.EDIT_CHILD:
                mEditSub();
                break;
        }
    }

    private void mCreateParent(){
        mSaveUsParent(new Category());
    }

    private void mEditParent(){
        mSaveUsParent(Category.getById((mBundle.getLong(Constants.CATEGORY_ID))));
    }

    private void mCreateChild(){
        mSaveUsChild(new Category());
    }

    private void mEditSub(){
        mSaveUsChild(Category.getById(mBundle.getLong(Constants.CATEGORY_ID)));
    }

    private void mSaveUsParent(Category parent){
        //Имя
        String name = String.valueOf(mEtName.getText());
        //Логотип
        int logoId = ((int)(((ImageView) mSpLogo.getSelectedView().findViewById(R.id.imageView))).getTag());
        Icon icon = Icon.getByResourceId(logoId);
        //Цвет
        int colorId = ((int)(mSpColor.getSelectedView().findViewById(R.id.tv_color)).getTag());
        Color color = Color.getByResourceId(colorId);
        // Проверка условий и сохранение
        if (name == null || name.length() == 0) {
            Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
        } else if (Category.isExistParent(name, mType) && !mIsOpenAsEditor()) {
            Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
        } else {
            parent.setName(name);
            parent.setType(mType);
            parent.setColor(color);
            parent.setIcon(icon);
            parent.save();
            parent.updateSubs();
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    private void mSaveUsChild(Category sub){
        //Имя
        String name = String.valueOf(mEtName.getText());
        //Родительская категория
        long parentId = (long)(mSpCategory.getSelectedView().findViewById(R.id.cv_parent_category)).getTag(R.string.tag_category_id);
        Category parent = Category.getById(parentId);
        // Проверка условий и сохранение
        if (name == null || name.length() == 0)
            Toast.makeText(this, R.string.msg_write_name, Toast.LENGTH_LONG).show();
        else if (parent.isExistSub(name) && !mIsOpenAsEditor())
            Toast.makeText(this, R.string.msg_category_exist, Toast.LENGTH_LONG).show();
        else {
            sub.setName(name);
            sub.setParent(parent);
            sub.save();
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    private boolean mIsHierarchyChanged(){
        return mHierarchyOld != mHierarchy;
    }

    private boolean mIsOpenAsEditor(){
        return Category.EDIT_CHILD == mOpenAs || mOpenAs == Category.EDIT_PARENT;
    }
}
