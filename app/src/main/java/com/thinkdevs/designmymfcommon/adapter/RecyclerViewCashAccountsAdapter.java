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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCashAccountActivity;
import com.thinkdevs.designmymfcommon.activity.NewOperationActivity;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.dialog.OperationTemplatesDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Constants;
import com.thinkdevs.designmymfcommon.utills.Formatter;

import java.util.List;

public class RecyclerViewCashAccountsAdapter extends
        RecyclerView.Adapter<RecyclerViewCashAccountsAdapter.CashAccountViewHolder>
        implements View.OnLongClickListener, DeleteDialogFragment.NoticeDialogListener{

    private List<CashAccount> mCashAccounts;
    private Activity          mContext;
    private Resources         mResources;
    private DialogFragment    dialogDelete;
    private int               positionToDelete;
    private long              idToDelete;

    public static class CashAccountViewHolder extends RecyclerView.ViewHolder {

        public CardView       cardView;
        public RelativeLayout rlTitleBar;
        public ImageView      ivCashAccountLogo;
        public TextView       tvCashAccountName;
        public TextView       tvCashAccountComment;
        public TextView       tvCashAccountAmount;
        public TextView       tvDate;
        public TextView       tvOperation;
        public TextView       tvUnit;
        public TextView       tvCurrency;
        public Button         btnAddExpense;
        public Button         btnAddProfit;

        public CashAccountViewHolder(View itemView) {
            super(itemView);
            cardView             = (CardView)itemView.findViewById(R.id.cv_cash_account);
            rlTitleBar           = (RelativeLayout)itemView.findViewById(R.id.rl_title_bar);
            ivCashAccountLogo    = (ImageView)itemView.findViewById(R.id.iv_category_logo);
            tvCashAccountName    = (TextView)itemView.findViewById(R.id.tv_cash_account_name);
            tvCashAccountComment = (TextView)itemView.findViewById(R.id.tv_cash_account_comment);
            tvCashAccountAmount  = (TextView)itemView.findViewById(R.id.tv_amount);
            tvDate               = (TextView)itemView.findViewById(R.id.tv_date);
            tvOperation          = (TextView)itemView.findViewById(R.id.tv_operation);
            tvUnit               = (TextView)itemView.findViewById(R.id.tv_unit);
            tvCurrency           = (TextView)itemView.findViewById(R.id.tv_currency);
            btnAddExpense        = (Button)itemView.findViewById(R.id.btn_add_expense);
            btnAddProfit         = (Button)itemView.findViewById(R.id.btn_add_profit);
        }
    }

    public RecyclerViewCashAccountsAdapter(Activity context, List<CashAccount> cashAccounts) {
        this.mCashAccounts = cashAccounts;
        this.mContext      = context;
        this.mResources    = context.getResources();
    }

    @Override
    public CashAccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cash_account, parent, false);

        CashAccountViewHolder vh = new CashAccountViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CashAccountViewHolder viewHolder, final int i) {

        CashAccount cashAccount = mCashAccounts.get(i);

        //Сохранение id счета и его позииции в списке
        viewHolder.cardView.setTag(R.string.tag_cash_account_id, cashAccount.getId());
        viewHolder.cardView.setTag(R.string.tag_position_in_rv, i);

        viewHolder.rlTitleBar.setTag(R.string.tag_cash_account_id, cashAccount.getId());
        viewHolder.rlTitleBar.setTag(R.string.tag_position_in_rv, i);

        //id счета для быстрого создания операций
        viewHolder.btnAddExpense.setTag(R.string.tag_cash_account_id, cashAccount.getId());
        viewHolder.btnAddProfit.setTag(R.string.tag_cash_account_id, cashAccount.getId());

        //Цвет бара кошелька
        viewHolder.rlTitleBar.   setBackgroundColor(
                (mResources.getColor(cashAccount.getColor().getResourceId())));
        viewHolder.rlTitleBar.setTag(
                R.string.tag_resource_id, cashAccount.getColor().getResourceId());

        //Логотип
        viewHolder.ivCashAccountLogo.setImageResource(cashAccount.getIcon().getResourceId());
        viewHolder.ivCashAccountLogo.setTag(R.string.tag_resource_id, cashAccount.getIcon().getResourceId());

        //Имя
        viewHolder.tvCashAccountName.setText(cashAccount.getName());

        //Комментарий
        viewHolder.tvCashAccountComment.setText(cashAccount.getComment());

        //Средства и еденицы
        String strAmount;
        String strUnit;
        long amount = cashAccount.getAmount()/100;
        if(amount > 1_000_000_000) {
            strAmount = String.valueOf(amount / 1_000_000_000f);
            strUnit   = mResources.getString(R.string.unit_billion);
        }
        else if(amount > 1_000_000){
            strAmount = String.valueOf(amount / 1_000_000f);
            strUnit   = mResources.getString(R.string.unit_billion);
        }
        else if(amount > 1_000){
            strAmount = String.valueOf(amount / 1_000f);
            strUnit   = mResources.getString(R.string.unit_thousand);
        }
        else {
            strAmount = String.valueOf(amount);
            strUnit   = "";
        }
        //Средства
        viewHolder.tvCashAccountAmount.setText(strAmount);
        viewHolder.tvUnit.setText(strUnit);

        //Валюта
        viewHolder.tvCurrency.setText(cashAccount.getCurrency().getStrSymbol());
        viewHolder.tvCurrency.setTag(R.string.tag_currency_id, cashAccount.getCurrency().getId());

        //Последняя операция и дата последнего изменения
        Operation lastOperation = cashAccount.getLastOperation();
        if(lastOperation == null){
            viewHolder.tvOperation.setText("операций нет");
            viewHolder.tvDate.setText("");
        }
        else {
            StringBuilder sbLastOperation  = new StringBuilder();
            if(lastOperation.isExpense())
                sbLastOperation.append("-").append(lastOperation.getAmount());
            else
                sbLastOperation.append("+").append(lastOperation.getAmount());

            viewHolder.tvOperation.setText(String.valueOf(sbLastOperation));
            viewHolder.tvDate.setText(Formatter.formatDateTime(lastOperation.getDate()));
        }

        //Установка слушателей
        viewHolder.rlTitleBar.setOnLongClickListener(this);
        FastOperationOnClickListener listener = new FastOperationOnClickListener();
        viewHolder.btnAddExpense.setOnClickListener(listener);
        viewHolder.btnAddProfit.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mCashAccounts.size();
    }

    private void openEditor(long id){
        Intent intent = new Intent(mContext, NewCashAccountActivity.class);
        intent.putExtra(Constants.OPEN_AS, CashAccount.EDIT);
        intent.putExtra(Constants.CASH_ACCOUNT_ID, id);
        intent.putExtra(Constants.ACTIVITY_TITLE, R.string.title_activity_cash_account_editing);
        mContext.startActivity(intent);
    }

    private void deleteCashAccount(){
        CashAccount.deleteByID(idToDelete);
    }

    private void updateAfterDelete(){
        mCashAccounts.remove(positionToDelete);
        notifyItemRemoved(positionToDelete);
        notifyItemRangeChanged(positionToDelete, getItemCount());
    }

    @Override
    public void onDialogPositiveClick() {
        deleteCashAccount();
        updateAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

    public void updateAfterChangeData(){
        mCashAccounts = CashAccount.getCashAccounts();
        this.notifyDataSetChanged();
    }

    @Override
    public boolean onLongClick(final View v) {
        //id операции
        final long id = (long)(v.getTag(R.string.tag_cash_account_id));
        //позиция в rv
        final int position = (int)(v.getTag(R.string.tag_position_in_rv));
        //меню
        final PopupMenu popupMenu = new PopupMenu(
                mContext, v.findViewById(R.id.tv_cash_account_name));
        popupMenu.inflate(R.menu.menu_popup_cash_account);
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
                                RecyclerViewCashAccountsAdapter.this,
                                mContext.getString(R.string.msg_delete_cash_account));
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

    public class FastOperationOnClickListener implements View.OnClickListener{
        int typeOperation;
        long cashAccountID;
        List<OperationTemplate> templates;
        OperationTemplatesDialogFragment dialog;

        @Override
        public void onClick(View v) {
            cashAccountID = (long) v.getTag(R.string.tag_cash_account_id);
            switch (v.getId()){
                case R.id.btn_add_expense:
                    typeOperation = Category.EXPENSE;
                    templates = OperationTemplate.getExpenseTemplates();
                    break;
                case R.id.btn_add_profit:
                    typeOperation = Category.PROFIT;
                    templates = OperationTemplate.getProfitTemplates();
            }

            if(templates != null && templates.isEmpty()){
                dialog = OperationTemplatesDialogFragment.newInstance(
                        typeOperation, cashAccountID, RecyclerViewCashAccountsAdapter.this);
            }
            else {
                Intent intent = new Intent(mContext, NewOperationActivity.class);
                intent.putExtra(
                        Constants.NAME,
                        CashAccount.getByID(cashAccountID).getName());
                intent.putExtra(Constants.ACTIVITY_TITLE, mContext.getResources().getString(R.string.action_new_operation));
                intent.putExtra(Constants.TYPE, typeOperation);
                mContext.startActivity(intent);
            }
        }
    }
}
