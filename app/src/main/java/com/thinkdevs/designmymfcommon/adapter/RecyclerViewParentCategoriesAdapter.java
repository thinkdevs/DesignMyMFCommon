package com.thinkdevs.designmymfcommon.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCategoryActivity;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.dialog.SubCategoriesDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class RecyclerViewParentCategoriesAdapter extends
        RecyclerView.Adapter<RecyclerViewParentCategoriesAdapter.CategoryViewHolder>
        implements View.OnLongClickListener, View.OnClickListener, DeleteDialogFragment.NoticeDialogListener{

    private List<Category> mCategories;
    private Activity       mContext;
    private Resources      mResources;
    private DialogFragment dialogDelete;
    private int            positionToDelete;
    private long           idToDelete;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public CardView    cardView;
        public FrameLayout flLogo;
        public ImageView   ivCategoryLogo;
        public TextView    tvCategoryName;
        public TextView    tvAmount;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            cardView       = (CardView)    itemView.findViewById(R.id.cv_parent_category);
            flLogo         = (FrameLayout) itemView.findViewById(R.id.fl_logo);
            ivCategoryLogo = (ImageView)   itemView.findViewById(R.id.iv_category_logo);
            tvCategoryName = (TextView)    itemView.findViewById(R.id.tv_category_name);
            tvAmount       = (TextView)    itemView.findViewById(R.id.tv_amount);
        }
    }

    public RecyclerViewParentCategoriesAdapter(Activity context, List<Category> categories) {
        this.mCategories = categories;
        this.mContext    = context;
        this.mResources  = context.getResources();
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
        viewHolder.cardView.setTag(R.string.tag_category_ID, category.getId());
        viewHolder.cardView.setTag(R.string.tag_position_in_rv, i);

        //Цвет фона логотипа
        viewHolder.flLogo.setBackgroundColor(
                (mResources.getColor(category.getColor().getResourceId())));
        viewHolder.flLogo.setTag(
                R.string.tag_resource_ID, (category.getColor().getResourceId()));

        //Логотип
        viewHolder.ivCategoryLogo.setImageResource(category.getLogo().getResourceId());
        viewHolder.ivCategoryLogo.setTag(
                R.string.tag_resource_ID, category.getLogo().getResourceId());

        //Имя категории
        viewHolder.tvCategoryName.setText(category.getName());

        //Счетчик подкатегорий
        StringBuilder sbCountSubCategories = new StringBuilder();
        sbCountSubCategories.append("( ").append(category.getSubCategories().size()).append(" )");
        viewHolder.tvAmount.setText(sbCountSubCategories);

        //Установка слушателей
        viewHolder.cardView.setOnLongClickListener(this);
        viewHolder.cardView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    private void openEditor(long id){
        Intent intent = new Intent(mContext, NewCategoryActivity.class);
        intent.putExtra(Constants.OPEN_AS, Category.EDIT_PARENT);
        intent.putExtra(Constants.CATEGORY_ID, id);
        intent.putExtra(Constants.CATEGORY_HIERARCHY, Category.PARENT);
        intent.putExtra(Constants.ACTIVITY_TITLE, R.string.title_activity_category_editing);
        mContext.startActivity(intent);
}

    private boolean deleteCategory(){
        Category categoryToDelete = Category.getById(idToDelete);
        if(!categoryToDelete.deleteCategory()){
            Toast.makeText(
                    mContext, mResources.getString(R.string.msg_can_not_be_removed), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void updateAfterDelete(){
        mCategories.remove(positionToDelete);
        notifyItemRemoved(positionToDelete);
        notifyItemRangeChanged(positionToDelete, getItemCount());
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
        final long id = (long)(v.findViewById(R.id.cv_parent_category).getTag(R.string.tag_category_ID));
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
                        openEditor(id);
                        return true;
                    case R.id.remove:
                        idToDelete = id;
                        positionToDelete = position;
                        dialogDelete = DeleteDialogFragment.newInstance(
                                RecyclerViewParentCategoriesAdapter.this,
                                mContext.getString(R.string.msg_delete_parent_category));
                        dialogDelete.show(mContext.getFragmentManager(), "dialog_delete");
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
        long idParentCategory = (long)v.findViewById(R.id.cv_parent_category).getTag(R.string.tag_category_ID);
        //диалог подкатегорий
        SubCategoriesDialogFragment subCategoriesDialogFragment =
                SubCategoriesDialogFragment.newInstance(idParentCategory);
        subCategoriesDialogFragment.show(mContext.getFragmentManager(), "dialog_sub_categories");
    }
}
