package com.thinkdevs.designmymfcommon.categories;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.common.FrDgDelete;
import com.thinkdevs.designmymfcommon.samples.AcCreateCategoryTemp;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class AdRvChildCategories extends
        RecyclerView.Adapter<AdRvChildCategories.SubCategoryViewHolder>
        implements View.OnLongClickListener, FrDgDelete.NoticeDialogListener{

    private List<Category> mCategories;
    private Activity       mContext;
    private DialogFragment mFragment;
    private Resources      mResources;
    private DialogFragment mDialogDelete;
    private int            mSelectedPosition;
    private long           mSelectedCategory;

    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvChildCategoryName;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);
            tvChildCategoryName = (TextView) itemView.findViewById(R.id.tv_sub_category_name);
        }
    }

    public AdRvChildCategories(DialogFragment fragment, List<Category> subCategories) {
        this.mCategories = subCategories;
        this.mFragment   = fragment;
        this.mContext    = fragment.getActivity();
        this.mResources  = mContext.getResources();
    }

    @Override
    public SubCategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_view_child_category, parent, false);

        SubCategoryViewHolder vh = new SubCategoryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SubCategoryViewHolder viewHolder, final int i) {

        Category category = mCategories.get(i);

        //Сохранение id категории и ее позииции в списке
        viewHolder.tvChildCategoryName.setTag(R.string.tag_category_id, category.getId());
        viewHolder.tvChildCategoryName.setTag(R.string.tag_position_in_rv, i);

        //Имя категории
        viewHolder.tvChildCategoryName.setText(category.getName());

        //Установка слушателей
        viewHolder.tvChildCategoryName.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    private void openEditor(long id, int position){
        Intent intent = new Intent(mContext, AcCreateCategoryTemp.class);
        intent.putExtra(Constants.OPEN_AS, Category.EDIT_CHILD);
        intent.putExtra(Constants.CATEGORY_ID, id);
        intent.putExtra(Constants.CATEGORY_HIERARCHY, Category.CHILD);
        intent.putExtra(Constants.CATEGORY_POSITION, position);
        mFragment.startActivityForResult(intent, FrListCategories.REQUEST_CODE_CHANGE_CHILD);
    }

    private void deleteChildCategory(){
        Category categoryToDelete = Category.getById(mSelectedCategory);
        if(!categoryToDelete.deleteCategory()){
            Toast.makeText(
                    mContext, mResources.getString(R.string.msg_can_not_be_removed), Toast.LENGTH_LONG).show();
        }
    }

    private void updateAfterDelete(){
        mCategories.remove(mSelectedPosition);
        notifyItemRemoved(mSelectedPosition);
        notifyItemRangeChanged(mSelectedPosition, getItemCount());
    }

    public void updateAfterAdd(long id){
        mCategories.add(Category.getById(id));
        notifyItemInserted(getItemCount());
    }

    public void updateAfterChange(long id, int position){
        Category category = Category.getById(id);
        mCategories.remove(position);
        mCategories.add(position, category);
        notifyItemChanged(position);
    }

    @Override
    public void onDialogPositiveClick() {
        deleteChildCategory();
        updateAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public boolean onLongClick(final View v) {
        //id подкатегории
        final long id = (long)(v.findViewById(R.id.tv_sub_category_name).getTag(R.string.tag_category_id));
        //позиция в rv
        final int position = (int)(v.findViewById(R.id.tv_sub_category_name).getTag(R.string.tag_position_in_rv));
        //меню
        final PopupMenu popupMenu = new PopupMenu(
                mContext, v.findViewById(R.id.tv_sub_category_name));
        popupMenu.inflate(R.menu.menu_popup_sub_categories);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        openEditor(id, position);
                        return true;
                    case R.id.remove:
                        mSelectedCategory = id;
                        mSelectedPosition = position;
                        mDialogDelete = FrDgDelete.newInstance(
                                AdRvChildCategories.this,
                                mContext.getString(R.string.msg_delete_sub_category));
                        mDialogDelete.show(mContext.getFragmentManager(), "dialog_delete");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
        return true;
    }
}
