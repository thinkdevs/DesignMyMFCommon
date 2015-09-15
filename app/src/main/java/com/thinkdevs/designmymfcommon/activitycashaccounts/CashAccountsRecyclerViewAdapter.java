package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Cash;
import com.thinkdevs.designmymfcommon.database.Cash$Table;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.Profit;
import com.thinkdevs.designmymfcommon.utills.Formatter;

import java.util.List;

public class CashAccountsRecyclerViewAdapter extends
        RecyclerView.Adapter<CashAccountsRecyclerViewAdapter.CashAccountViewHolder> {

    private List<Cash> mCashAccounts;
    private Context mContext;
    private Resources mResources;

    public static class CashAccountViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView       cardView;
        public RelativeLayout rlTitleBar;
        public ImageView      tvAccountLogo;
        public TextView       tvAccountName;
        public TextView       tvAccountType;
        public TextView       tvMoney;
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
            tvAccountLogo = (ImageView)      itemView.findViewById(R.id.iv_category_logo);
            tvAccountName = (TextView)       itemView.findViewById(R.id.tv_account_name);
            tvAccountType = (TextView)       itemView.findViewById(R.id.tv_account_type);
            tvMoney       = (TextView)       itemView.findViewById(R.id.tv_money);
            tvDate        = (TextView)       itemView.findViewById(R.id.tv_date);
            tvOperation   = (TextView)       itemView.findViewById(R.id.tv_operation);
            tvCurrency    = (TextView)       itemView.findViewById(R.id.tv_currency);
            btnAddExpense = (Button)         itemView.findViewById(R.id.btn_add_expense);
            btnAddProfit  = (Button)         itemView.findViewById(R.id.btn_add_profit);
            btnMenu       = (ImageView)      itemView.findViewById(R.id.iv_menu);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CashAccountsRecyclerViewAdapter(Context context, List<Cash> cashAccounts) {
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
    public void onBindViewHolder(final CashAccountViewHolder viewHolder, int i) {

        viewHolder.rlTitleBar.   setBackgroundColor(
                (mResources.getColor(mCashAccounts.get(i).getColor().getResourceId())));
        viewHolder.tvAccountLogo.setImageResource(mCashAccounts.get(i).getLogo().getResourceId());
        viewHolder.tvAccountName.setText(mCashAccounts.get(i).getName());
        viewHolder.tvAccountType.setText(mCashAccounts.get(i).getType());
        viewHolder.tvMoney.      setText(String.valueOf(mCashAccounts.get(i).getAmount()));
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
                int height = displayMetrics.heightPixels/2;
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
                        Toast toast;
                        switch (item.getItemId()){
                            case R.id.edit :
                                toast = Toast.makeText(
                                        mContext,
                                        "Edit",
                                        Toast.LENGTH_LONG);
                                toast.show();
                                return true;
                            case R.id.remove :
                                toast = Toast.makeText(
                                        mContext,
                                        "Remove",
                                        Toast.LENGTH_LONG);
                                toast.show();
                                Cash cash = new Select().from(Cash.class).
                                        where(Condition.column(Cash$Table.NAME)
                                                .is(viewHolder.tvAccountName.getText()))
                                        .querySingle();
                                cash.delete();
                                CashAccountsRecyclerViewAdapter.this.notifyDataSetChanged();
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

}
