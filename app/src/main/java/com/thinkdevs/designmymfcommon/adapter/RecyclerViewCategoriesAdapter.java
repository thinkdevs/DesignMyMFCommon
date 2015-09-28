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

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCategoryActivity;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.dialog.SubCategoriesDialogFragment;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.List;

public class RecyclerViewCategoriesAdapter extends
        RecyclerView.Adapter<RecyclerViewCategoriesAdapter.CategoryViewHolder>
        implements DeleteDialogFragment.NoticeDialogListener{

    final String LOG_TAG = "mylog";

    private List<Category> mCategories;
    private Activity mContext;
    private Resources mResources;
    private DialogFragment dialogDelete;
    private int positionCategoryToDelete;
    private long idCategoryToDelete;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView       cardView;
        public FrameLayout    flLogo;
        public ImageView      ivCategoryLogo;
        public TextView       tvCategoryName;
        public TextView       tvAmount;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            cardView       = (CardView)      itemView.findViewById(R.id.cv_category);
            flLogo         = (FrameLayout)   itemView.findViewById(R.id.fl_logo);
            ivCategoryLogo = (ImageView)     itemView.findViewById(R.id.iv_category_logo);
            tvCategoryName = (TextView)      itemView.findViewById(R.id.tv_category_name);
            tvAmount       = (TextView)      itemView.findViewById(R.id.tv_amount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewCategoriesAdapter(
            Activity context,
            List<Category> categories) {
        this.mCategories = categories;
        this.mContext = context;
        this.mResources = context.getResources();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_category, parent, false);

        CategoryViewHolder vh = new CategoryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder viewHolder, final int i) {

        viewHolder.flLogo.   setBackgroundColor(
                (mResources.getColor(mCategories.get(i).getColor().getResourceId())));
        viewHolder.flLogo.   setTag(
                (mCategories.get(i).getColor().getResourceId()));
        viewHolder.ivCategoryLogo.setImageResource(mCategories.get(i).getLogo().getResourceId());
        viewHolder.ivCategoryLogo.setTag(mCategories.get(i).getLogo().getResourceId());
        viewHolder.tvCategoryName.setText(mCategories.get(i).getName());
        viewHolder.tvCategoryName.setTag(mCategories.get(i).getId());
        StringBuilder sbCountSubCategories = new StringBuilder();
        sbCountSubCategories.append("(").append(mCategories.get(i).getSubCategories().size()).append(")");
        viewHolder.tvAmount.setText(sbCountSubCategories);

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(mContext, v.findViewById(R.id.tv_category_name));
                popupMenu.inflate(R.menu.card_account_edit_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                startEditor(v);
                                return true;
                            case R.id.remove:
                                dialogDelete = DeleteDialogFragment.newInstance(RecyclerViewCategoriesAdapter.this, "При удалении будут удаленны все подкатегории, операциям данных категорий будет присвоена категория прочее");
                                idCategoryToDelete   = (long)(v.findViewById(R.id.tv_category_name).getTag());
                                positionCategoryToDelete = i;
                                dialogDelete.show(mContext.getFragmentManager(), "dialogDelete");
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return true;
            }
        };

        viewHolder.cardView.setOnLongClickListener(longClickListener);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем кошелек
                long idCategory = (long)v.findViewById(R.id.tv_category_name).getTag();
                // Получаем шаблон
                SubCategoriesDialogFragment subCategoriesDialogFragment =
                        SubCategoriesDialogFragment.newInstance(idCategory);
                subCategoriesDialogFragment.show(mContext.getFragmentManager(), "subCategories dialog");
            }
        };

        viewHolder.cardView.setOnClickListener(clickListener);

    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    private void startEditor (View view){
        Intent intent = new Intent(mContext, NewCategoryActivity.class);
        intent.putExtra(NamesOfParametrs.IS_NEW, false);
        intent.putExtra(NamesOfParametrs.IS_SUB_CATEGORY, false);
        long categoryId = (long)view.findViewById(R.id.tv_category_name).getTag();
        intent.putExtra(NamesOfParametrs.CATEGORY_ID, categoryId);
        intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, "Редактирование");
        mContext.startActivity(intent);
    }

    private void deleteOperationTemplate(){
        Category category;
        category = Category.getCategoryById(idCategoryToDelete);
        category.delete();
    }

    private void updateRecycleViewAfterDelete(){
        mCategories.remove(positionCategoryToDelete);
        notifyItemRemoved(positionCategoryToDelete);
        notifyItemRangeChanged(positionCategoryToDelete, getItemCount());
    }


    @Override
    public void onDialogPositiveClick() {
        deleteOperationTemplate();
        updateRecycleViewAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

}