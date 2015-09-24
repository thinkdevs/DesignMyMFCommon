package com.thinkdevs.designmymfcommon.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCashAccountActivity;
import com.thinkdevs.designmymfcommon.activity.NewOperationActivity;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.dialog.OperationTemplatesDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Formatter;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.List;

public class RecyclerViewCashAccountsAdapter extends
        RecyclerView.Adapter<RecyclerViewCashAccountsAdapter.CashAccountViewHolder>
        implements DeleteDialogFragment.NoticeDialogListener{

    final String LOG_TAG = "mylog";

    private List<CashAccount> mCashAccounts;
    private Activity mContext;
    private Resources mResources;
    private DialogFragment dialogDelete;
    private int positionCashAccountToDelete;
    private String nameCashAccountToDelete;

    public static class CashAccountViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView       cardView;
        public RelativeLayout rlTitleBar;
        public ImageView ivCashAccountLogo;
        public TextView tvCashAccountName;
        public TextView tvCashAccountComment;
        public TextView tvCashAccountAmount;
        public TextView       tvDate;
        public TextView       tvOperation;
        public TextView       tvCurrency;
        public Button         btnAddExpense;
        public Button         btnAddProfit;
        public ImageView      btnMenu;

        public CashAccountViewHolder(View itemView) {
            super(itemView);
            cardView          = (CardView)itemView.findViewById(R.id.cv_cash_account);
            rlTitleBar        = (RelativeLayout)itemView.findViewById(R.id.rl_title_bar);
            ivCashAccountLogo = (ImageView)itemView.findViewById(R.id.iv_category_logo);
            tvCashAccountName = (TextView)itemView.findViewById(R.id.tv_cash_account_name);
            tvCashAccountComment = (TextView)itemView.findViewById(R.id.tv_cash_account_comment);
            tvCashAccountAmount  = (TextView)itemView.findViewById(R.id.tv_amount);
            tvDate        = (TextView)itemView.findViewById(R.id.tv_date);
            tvOperation   = (TextView)itemView.findViewById(R.id.tv_operation);
            tvCurrency    = (TextView)itemView.findViewById(R.id.tv_currency);
            btnAddExpense = (Button)itemView.findViewById(R.id.btn_add_expense);
            btnAddProfit  = (Button)itemView.findViewById(R.id.btn_add_profit);
            btnMenu       = (ImageView)itemView.findViewById(R.id.iv_menu);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewCashAccountsAdapter(Activity context, List<CashAccount> cashAccounts) {
        this.mCashAccounts = cashAccounts;
        this.mContext = context;
        this.mResources = context.getResources();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CashAccountViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cash_account, parent, false);

        CashAccountViewHolder vh = new CashAccountViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CashAccountViewHolder viewHolder, final int i) {

        viewHolder.rlTitleBar.   setBackgroundColor(
                (mResources.getColor(mCashAccounts.get(i).getColor().getResourceId())));
        viewHolder.rlTitleBar.   setTag(
                (mCashAccounts.get(i).getColor().getResourceId()));
        viewHolder.ivCashAccountLogo.setImageResource(
                mCashAccounts.get(i).getLogo().getResourceId());
        viewHolder.ivCashAccountLogo.setTag(
                mCashAccounts.get(i).getLogo().getResourceId());
        viewHolder.tvCashAccountName.setText(
                mCashAccounts.get(i).getName());
        viewHolder.tvCashAccountComment.setText(
                mCashAccounts.get(i).getComment());
        viewHolder.tvCashAccountAmount.setText(String.valueOf(
                mCashAccounts.get(i).getAmount()));
        viewHolder.tvCurrency.setText(
                mCashAccounts.get(i).getCurrency().getStrSymbol());

        Operation lastOperation = mCashAccounts.get(i).getLastOperation();
        if(lastOperation == null){
            viewHolder.tvOperation.setText("операций нет");
            viewHolder.tvDate.setText("");
        }
        else {
            StringBuilder operation  = new StringBuilder();
            if(lastOperation.isExpense())
                operation.append("-").append(lastOperation.getAmount());
            else
                operation.append("+").append(lastOperation.getAmount());

            viewHolder.tvOperation.setText(String.valueOf(operation));
            viewHolder.tvDate.setText(Formatter.formatDateTime(lastOperation.getDate()));
        }


        viewHolder.btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OperationTemplate.getExpenseOperationTemplates()!= null
                        && !OperationTemplate.getExpenseOperationTemplates().isEmpty()) {
                    final OperationTemplatesDialogFragment dialogTemplates
                            = OperationTemplatesDialogFragment.newInstance(
                            OperationTemplate.TYPE_EXPENSE,
                            viewHolder.tvCashAccountName.getText().toString(),
                            RecyclerViewCashAccountsAdapter.this);
                    dialogTemplates.show(mContext.getFragmentManager(), "dialogTemplates");
            }
            else {
                Intent intent = new Intent(mContext, NewOperationActivity.class);
                intent.putExtra(
                        NamesOfParametrs.NAME,
                        viewHolder.tvCashAccountName.getText().toString());
                intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, mContext.getResources().getString(R.string.action_new_operation));
                intent.putExtra(NamesOfParametrs.TYPE, OperationTemplate.TYPE_EXPENSE);
                mContext.startActivity(intent);
            }
            }
        });

        viewHolder.btnAddProfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OperationTemplate.getProfitOperationTemplates() != null
                        && !OperationTemplate.getProfitOperationTemplates().isEmpty() ) {
                    final OperationTemplatesDialogFragment dialogTemplates
                            = OperationTemplatesDialogFragment.newInstance(
                            OperationTemplate.TYPE_PROFIT,
                            viewHolder.tvCashAccountName.getText().toString(),
                            RecyclerViewCashAccountsAdapter.this);
                    dialogTemplates.show(mContext.getFragmentManager(), "dialogTemplates");
                }
                else {
                    Intent intent = new Intent(mContext, NewOperationActivity.class);
                    intent.putExtra(
                            NamesOfParametrs.NAME,
                            viewHolder.tvCashAccountName.getText().toString());
                    intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, mContext.getResources().getString(R.string.action_new_operation));
                    intent.putExtra(NamesOfParametrs.TYPE, OperationTemplate.TYPE_PROFIT);
                    mContext.startActivity(intent);
                }
            }
        });


        viewHolder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(mContext, v, Gravity.START);
                popupMenu.inflate(R.menu.card_account_edit_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                startEditor(viewHolder);
                                return true;
                            case R.id.remove:
                                dialogDelete = DeleteDialogFragment.newInstance(
                                        RecyclerViewCashAccountsAdapter.this,
                                        "При удалении счета будут удалены все операции");
                                nameCashAccountToDelete =
                                        viewHolder.tvCashAccountName.getText().toString();
                                positionCashAccountToDelete = i;
                                dialogDelete.show(mContext.getFragmentManager(), "dialogDelete");
                                Log.d(LOG_TAG, "button remove");
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCashAccounts.size();
    }

    private void startEditor (CashAccountViewHolder viewHolder){
        Intent intent = new Intent(mContext, NewCashAccountActivity.class);
        intent.putExtra(NamesOfParametrs.NAME, viewHolder.tvCashAccountName.getText());
        intent.putExtra(NamesOfParametrs.COMMENT, viewHolder.tvCashAccountComment.getText());
        intent.putExtra(NamesOfParametrs.CURRENCY_STR_SYMBOL, viewHolder.tvCurrency.getText());
        intent.putExtra(NamesOfParametrs.AMOUNT, viewHolder.tvCashAccountAmount.getText());
        intent.putExtra(NamesOfParametrs.LOGO, (int) viewHolder.ivCashAccountLogo.getTag());
        intent.putExtra(NamesOfParametrs.COLOR,         (int) viewHolder.rlTitleBar.getTag());
        intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE,      "Редактирование");
        mContext.startActivity(intent);
    }

    private void deleteCashAccount(){
        CashAccount cashAccount = CashAccount.getCashAccountByName(nameCashAccountToDelete);
        List<Operation> operations = cashAccount.getAllOperations();

        if(!operations.isEmpty()){
            for(Operation operation : operations ){
                operation.delete();
            }
        }
        cashAccount.delete();
    }

    private void updateRecycleViewAfterDelete(){
        mCashAccounts.remove(positionCashAccountToDelete);
        notifyItemRemoved(positionCashAccountToDelete);
        notifyItemRangeChanged(positionCashAccountToDelete, getItemCount());
    }


    @Override
    public void onDialogPositiveClick() {
        deleteCashAccount();
        updateRecycleViewAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

    public void update(){
        mCashAccounts = CashAccount.getCashAccounts();
        this.notifyDataSetChanged();
    }
}
