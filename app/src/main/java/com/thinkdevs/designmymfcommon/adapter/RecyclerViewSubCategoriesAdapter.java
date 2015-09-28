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

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCategoryActivity;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.List;

public class RecyclerViewSubCategoriesAdapter extends
        RecyclerView.Adapter<RecyclerViewSubCategoriesAdapter.SubCategoryViewHolder>
        implements DeleteDialogFragment.NoticeDialogListener{

    final String LOG_TAG = "mylog";

    private List<SubCategory> mSubCategories;
    private Activity mContext;
    private Resources mResources;
    private DialogFragment dialogDelete;
    private int positionSubCategoryToDelete;
    private String nameSubCategoryToDelete;
    private boolean typeOperationTemplateToDelete;
    private long idSubCategoryToDelete;

    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView       cardView;
        public TextView       tvSubCategoryName;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);
            cardView          = (CardView) itemView.findViewById(R.id.cv_subCategory);
            tvSubCategoryName = (TextView) itemView.findViewById(R.id.tv_subCategory_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewSubCategoriesAdapter(
            Activity context,
            List<SubCategory> subCategories) {
        this.mSubCategories = subCategories;
        this.mContext = context;
        this.mResources = context.getResources();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SubCategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_sub_category, parent, false);

        SubCategoryViewHolder vh = new SubCategoryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SubCategoryViewHolder viewHolder, final int i) {

        viewHolder.tvSubCategoryName.setText(mSubCategories.get(i).getName());
        viewHolder.tvSubCategoryName.setTag(mSubCategories.get(i).getId());
        viewHolder.tvSubCategoryName.setTextColor(
                (mResources.getColor(mSubCategories.get(i).getCategory().getColor().getResourceId())));

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(mContext, v.findViewById(R.id.tv_subCategory_name));
                popupMenu.inflate(R.menu.card_account_edit_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                startEditor(v);
                                return true;
                            case R.id.remove:
                                dialogDelete = DeleteDialogFragment.newInstance(RecyclerViewSubCategoriesAdapter.this, "При удалении, операциям будет присвоена категория прочее");
                                idSubCategoryToDelete = (long)(v.findViewById(R.id.tv_subCategory_name).getTag());
                                positionSubCategoryToDelete = i;
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
    }

    @Override
    public int getItemCount() {
        return mSubCategories.size();
    }

    private void startEditor (View view){
        Intent intent = new Intent(mContext, NewCategoryActivity.class);
        intent.putExtra(NamesOfParametrs.IS_NEW, false);
        long subCategoryId = (long)view.findViewById(R.id.tv_subCategory_name).getTag();
        intent.putExtra(NamesOfParametrs.IS_SUB_CATEGORY, true);
        intent.putExtra(NamesOfParametrs.SUB_CATEGORY_ID, subCategoryId);
        intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, "Редактирование");
        mContext.startActivity(intent);
    }

    private void deleteSubCategory(){
        SubCategory subCategory;
        subCategory = SubCategory.getSubCategoryById(idSubCategoryToDelete);
        subCategory.delete();
    }

    private void updateRecycleViewAfterDelete(){
        mSubCategories.remove(positionSubCategoryToDelete);
        notifyItemRemoved(positionSubCategoryToDelete);
        notifyItemRangeChanged(positionSubCategoryToDelete, getItemCount());
    }


    @Override
    public void onDialogPositiveClick() {
        deleteSubCategory();
        updateRecycleViewAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

}
