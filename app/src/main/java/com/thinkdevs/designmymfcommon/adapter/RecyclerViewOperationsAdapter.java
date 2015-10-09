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
import com.thinkdevs.designmymfcommon.activity.NewOperationActivity;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Constants;
import com.thinkdevs.designmymfcommon.utills.Formatter;

import java.util.List;

public class RecyclerViewOperationsAdapter extends
        RecyclerView.Adapter<RecyclerViewOperationsAdapter.OperationViewHolder>
        implements View.OnLongClickListener, DeleteDialogFragment.NoticeDialogListener{

    private List<Operation> mOperations;
    private Activity        mContext;
    private Resources       mResources;
    private DialogFragment  dialogDelete;
    private int             positionToDelete;
    private long            idToDelete;

    public static class OperationViewHolder extends RecyclerView.ViewHolder {
        public CardView    cardView;
        public FrameLayout flLogo;
        public ImageView   ivCategoryLogo;
        public TextView    tvCategoryName;
        public TextView    tvCashAccountName;
        public TextView    tvAmount;
        public TextView    tvDate;

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

    public RecyclerViewOperationsAdapter(Activity context, List<Operation> operations) {
        this.mOperations = operations;
        this.mContext    = context;
        this.mResources  = context.getResources();
    }

    @Override
    public OperationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_operation, parent, false);

        OperationViewHolder vh = new OperationViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OperationViewHolder viewHolder, final int i) {

        Operation operation = mOperations.get(i);

        //Сохранение id операции и ее позииции в списке
        viewHolder.cardView.setTag(R.string.tag_operation_id, operation.getId());
        viewHolder.cardView.setTag(R.string.tag_position_in_rv, i);

        //Цвет фона под логотипом
        viewHolder.flLogo.setBackgroundColor(
                (mResources.getColor(operation.getCategory().getColor().getResourceId())));
        viewHolder.flLogo.setTag(
                R.string.tag_resource_id, (operation.getCategory().getColor().getResourceId()));

        //Логотип
        viewHolder.ivCategoryLogo.setImageResource(operation.getCategory().getLogo().getResourceId());
        viewHolder.ivCategoryLogo.setTag(
                R.string.tag_resource_id, operation.getCategory().getLogo().getResourceId());

        //Имя категории
        viewHolder.tvCategoryName.setText(operation.getCategory().getName());
        viewHolder.tvCategoryName.setTag(R.string.tag_id, operation.getCategory().getId());
        viewHolder.tvCategoryName.setTag(
                R.string.tag_hierarchy, operation.getCategory().getHierarchy());

        //Имя кошелька
        viewHolder.tvCashAccountName.setText(mOperations.get(i).getCashAccount().getName());
        viewHolder.tvCashAccountName.setTag(R.string.tag_id, operation.getCashAccount().getId());

        //Сумма
        StringBuilder sbAmount  = new StringBuilder();
        float amount = operation.getAmount();
        if(operation.isExpense()) {
            sbAmount.append("-").append(amount);
            viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else {
            sbAmount.append("+").append(amount);
            viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        viewHolder.tvAmount.setText(sbAmount);

        //Дата
        viewHolder.tvDate.setText(Formatter.formatDateTime(operation.getDate()));

        //Установка слушателей
        viewHolder.cardView.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mOperations.size();
    }

    private void openEditor(long id){
        Intent intent = new Intent(mContext, NewOperationActivity.class);
        intent.putExtra(Constants.OPEN_AS, false);
        intent.putExtra(Constants.OPERATION_ID, id);
        intent.putExtra(Constants.ACTIVITY_TITLE, R.string.title_activity_operation_editing);
        mContext.startActivity(intent);
    }

    private void deleteOperation(){
        Operation operation = Operation.getByID(idToDelete);
        CashAccount cashAccount = operation.getCashAccount();
        float cashAccountAmount = cashAccount.getAmount();
        cashAccount.setAmount(operation.isExpense()
                ? cashAccountAmount + operation.getAmount()
                : cashAccountAmount - operation.getAmount());
        cashAccount.update();
        operation.delete();
    }

    private void updateAfterDelete(){
        mOperations.remove(positionToDelete);
        notifyItemRemoved(positionToDelete);
        notifyItemRangeChanged(positionToDelete, getItemCount());
    }

    @Override
    public void onDialogPositiveClick() {
        deleteOperation();
        updateAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public boolean onLongClick(final View v) {
        //id операции
        final long id = (long)(v.findViewById(R.id.cv_operation).getTag(R.string.tag_id));
        //позиция в rv
        final int position = (int)(v.findViewById(R.id.cv_operation).getTag(R.string.tag_position_in_rv));
        //меню
        final PopupMenu popupMenu = new PopupMenu(mContext, v.findViewById(R.id.tv_category_name));
        popupMenu.inflate(R.menu.menu_popup_operation);
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
                                RecyclerViewOperationsAdapter.this, mContext.getString(R.string.msg_delete_operation));
                        dialogDelete.show(mContext.getFragmentManager(), "dialog_delete");
                        return true;
                    case R.id.add_to_templates:
                        OperationTemplate.add(Operation.getByID(id));
                    default:
                        return false;
                    }
                }
            });
        popupMenu.show();
        return true;
    }

}
