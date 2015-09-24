package com.thinkdevs.designmymfcommon.adapter;

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

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewOperationActivity;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Formatter;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.List;

public class RecyclerViewOperationAdapter extends
        RecyclerView.Adapter<RecyclerViewOperationAdapter.OperationViewHolder>
        implements DeleteDialogFragment.NoticeDialogListener{

    final String LOG_TAG = "mylog";

    private List<Operation> mOperations;
    private Activity mContext;
    private Resources mResources;
    private DialogFragment dialogDelete;
    private int positionOperationToDelete;
    private long timeOperationToDelete;
    private boolean typeOperationToDelete;

    public static class OperationViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView       cardView;
        public FrameLayout    flLogo;
        public ImageView      ivCategoryLogo;
        public TextView       tvCategoryName;
        public TextView       tvCashAccountName;
        public TextView       tvAmount;
        public TextView       tvDate;

        public OperationViewHolder(View itemView) {
            super(itemView);
            cardView          = (CardView)      itemView.findViewById(R.id.cv_operation);
            flLogo            = (FrameLayout)   itemView.findViewById(R.id.fl_logo);
            ivCategoryLogo    = (ImageView)     itemView.findViewById(R.id.iv_category_logo);
            tvCategoryName    = (TextView)      itemView.findViewById(R.id.tv_category_name);
            tvCashAccountName = (TextView)      itemView.findViewById(R.id.tv_cash_account_name);
            tvAmount          = (TextView)      itemView.findViewById(R.id.tv_amount);
            tvDate            = (TextView)      itemView.findViewById(R.id.tv_date);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewOperationAdapter(
            Activity context,
            List<Operation> operations) {
        this.mOperations = operations;
        this.mContext = context;
        this.mResources = context.getResources();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OperationViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_operation, parent, false);

        OperationViewHolder vh = new OperationViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OperationViewHolder viewHolder, final int i) {

        viewHolder.flLogo.setBackgroundColor(
                (mResources.getColor(mOperations.get(i).getSubCategory().getCategory().getColor().getResourceId())));
        viewHolder.flLogo.setTag(
                (mOperations.get(i).getSubCategory().getCategory().getColor().getResourceId()));
        viewHolder.ivCategoryLogo.setImageResource(
                mOperations.get(i).getSubCategory().getCategory().getLogo().getResourceId());
        viewHolder.ivCategoryLogo.setTag(
                mOperations.get(i).getSubCategory().getCategory().getLogo().getResourceId());


        StringBuilder sb = new StringBuilder();
             sb.append(mOperations.get(i).getSubCategory().getCategory().getName()).
                append("/").
                append(mOperations.get(i).getSubCategory().getName());
                viewHolder.tvCategoryName.setText(sb);
        viewHolder.tvCashAccountName.setText(
                mOperations.get(i).getCashAccount().getName());

            StringBuilder sbAmount  = new StringBuilder();
            if(mOperations.get(i).isExpense()) {
                sbAmount.append("-").append(mOperations.get(i).getAmount());
                viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else {
                sbAmount.append("+").append(mOperations.get(i).getAmount());
                viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        viewHolder.tvAmount.setText(sbAmount);
        viewHolder.tvDate.setText(Formatter.formatDateTime(mOperations.get(i).getDate()));
        viewHolder.tvDate.setTag(mOperations.get(i).getDate().getTime());

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
                                dialogDelete = DeleteDialogFragment.newInstance(RecyclerViewOperationAdapter.this, "");
                                timeOperationToDelete = (long)(v.findViewById(R.id.tv_date)).getTag();
                                positionOperationToDelete = i;
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
            viewHolder.cardView.setOnLongClickListener(longClickListener);
    }

    @Override
    public int getItemCount() {
        return mOperations.size();
    }
    private void startEditor (View view){
        Intent intent = new Intent(mContext, NewOperationActivity.class);
        String [] hierarchyCategories = ((TextView) view.findViewById(R.id.tv_category_name)).getText().toString().split("/");
        intent.putExtra(NamesOfParametrs.IS_NEW, false);
        intent.putExtra(NamesOfParametrs.CATEGORY_NAME, hierarchyCategories[0]);
        intent.putExtra(NamesOfParametrs.SUB_CATEGORY_NAME, hierarchyCategories[1]);
        boolean typeOperation = (SubCategory.getExpenseSubCategoryByName(hierarchyCategories[1]) != null);
        intent.putExtra(
                NamesOfParametrs.TYPE, typeOperation ? Operation.TYPE_EXPENSE : Operation.TYPE_PROFIT);
        intent.putExtra(
                NamesOfParametrs.AMOUNT,
                ((TextView) view.findViewById(R.id.tv_amount)).getText().toString());
        intent.putExtra(
                NamesOfParametrs.ACTIVITY_TITLE, "Редактирование");
        intent.putExtra(
                NamesOfParametrs.CASH_ACCOUNT_NAME,
                ((TextView) view.findViewById(R.id.tv_cash_account_name)).getText().toString());
        intent.putExtra(NamesOfParametrs.DATE,
                (long) (view.findViewById(R.id.tv_date)).getTag());
        mContext.startActivity(intent);
    }

    private void deleteOperation(){
        Operation operation;
        operation = Operation.getOperationByTime(timeOperationToDelete);
        CashAccount cashAccount = operation.getCashAccount();
        float cashAccountAmount = cashAccount.getAmount();
        cashAccount.setAmount(operation.isExpense()
                ? cashAccountAmount + operation.getAmount()
                : cashAccountAmount - operation.getAmount());
        cashAccount.update();
        operation.delete();
    }

    private void updateRecycleViewAfterDelete(){
        mOperations.remove(positionOperationToDelete);
        notifyItemRemoved(positionOperationToDelete);
        notifyItemRangeChanged(positionOperationToDelete, getItemCount());
    }

    @Override
    public void onDialogPositiveClick() {
        deleteOperation();
        updateRecycleViewAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

}
