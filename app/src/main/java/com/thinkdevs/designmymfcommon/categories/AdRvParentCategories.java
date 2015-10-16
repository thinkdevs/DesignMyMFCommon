package com.thinkdevs.designmymfcommon.categories;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.common.FrDgDelete;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class AdRvParentCategories extends
        RecyclerView.Adapter<AdRvParentCategories.CategoryViewHolder>
        implements View.OnLongClickListener, View.OnClickListener, FrDgDelete.NoticeDialogListener{

    private List<Category> mCategories;
    private Activity       mContext;
    private Fragment       mFragment;
    private Resources      mResources;
    private DialogFragment mDialogDelete;
    private int            mSelectedPosition;
    private long           mSelectedCategoryId;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public CardView    cardView;
        public ImageView   ivCategoryLogo;
        public TextView    tvCategoryName;
        public TextView    tvAmount;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            cardView       = (CardView)    itemView.findViewById(R.id.cv_parent_category);
            ivCategoryLogo = (ImageView)   itemView.findViewById(R.id.iv_category_logo);
            tvCategoryName = (TextView)    itemView.findViewById(R.id.tv_category_name);
            tvAmount       = (TextView)    itemView.findViewById(R.id.tv_amount);
        }
    }

    public AdRvParentCategories(Fragment fragment, List<Category> categories) {
        this.mCategories = categories;
        this.mFragment   = fragment;
        this.mContext    = fragment.getActivity();
        this.mResources  = mContext.getResources();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);
        CategoryViewHolder vh = new CategoryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder viewHolder, final int i) {

        Category category = mCategories.get(i);

        //Сохранение id категории и ее позииции в списке
        viewHolder.cardView.setTag(R.string.tag_category_id, category.getId());
        viewHolder.cardView.setTag(R.string.tag_position_in_rv, i);

        GradientDrawable drawable = (GradientDrawable)viewHolder.ivCategoryLogo.getBackground();
        drawable.setColor(mResources.getColor(category.getColor().getResourceId()));

        //Логотип
        viewHolder.ivCategoryLogo.setImageResource(category.getIcon().getResourceId());
        viewHolder.ivCategoryLogo.setTag(
                R.string.tag_resource_id, category.getIcon().getResourceId());

        //Имя категории
        viewHolder.tvCategoryName.setText(category.getName());

        //Счетчик подкатегорий
        StringBuilder sbCountSubCategories = new StringBuilder();
        sbCountSubCategories.append("( ").append(category.getChilds().size()).append(" )");
        viewHolder.tvAmount.setText(sbCountSubCategories);

        //Установка слушателей
        viewHolder.cardView.setOnLongClickListener(this);
        viewHolder.cardView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    private void openEditor(long id, int position){
        Intent intent = new Intent(mContext, AcCreateCategory.class);
        intent.putExtra(Constants.OPEN_AS, Category.EDIT_PARENT);
        intent.putExtra(Constants.CATEGORY_ID, id);
        intent.putExtra(Constants.CATEGORY_HIERARCHY, Category.PARENT);
        intent.putExtra(Constants.CATEGORY_POSITION, position);
        mFragment.startActivityForResult(intent, FrListCategories.REQUEST_CODE_CHANGE);
}

    private boolean deleteCategory(){
        Category categoryToDelete = Category.getById(mSelectedCategoryId);
        if(!categoryToDelete.deleteCategory()){
            Toast.makeText(
                    mContext, mResources.getString(R.string.msg_can_not_be_removed), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void updateAfterDelete(){
        mCategories.remove(mSelectedPosition);
        notifyItemRemoved(mSelectedPosition);
        notifyItemRangeChanged(mSelectedPosition, getItemCount());
    }

    public void updateAfterChange(long id, int position){
        Category category = Category.getById(id);
        mCategories.remove(position);
        mCategories.add(position, category);
        notifyItemChanged(position);
    }

    public void updateAfterAdd(long id){
        mCategories.add(Category.getById(id));
        notifyItemInserted(getItemCount());
    }

    @Override
    public void onDialogPositiveClick() {
        if(deleteCategory())
            updateAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public boolean onLongClick(final View v) {
        //id категории
        final long id = (long)(v.findViewById(R.id.cv_parent_category).getTag(R.string.tag_category_id));
        //позиция в rv
        final int position = (int)(v.findViewById(R.id.cv_parent_category).getTag(R.string.tag_position_in_rv));
        //меню
        final PopupMenu popupMenu = new PopupMenu(
                mContext, v.findViewById(R.id.tv_category_name));
        popupMenu.inflate(R.menu.menu_popup_parent_categories);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        openEditor(id, position);
                        return true;
                    case R.id.remove:
                        mSelectedCategoryId = id;
                        mSelectedPosition = position;
                        mDialogDelete = FrDgDelete.newInstance(
                                AdRvParentCategories.this,
                                mContext.getString(R.string.msg_delete_parent_category));
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

    @Override
    public void onClick(View v) {
        //id категории
        long idParentCategory = (long)v.findViewById(R.id.cv_parent_category).getTag(R.string.tag_category_id);
        //диалог подкатегорий
        FrDgChildCategories frDgChildCategories =
                FrDgChildCategories.newInstance(idParentCategory);
        frDgChildCategories.show(mContext.getFragmentManager(), "dialog_sub_categories");
    }
}
