package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.mock.CashAccount;

import java.util.List;

public class CashAccountsRecyclerViewAdapter extends
        RecyclerView.Adapter<CashAccountsRecyclerViewAdapter.CashAccountViewHolder> {

    private List<CashAccount> mDataset;
    private Context mContext;

    public static class CashAccountViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView  cardView;
        public ImageView tvAccountLogo;
        public TextView  tvAccountName;
        public TextView  tvAccountType;
        public TextView  tvMoney;
        public TextView  tvDate;
        public TextView  tvOperation;
        public ImageView ivCurrency;
        public Button    btnAddExpense;
        public Button    btnAddProfit;
        public ImageView btnMenu;

        public CashAccountViewHolder(View itemView) {
            super(itemView);
            cardView      = (CardView)  itemView.findViewById(R.id.cv_cash_account);
            tvAccountLogo = (ImageView) itemView.findViewById(R.id.iv_account_logo);
            tvAccountName = (TextView)  itemView.findViewById(R.id.tv_account_name);
            tvAccountType = (TextView)  itemView.findViewById(R.id.tv_account_type);
            tvMoney       = (TextView)  itemView.findViewById(R.id.tv_money);
            tvDate        = (TextView)  itemView.findViewById(R.id.tv_date);
            tvOperation   = (TextView)  itemView.findViewById(R.id.tv_operation);
            ivCurrency    = (ImageView) itemView.findViewById(R.id.iv_currency);
            btnAddExpense = (Button)    itemView.findViewById(R.id.btn_add_expense);
            btnAddProfit  = (Button)    itemView.findViewById(R.id.btn_add_profit);
            btnMenu       = (ImageView) itemView.findViewById(R.id.iv_menu);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CashAccountsRecyclerViewAdapter(Context context, List<CashAccount> mDataset) {
        this.mDataset = mDataset;
        this.mContext = context;
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

    // Заполняем view данными
    @Override
    public void onBindViewHolder(CashAccountViewHolder viewHolder, int i) {
        viewHolder.tvAccountLogo.setImageResource(mDataset.get(i).getLogo());
        viewHolder.tvAccountName.setText(mDataset.get(i).getName());
        viewHolder.tvAccountType.setText(mDataset.get(i).getType());
        viewHolder.tvMoney.      setText(String.valueOf(mDataset.get(i).getAmount()));
        viewHolder.tvDate.       setText(mDataset.get(i).getDateLastOperation());
        viewHolder.tvOperation.  setText(String.valueOf(mDataset.get(i).getAmountLastOperation()));
        viewHolder.ivCurrency.   setImageResource(mDataset.get(i).getLogoCurrency());

        viewHolder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.inflate(R.menu.card_accoutn_edit_menu);
                popupMenu.show();
            }
        });
    }

    // Возвращает колличество элементов в списке
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
