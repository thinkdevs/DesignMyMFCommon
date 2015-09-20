package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewOperationTemplateActivity;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.fragment.DialogDelete;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.List;

public class OperationFavoritesRecyclerViewAdapter extends
        RecyclerView.Adapter<OperationFavoritesRecyclerViewAdapter.OperationFavoriteViewHolder>
        implements DialogDelete.NoticeDialogListener{

    final String LOG_TAG = "mylog";

    private List<OperationTemplate> mOperationTemplate;
    private Activity mContext;
    private Resources mResources;
    private DialogFragment dialogDelete;
    private int positionOperationFavoriteToDelete;
    private String titleOperationFavoriteToDelete;
    private boolean typeOperationTemplateToDelete;

    public static class OperationFavoriteViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView       cardView;
        public FrameLayout    flLogo;
        public ImageView      ivCategoryLogo;
        public TextView       tvTemplateName;
        public TextView       tvCategoryName;
        public TextView       tvAmount;

        public OperationFavoriteViewHolder(View itemView) {
            super(itemView);
            cardView       = (CardView)      itemView.findViewById(R.id.cv_cash_account);
            flLogo         = (FrameLayout)   itemView.findViewById(R.id.fl_logo);
            ivCategoryLogo = (ImageView)     itemView.findViewById(R.id.iv_category_logo);
            tvTemplateName = (TextView)      itemView.findViewById(R.id.tv_template_name);
            tvCategoryName = (TextView)      itemView.findViewById(R.id.tv_category_name);
            tvAmount       = (TextView)      itemView.findViewById(R.id.tv_amount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OperationFavoritesRecyclerViewAdapter(Activity context, List<OperationTemplate> cashAccounts) {
        this.mOperationTemplate = cashAccounts;
        this.mContext = context;
        this.mResources = context.getResources();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OperationFavoriteViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_operation_favorit, parent, false);

        OperationFavoriteViewHolder vh = new OperationFavoriteViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OperationFavoriteViewHolder viewHolder, final int i) {

        viewHolder.flLogo.   setBackgroundColor(
                (mResources.getColor(mOperationTemplate.get(i).getSubCategory().getCategory().getColor().getResourceId())));
        viewHolder.flLogo.   setTag(
                (mOperationTemplate.get(i).getSubCategory().getCategory().getColor().getResourceId()));
        viewHolder.ivCategoryLogo.setImageResource(mOperationTemplate.get(i).getSubCategory().getCategory().getLogo().getResourceId());
        viewHolder.ivCategoryLogo.setTag(mOperationTemplate.get(i).getSubCategory().getCategory().getLogo().getResourceId());
        viewHolder.tvTemplateName.setText(mOperationTemplate.get(i).getTitle());
        viewHolder.tvCategoryName.setText(mOperationTemplate.get(i).getSubCategory().getName());

            StringBuilder sbAmount  = new StringBuilder();
            if(mOperationTemplate.get(i).getSubCategory().getCategory().getType().equals(OperationTemplate.TYPE_EXPENSE)) {
                sbAmount.append("-").append(mOperationTemplate.get(i).getAmount());
                viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else {
                sbAmount.append("+").append(mOperationTemplate.get(i).getAmount());
                viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        viewHolder.tvAmount.setText(sbAmount.toString());

        View.OnLongClickListener listener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(mContext, v.findViewById(R.id.tv_template_name));
                popupMenu.inflate(R.menu.card_account_edit_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                startEditor(v);
                                return true;
                            case R.id.remove:
                                dialogDelete = DialogDelete.newInstance(OperationFavoritesRecyclerViewAdapter.this, "");
                                titleOperationFavoriteToDelete = ((TextView)v.findViewById(R.id.tv_template_name)).getText().toString();
                                String subCategoryName = ((TextView) v.findViewById(R.id.tv_category_name)).getText().toString();
                                Log.d(LOG_TAG, subCategoryName);
                                typeOperationTemplateToDelete = (SubCategory.getExpenseSubCategoryByTitle(subCategoryName) != null);
                                Log.d(LOG_TAG, String.valueOf(typeOperationTemplateToDelete));
                                positionOperationFavoriteToDelete = i;
                                dialogDelete.show(mContext.getFragmentManager(), "dialogDelete");
                                Log.d(LOG_TAG, "button remove");
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

        viewHolder.cardView.setOnLongClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mOperationTemplate.size();
    }

    private void startEditor (View view){
        Intent intent = new Intent(mContext, NewOperationTemplateActivity.class);
        intent.putExtra(NamesOfParametrs.TITLE, ((TextView) view.findViewById(R.id.tv_template_name)).getText());
        String subCategoryName = ((TextView) view.findViewById(R.id.tv_category_name)).getText().toString();
        Log.d(LOG_TAG, subCategoryName + " startEditor");
        intent.putExtra(NamesOfParametrs.NAME_CATEGORY, subCategoryName);
        boolean typeOperation = (SubCategory.getExpenseSubCategoryByTitle(subCategoryName) != null);
        intent.putExtra(NamesOfParametrs.TYPE, typeOperation);
        intent.putExtra(NamesOfParametrs.AMOUNT, ((TextView) view.findViewById(R.id.tv_amount)).getText().toString().substring(1));
        intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, "Редактирование");
        mContext.startActivity(intent);
    }

    private void deleteFavoriteOperation(){
        OperationTemplate operationTemplate;
        operationTemplate = OperationTemplate.getOperationTemplateByTitle(titleOperationFavoriteToDelete);
        operationTemplate.delete();
    }

    private void updateRecycleViewAfterDelete(){
        mOperationTemplate.remove(positionOperationFavoriteToDelete);
        notifyItemRemoved(positionOperationFavoriteToDelete);
        notifyItemRangeChanged(positionOperationFavoriteToDelete, getItemCount());
    }


    @Override
    public void onDialogPositiveClick() {
        deleteFavoriteOperation();
        updateRecycleViewAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }
}
