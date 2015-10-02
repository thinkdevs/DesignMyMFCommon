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
import com.thinkdevs.designmymfcommon.activity.NewOperationTemplateActivity;
import com.thinkdevs.designmymfcommon.database.Operation;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.dialog.DeleteDialogFragment;
import com.thinkdevs.designmymfcommon.dialog.OperationTemplatesDialogFragment;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class RecyclerViewOperationTemplatesAdapter extends
        RecyclerView.Adapter<RecyclerViewOperationTemplatesAdapter.OperationTemplateViewHolder>
        implements View.OnLongClickListener, View.OnClickListener, DeleteDialogFragment.NoticeDialogListener {

    private boolean IS_DIALOG = false;

    private List<OperationTemplate> mTemplates;
    private Activity                mContext;
    private Resources               mResources;
    private DialogFragment          dialogDelete;
    private int                     positionToDelete;
    private long                    idToDelete;

    private long cashAccountID;
    private RecyclerViewCashAccountsAdapter  cashAccountsAdapter;
    private OperationTemplatesDialogFragment operationTemplatesDialogFragment;

    public static class OperationTemplateViewHolder extends RecyclerView.ViewHolder {
        public CardView       cardView;
        public FrameLayout    flLogo;
        public ImageView      ivCategoryLogo;
        public TextView       tvTemplateName;
        public TextView       tvCategoryName;
        public TextView       tvAmount;

        public OperationTemplateViewHolder(View itemView) {
            super(itemView);
            cardView       = (CardView)    itemView.findViewById(R.id.cv_operation_template);
            flLogo         = (FrameLayout) itemView.findViewById(R.id.fl_logo);
            ivCategoryLogo = (ImageView)   itemView.findViewById(R.id.iv_category_logo);
            tvTemplateName = (TextView)    itemView.findViewById(R.id.tv_template_name);
            tvCategoryName = (TextView)    itemView.findViewById(R.id.tv_category_name);
            tvAmount       = (TextView)    itemView.findViewById(R.id.tv_amount);
        }
    }

    public RecyclerViewOperationTemplatesAdapter(
            Activity context,
            List<OperationTemplate> templates) {
        this.mTemplates = templates;
        this.mContext   = context;
        this.mResources = context.getResources();
    }

    public RecyclerViewOperationTemplatesAdapter(
            Activity context,
            List<OperationTemplate> operationTemplates,
            long cashAccountID,
            RecyclerViewCashAccountsAdapter cashAccountsAdapter,
            OperationTemplatesDialogFragment operationTemplatesDialogFragment) {
        this.IS_DIALOG           = true;
        this.mTemplates          = operationTemplates;
        this.cashAccountID       = cashAccountID;
        this.cashAccountsAdapter = cashAccountsAdapter;
        this.operationTemplatesDialogFragment
                                 = operationTemplatesDialogFragment;
        this.mContext            = context;
        this.mResources          = context.getResources();
    }

    @Override
    public OperationTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_operation_template, parent, false);
        OperationTemplateViewHolder vh = new OperationTemplateViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OperationTemplateViewHolder viewHolder, final int i) {

        OperationTemplate template = mTemplates.get(i);

        //Сохранение id шаблона и его позииции в списке
        viewHolder.cardView.setTag(R.string.tag_operation_template_ID, template.getId());
        viewHolder.cardView.setTag(R.string.tag_position_in_rv, i);

        //Цвет фона логотипа
        viewHolder.flLogo.setBackgroundColor(
                (mResources.getColor(template.getCategory().getColor().getResourceId())));
        viewHolder.flLogo.setTag(
                R.string.tag_resource_ID, template.getCategory().getColor().getResourceId());

        //Логотип
        viewHolder.ivCategoryLogo.setImageResource(template.getCategory().getLogo().getResourceId());
        viewHolder.ivCategoryLogo.setTag(
                R.string.tag_resource_ID, template.getCategory().getLogo().getResourceId());

        //Имя шаблона
        viewHolder.tvTemplateName.setText(template.getName());

        //Имя категории
        viewHolder.tvCategoryName.setText(template.getCategory().getName());
        viewHolder.tvCategoryName.setTag(R.string.tag_id, template.getCategory().getId());
        viewHolder.tvCategoryName.setTag(
                R.string.tag_hierarchy, template.getCategory().getHierarchy());

        //Сумма
        StringBuilder sbAmount  = new StringBuilder();
        float amount = template.getAmount();
        if (template.isExpense()) {
            sbAmount.append("-").append(amount);
            viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else {
            sbAmount.append("+").append(amount);
            viewHolder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        viewHolder.tvAmount.setText(sbAmount.toString());

        //Установка слушателей
        viewHolder.cardView.setOnLongClickListener(this);
        if(IS_DIALOG) {
            viewHolder.cardView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return mTemplates.size();
    }

    private void openEditor(long id){
        Intent intent = new Intent(mContext, NewOperationTemplateActivity.class);
        intent.putExtra(Constants.OPEN_AS, false);
        intent.putExtra(
                Constants.OPERATION_TEMPLATE_ID, id);
        intent.putExtra(Constants.ACTIVITY_TITLE, R.string.title_activity_operation_template_editing);
        mContext.startActivity(intent);
    }

    private void deleteTemplate(){
       OperationTemplate.deleteByID(idToDelete);
    }

    private void updateAfterDelete(){
        mTemplates.remove(positionToDelete);
        notifyItemRemoved(positionToDelete);
        notifyItemRangeChanged(positionToDelete, getItemCount());
    }

    @Override
    public void onDialogPositiveClick() {
        deleteTemplate();
        updateAfterDelete();
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public boolean onLongClick(final View v) {
        //id операции
        final long id = (long)(v.findViewById(R.id.cv_operation_template).getTag(R.string.tag_id));
        //позиция в rv
        final int position =
                (int)(v.findViewById(R.id.cv_operation_template).getTag(R.string.tag_position_in_rv));
        //меню
        final PopupMenu popupMenu = new PopupMenu(
                mContext, v.findViewById(R.id.tv_template_name));
        popupMenu.inflate(R.menu.menu_popup_operation_template);
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
                                RecyclerViewOperationTemplatesAdapter.this,
                                mContext.getString(R.string.msg_delete_operation_template));
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

    @Override
    public void onClick(View v) {
        long templateID = (long)v.getTag(R.string.tag_operation_template_ID);
        Operation.add(templateID, cashAccountID);
        cashAccountsAdapter.updateAfterChangeData();
        operationTemplatesDialogFragment.dismiss();
    }

}
