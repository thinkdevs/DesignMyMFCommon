package com.thinkdevs.designmymfcommon.dialog;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewOperationActivity;
import com.thinkdevs.designmymfcommon.adapter.RecyclerViewCashAccountsAdapter;
import com.thinkdevs.designmymfcommon.adapter.RecyclerViewOperationTemplatesAdapter;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.utills.NamesOfParametrs;

import java.util.List;

public class OperationTemplatesDialogFragment extends DialogFragment
        implements View.OnClickListener {

    final String LOG_TAG = "mylog";

    private RecyclerView rvOperationTemplates;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView     tvTitle;
    Button       btnNew;

    private static String typeOperation;
    private static String nameCashAccount;
    private static RecyclerViewCashAccountsAdapter recyclerViewCashAccountsAdapter;

    public static OperationTemplatesDialogFragment newInstance (
            String typeOperation,
            String nameCashAccount,
            RecyclerViewCashAccountsAdapter recyclerViewCashAccountsAdapter){
        OperationTemplatesDialogFragment.typeOperation
                = typeOperation;
        OperationTemplatesDialogFragment.nameCashAccount
                = nameCashAccount;
        OperationTemplatesDialogFragment.recyclerViewCashAccountsAdapter
                = recyclerViewCashAccountsAdapter;
        return new OperationTemplatesDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_dialog_operation_templates, null);
        tvTitle   = (TextView) view.findViewById(R.id.tv_title);
        btnNew = (Button)view.findViewById(R.id.btn_new);
        btnNew.setOnClickListener(this);

        List<OperationTemplate> operationTemplateList;


        if(OperationTemplate.TYPE_EXPENSE.equals(typeOperation)) {
            tvTitle.setText(getActivity().getResources().getString(R.string.operation_expense).toUpperCase());
            tvTitle.setTextColor(getResources().getColor(R.color.red));
            btnNew.setTextColor(getResources().getColor(R.color.red));
            operationTemplateList = OperationTemplate.getExpenseOperationTemplates();
        }
        else {
            tvTitle.setText(getActivity().getResources().getString(R.string.operation_profit).toUpperCase());
            tvTitle.setTextColor(getResources().getColor(R.color.green));
            operationTemplateList = OperationTemplate.getProfitOperationTemplates();
            btnNew.setTextColor(getResources().getColor(R.color.green));
        }

        rvOperationTemplates = (RecyclerView) view.findViewById(R.id.rv_operation_templates);
        rvOperationTemplates.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvOperationTemplates.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewOperationTemplatesAdapter(
                getActivity(),
                operationTemplateList,
                nameCashAccount,
                recyclerViewCashAccountsAdapter,
                this);
        rvOperationTemplates.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), NewOperationActivity.class);
        intent.putExtra(NamesOfParametrs.CASH_ACCOUNT_NAME, nameCashAccount);
        intent.putExtra(NamesOfParametrs.ACTIVITY_TITLE, getResources().getString(R.string.action_new_operation));
        intent.putExtra(NamesOfParametrs.TYPE, typeOperation);
        startActivity(intent);
        dismiss();
    }

    public interface NoticeDialogListener {
        void onDialogOperationFromTempalateClick();
    }

}
