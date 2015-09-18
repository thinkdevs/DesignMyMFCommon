package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Cash;
import com.thinkdevs.designmymfcommon.database.Cash$Table;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.OperationFavorite;
import com.thinkdevs.designmymfcommon.database.ProfitFavorite;
import com.thinkdevs.designmymfcommon.fragment.DialogDelete;

import java.util.ArrayList;
import java.util.List;

public class OperationFavoritesRecyclerViewAdapter extends
        RecyclerView.Adapter<OperationFavoritesRecyclerViewAdapter.OperationFavoriteViewHolder>
        implements DialogDelete.NoticeDialogListener{

    final String LOG_TAG = "mylog";

    private List<OperationFavorite> mOperationFavorites;
    private Activity mContext;
    private Resources mResources;
    private DialogFragment dialogDelete;
    private int positionCashToDelete;
    private String titleCashToDelete;

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
    public OperationFavoritesRecyclerViewAdapter(Activity context, List<OperationFavorite> cashAccounts) {
        this.mOperationFavorites = cashAccounts;
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
                (mResources.getColor(mOperationFavorites.get(i).getSubCategory().getCategory().getColor().getResourceId())));
        viewHolder.flLogo.   setTag(
                (mOperationFavorites.get(i).getSubCategory().getCategory().getColor().getResourceId()));
        viewHolder.ivCategoryLogo.setImageResource(mOperationFavorites.get(i).getSubCategory().getCategory().getLogo().getResourceId());
        viewHolder.ivCategoryLogo.setTag(mOperationFavorites.get(i).getSubCategory().getCategory().getLogo().getResourceId());
        viewHolder.tvTemplateName.setText(mOperationFavorites.get(i).getTitle());
        viewHolder.tvCategoryName.setText(mOperationFavorites.get(i).getSubCategory().getName());

            StringBuilder sbAmount  = new StringBuilder();
            if(mOperationFavorites.get(i).getSubCategory() instanceof ProfitFavorite) {
                sbAmount.append("+").append(mOperationFavorites.get(i).getAmount());
                viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else {
                sbAmount.append("+").append(mOperationFavorites.get(i).getAmount());
                viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        viewHolder.tvAmount.setText(sbAmount.toString());
    }

    @Override
    public int getItemCount() {
        return mOperationFavorites.size();
    }

    private void startEditor (OperationFavoriteViewHolder viewHolder){
//        Intent intent = new Intent(mContext, NewCashAccountActivity.class);
//        intent.putExtra(NamesOfParametrs.CASH_TITLE, viewHolder.tvTemplateName.getText());
//        intent.putExtra(NamesOfParametrs.CASH_TYPE, viewHolder.tvCategoryName.getText());
//        intent.putExtra(NamesOfParametrs.CASH_CURRENCY_SHORT_HAND, viewHolder.tvCurrency.getText());
//        intent.putExtra(NamesOfParametrs.AMOUNT, viewHolder.tvAmount.getText());
//        intent.putExtra(NamesOfParametrs.CASH_LOGO, (int) viewHolder.ivCategoryLogo.getTag());
//        intent.putExtra(NamesOfParametrs.CASH_COLOR, (int) viewHolder.rlTitleBar.getTag());
//        intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, "Редактирование");
//        mContext.startActivity(intent);
    }

    private void deleteCashAccount(){
        Cash cash = new Select().from(Cash.class).
                where(Condition.column(Cash$Table.NAME)
                        .is(titleCashToDelete))
                .querySingle();
        List<Operation> operations = new ArrayList<>();
        operations.addAll(cash.getProfits());
        operations.addAll(cash.getExpenses());

        if(!operations.isEmpty()){
            for(Operation operation : operations ){
                operation.delete();
            }
        }
        cash.delete();
    }

    private void updateRecycleViewAfterDelete(){
        mOperationFavorites.remove(positionCashToDelete);
        notifyItemRemoved(positionCashToDelete);
        notifyItemRangeChanged(positionCashToDelete, getItemCount());
    }


    @Override
    public void onDialogPositiveClick() {
        deleteCashAccount();
        updateRecycleViewAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }
}
