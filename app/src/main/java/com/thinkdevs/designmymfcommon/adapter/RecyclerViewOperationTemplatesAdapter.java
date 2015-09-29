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
import com.thinkdevs.designmymfcommon.activity.NewOperationTemplateActivity;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.dialog.OperationTemplatesDialogFragment;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.List;

public class RecyclerViewOperationTemplatesAdapter extends
        RecyclerView.Adapter<RecyclerViewOperationTemplatesAdapter.OperationTemplateViewHolder>
        implements DeleteDialogFragment.NoticeDialogListener{

    final String LOG_TAG = "mylog";

    private List<OperationTemplate> mOperationTemplate;
    private Activity mContext;
    private Resources mResources;
    private DialogFragment dialogDelete;
    private int positionOperationTemplateToDelete;
    private String titleOperationTemplateToDelete;
    private boolean typeOperationTemplateToDelete;

    private String categoryName;

    private String cashAccountName;
    private RecyclerViewCashAccountsAdapter cashAccountsAdapter;
    private OperationTemplatesDialogFragment operationTemplatesDialogFragment;

    public static class OperationTemplateViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView       cardView;
        public FrameLayout    flLogo;
        public ImageView      ivCategoryLogo;
        public TextView       tvTemplateName;
        public TextView       tvCategoryName;
        public TextView       tvAmount;

        public OperationTemplateViewHolder(View itemView) {
            super(itemView);
            cardView       = (CardView)      itemView.findViewById(R.id.cv_operation_template);
            flLogo         = (FrameLayout)   itemView.findViewById(R.id.fl_logo);
            ivCategoryLogo = (ImageView)     itemView.findViewById(R.id.iv_category_logo);
            tvTemplateName = (TextView)      itemView.findViewById(R.id.tv_template_name);
            tvCategoryName = (TextView)      itemView.findViewById(R.id.tv_category_name);
            tvAmount       = (TextView)      itemView.findViewById(R.id.tv_amount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewOperationTemplatesAdapter(
            Activity context,
            List<OperationTemplate> operationTemplates) {
        this.mOperationTemplate = operationTemplates;
        this.mContext = context;
        this.mResources = context.getResources();
    }

    public RecyclerViewOperationTemplatesAdapter(
            Activity context,
            List<OperationTemplate> operationTemplates,
            String cashAccountName,
            RecyclerViewCashAccountsAdapter cashAccountsAdapter,
            OperationTemplatesDialogFragment operationTemplatesDialogFragment) {
        this.mOperationTemplate = operationTemplates;
        this.cashAccountName = cashAccountName;
        this.cashAccountsAdapter = cashAccountsAdapter;
        this.operationTemplatesDialogFragment = operationTemplatesDialogFragment;
        this.mContext = context;
        this.mResources = context.getResources();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OperationTemplateViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_operation_template, parent, false);

        OperationTemplateViewHolder vh = new OperationTemplateViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OperationTemplateViewHolder viewHolder, final int i) {

        viewHolder.flLogo.   setBackgroundColor(
                (mResources.getColor(mOperationTemplate.get(i).getSubCategory().getCategory().getColor().getResourceId())));
        viewHolder.flLogo.   setTag(
                (mOperationTemplate.get(i).getSubCategory().getCategory().getColor().getResourceId()));
        viewHolder.ivCategoryLogo.setImageResource(mOperationTemplate.get(i).getSubCategory().getCategory().getLogo().getResourceId());
        viewHolder.ivCategoryLogo.setTag(mOperationTemplate.get(i).getSubCategory().getCategory().getLogo().getResourceId());
        viewHolder.tvTemplateName.setText(mOperationTemplate.get(i).getName());
        viewHolder.tvCategoryName.setText(mOperationTemplate.get(i).getSubCategory().getName());
        categoryName = mOperationTemplate.get(i).getSubCategory().getCategory().getName();

            StringBuilder sbAmount  = new StringBuilder();
            if(mOperationTemplate.get(i).isExpense()) {
                sbAmount.append("-").append(mOperationTemplate.get(i).getAmount());
                viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else {
                sbAmount.append("+").append(mOperationTemplate.get(i).getAmount());
                viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        viewHolder.tvAmount.setText(sbAmount.toString());

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(mContext, v.findViewById(R.id.tv_template_name));
                popupMenu.inflate(R.menu.menu_popup_cash_account);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                startEditor(v);
                                return true;
                            case R.id.remove:
                                dialogDelete = DeleteDialogFragment.newInstance(RecyclerViewOperationTemplatesAdapter.this, "");
                                titleOperationTemplateToDelete = ((TextView)v.findViewById(R.id.tv_template_name)).getText().toString();
                                String subCategoryName = ((TextView) v.findViewById(R.id.tv_category_name)).getText().toString();
                                Log.d(LOG_TAG, subCategoryName);
                                typeOperationTemplateToDelete = (SubCategory.getExpenseSubCategoryByName(subCategoryName) != null);
                                Log.d(LOG_TAG, String.valueOf(typeOperationTemplateToDelete));
                                positionOperationTemplateToDelete = i;
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


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем кошелек
                CashAccount cashAccount = CashAccount.getCashAccountByName(cashAccountName);
                // Получаем шаблон
                String typeOperationTemplate = viewHolder.tvAmount.getText().toString().startsWith("-")
                        ? OperationTemplate.TYPE_EXPENSE
                        : OperationTemplate.TYPE_PROFIT;

                Log.d(LOG_TAG, typeOperationTemplate + " " + viewHolder.tvTemplateName.getText().toString());

                OperationTemplate operationTemplate =
                        OperationTemplate.getOperationTemplateByName(viewHolder.tvTemplateName.getText().toString()
                                , typeOperationTemplate);
                Operation.newOperationFromTemplate(operationTemplate, cashAccount);

                cashAccountsAdapter.update();
                operationTemplatesDialogFragment.dismiss();
            }
        };

            viewHolder.cardView.setOnLongClickListener(longClickListener);
        if(cashAccountName != null) {
            Log.d("tag", "RecyclerViewOperationTemplateAdapter - 'setOnclickListener'");
            viewHolder.cardView.setOnClickListener(clickListener);
        }

    }

    @Override
    public int getItemCount() {
        return mOperationTemplate.size();
    }

    private void startEditor (View view){
        Intent intent = new Intent(mContext, NewOperationTemplateActivity.class);
        intent.putExtra(NamesOfParametrs.IS_NEW, false);
        intent.putExtra(NamesOfParametrs.NAME, ((TextView) view.findViewById(R.id.tv_template_name)).getText());
        String subCategoryName = ((TextView) view.findViewById(R.id.tv_category_name)).getText().toString();
        Log.d(LOG_TAG, subCategoryName + " startEditor");
        intent.putExtra(NamesOfParametrs.SUB_CATEGORY_NAME, subCategoryName);
        intent.putExtra(NamesOfParametrs.CATEGORY_NAME, categoryName);
        boolean typeOperation = (SubCategory.getExpenseSubCategoryByName(subCategoryName) != null);
        intent.putExtra(NamesOfParametrs.TYPE, typeOperation);
        intent.putExtra(NamesOfParametrs.AMOUNT, ((TextView) view.findViewById(R.id.tv_amount)).getText().toString().substring(1));
        intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, "Редактирование");
        mContext.startActivity(intent);
    }

    private void deleteOperationTemplate(){
        OperationTemplate operationTemplate;
        String typeOperation = typeOperationTemplateToDelete
                ? OperationTemplate.TYPE_EXPENSE
                : OperationTemplate.TYPE_PROFIT;
        operationTemplate = OperationTemplate.getOperationTemplateByName(titleOperationTemplateToDelete, typeOperation);
        operationTemplate.delete();
    }

    private void updateRecycleViewAfterDelete(){
        mOperationTemplate.remove(positionOperationTemplateToDelete);
        notifyItemRemoved(positionOperationTemplateToDelete);
        notifyItemRangeChanged(positionOperationTemplateToDelete, getItemCount());
    }


    @Override
    public void onDialogPositiveClick() {
        deleteOperationTemplate();
        updateRecycleViewAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

}
