package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCashAccountActivity;
import com.thinkdevs.designmymfcommon.database.Cash;
import com.thinkdevs.designmymfcommon.database.Cash$Table;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.Profit;
import com.thinkdevs.designmymfcommon.fragment.DialogDelete;
import com.thinkdevs.designmymfcommon.utills.Formatter;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.ArrayList;
import java.util.List;

public class CashAccountsRecyclerViewAdapter extends
        RecyclerView.Adapter<CashAccountsRecyclerViewAdapter.CashAccountViewHolder>
        implements DialogDelete.NoticeDialogListener{

    final String LOG_TAG = "mylog";

    private List<Cash> mCashAccounts;
    private Activity mContext;
    private Resources mResources;
    private DialogFragment dialogDelete;
    private int positionCashToDelete;
    private String titleCashToDelete;

    public static class CashAccountViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView       cardView;
        public RelativeLayout rlTitleBar;
        public ImageView      ivAccountLogo;
        public TextView       tvAccountName;
        public TextView       tvAccountType;
        public TextView       tvAmount;
        public TextView       tvDate;
        public TextView       tvOperation;
        public TextView       tvCurrency;
        public Button         btnAddExpense;
        public Button         btnAddProfit;
        public ImageView      btnMenu;

        public CashAccountViewHolder(View itemView) {
            super(itemView);
            cardView      = (CardView)       itemView.findViewById(R.id.cv_cash_account);
            rlTitleBar    = (RelativeLayout) itemView.findViewById(R.id.rl_title_bar);
            ivAccountLogo = (ImageView)      itemView.findViewById(R.id.iv_category_logo);
            tvAccountName = (TextView)       itemView.findViewById(R.id.tv_account_name);
            tvAccountType = (TextView)       itemView.findViewById(R.id.tv_account_type);
            tvAmount      = (TextView)       itemView.findViewById(R.id.tv_amount);
            tvDate        = (TextView)       itemView.findViewById(R.id.tv_date);
            tvOperation   = (TextView)       itemView.findViewById(R.id.tv_operation);
            tvCurrency    = (TextView)       itemView.findViewById(R.id.tv_currency);
            btnAddExpense = (Button)         itemView.findViewById(R.id.btn_add_expense);
            btnAddProfit  = (Button)         itemView.findViewById(R.id.btn_add_profit);
            btnMenu       = (ImageView)      itemView.findViewById(R.id.iv_menu);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CashAccountsRecyclerViewAdapter(Activity context, List<Cash> cashAccounts) {
        this.mCashAccounts = cashAccounts;
        this.mContext = context;
        this.mResources = context.getResources();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CashAccountViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cash_account_new, parent, false);

        CashAccountViewHolder vh = new CashAccountViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CashAccountViewHolder viewHolder, final int i) {

        viewHolder.rlTitleBar.   setBackgroundColor(
                (mResources.getColor(mCashAccounts.get(i).getColor().getResourceId())));
        viewHolder.rlTitleBar.   setTag(
                (mCashAccounts.get(i).getColor().getResourceId()));
        viewHolder.ivAccountLogo.setImageResource(mCashAccounts.get(i).getLogo().getResourceId());
        viewHolder.ivAccountLogo.setTag(mCashAccounts.get(i).getLogo().getResourceId());
        viewHolder.tvAccountName.setText(mCashAccounts.get(i).getName());
        viewHolder.tvAccountType.setText(mCashAccounts.get(i).getType());
        viewHolder.tvAmount.      setText(String.valueOf(mCashAccounts.get(i).getAmount()));
        viewHolder.tvCurrency.   setText(mCashAccounts.get(i).getCurrency().getShortHand());

        Operation lastOperation = mCashAccounts.get(i).getLastOperation();
        if(lastOperation == null){
            viewHolder.tvOperation.setText("операций нет");
            viewHolder.tvDate.setText("");
        }
        else {
            StringBuilder operation  = new StringBuilder();
            if(lastOperation instanceof Profit)
                operation.append("+").append(lastOperation.getAmount());
            else
                operation.append("-").append(lastOperation.getAmount());

            viewHolder.tvOperation.setText(String.valueOf(operation));
            viewHolder.tvDate.setText(Formatter.formatDateTime(lastOperation.getDate()));
        }


        viewHolder.btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                View layout = inflater.inflate(R.layout.template_operations, null);

                final PopupWindow popupWindow = new PopupWindow(mContext);
                popupWindow.setContentView(layout);
                Button button = ((Button) layout.findViewById(R.id.btn_new));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels - 100;
                int height = displayMetrics.heightPixels / 2;
                popupWindow.setWidth(width);
                popupWindow.setHeight(height);
                popupWindow.setElevation(30);
                popupWindow.showAtLocation(viewHolder.btnAddProfit, Gravity.CENTER, 0, 0);
            }
        });

        viewHolder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(mContext, v, Gravity.START);
                popupMenu.inflate(R.menu.card_accoutn_edit_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                startEditor(viewHolder);
                                return true;
                            case R.id.remove:
                                dialogDelete = DialogDelete.newInstance(CashAccountsRecyclerViewAdapter.this);
                                titleCashToDelete = viewHolder.tvAccountName.getText().toString();
                                positionCashToDelete = i;
                                dialogDelete.show(mContext.getFragmentManager(), "dialogDelete");
                                Log.d(LOG_TAG, "button remove");
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCashAccounts.size();
    }

    private void startEditor (CashAccountViewHolder viewHolder){
        Intent intent = new Intent(mContext, NewCashAccountActivity.class);
        intent.putExtra(NamesOfParametrs.CASH_TITLE, viewHolder.tvAccountName.getText());
        intent.putExtra(NamesOfParametrs.CASH_TYPE, viewHolder.tvAccountType.getText());
        intent.putExtra(NamesOfParametrs.CASH_CURRENCY_SHORT_HAND, viewHolder.tvCurrency.getText());
        intent.putExtra(NamesOfParametrs.AMOUNT, viewHolder.tvAmount.getText());
        intent.putExtra(NamesOfParametrs.CASH_LOGO, (int) viewHolder.ivAccountLogo.getTag());
        intent.putExtra(NamesOfParametrs.CASH_COLOR, (int) viewHolder.rlTitleBar.getTag());
        intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, "Редактирование");
        mContext.startActivity(intent);
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
        mCashAccounts.remove(positionCashToDelete);
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
