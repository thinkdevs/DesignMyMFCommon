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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCategoryActivity;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class RecyclerViewChildCategoriesAdapter extends
        RecyclerView.Adapter<RecyclerViewChildCategoriesAdapter.SubCategoryViewHolder>
        implements View.OnLongClickListener, DeleteDialogFragment.NoticeDialogListener{

    private List<Category> mCategories;
    private Activity       mContext;
    private Resources      mResources;
    private DialogFragment dialogDelete;
    private int            positionToDelete;
    private long           idToDelete;

    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView tvChildCategoryName;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);
            tvChildCategoryName = (TextView) itemView.findViewById(R.id.tv_sub_category_name);
        }
    }

    public RecyclerViewChildCategoriesAdapter(Activity context, List<Category> subCategories) {
        this.mCategories = subCategories;
        this.mContext    = context;
        this.mResources  = context.getResources();
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

    private void openEditor(long id){
        Intent intent = new Intent(mContext, NewCategoryActivity.class);
        intent.putExtra(Constants.OPEN_AS, Category.EDIT_CHILD);
        intent.putExtra(Constants.CATEGORY_ID, id);
        intent.putExtra(Constants.CATEGORY_HIERARCHY, Category.CHILD);
        mContext.startActivity(intent);
    }

    private void deleteChildCategory(){
        Category categoryToDelete = Category.getById(idToDelete);
        if(!categoryToDelete.deleteCategory()){
            Toast.makeText(
                    mContext, mResources.getString(R.string.msg_can_not_be_removed), Toast.LENGTH_LONG).show();
        }
    }

    private void updateAfterDelete(){
        mCategories.remove(positionToDelete);
        notifyItemRemoved(positionToDelete);
        notifyItemRangeChanged(positionToDelete, getItemCount());
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
                        openEditor(id);
                        return true;
                    case R.id.remove:
                        idToDelete = id;
                        positionToDelete = position;
                        dialogDelete = DeleteDialogFragment.newInstance(
                                RecyclerViewChildCategoriesAdapter.this,
                                mContext.getString(R.string.msg_delete_sub_category));
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
}