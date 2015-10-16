package com.thinkdevs.designmymfcommon.operationtemplates;

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
import com.thinkdevs.designmymfcommon.operations.AcCreateOperation;
import com.thinkdevs.designmymfcommon.cashaccounts.AdRvCashAccounts;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.OperationTemplate;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class FrDgOperationTemplates extends DialogFragment
        implements View.OnClickListener {

    private RecyclerView rvOperationTemplates;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView tvTitle;
    Button   btnNew;

    private static int typeOperation;
    private static long cashAccountID;
    private static AdRvCashAccounts adRvCashAccounts;

    public static FrDgOperationTemplates newInstance (
            int typeOperation,
            long cashAccountID,
            AdRvCashAccounts adRvCashAccounts){
        FrDgOperationTemplates.typeOperation
                = typeOperation;
        FrDgOperationTemplates.cashAccountID
                = cashAccountID;
        FrDgOperationTemplates.adRvCashAccounts
                = adRvCashAccounts;
        return new FrDgOperationTemplates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_dialog_operation_templates, null);
        tvTitle   = (TextView) view.findViewById(R.id.tv_title);
        btnNew = (Button)view.findViewById(R.id.btn_new);
        btnNew.setOnClickListener(this);

        List<OperationTemplate> templates;

        if(Category.EXPENSE == typeOperation) {
            tvTitle.setText(getActivity().getResources().getString(R.string.operation_expense).toUpperCase());
            tvTitle.setTextColor(getResources().getColor(R.color.red));
            btnNew.setTextColor(getResources().getColor(R.color.red));
            templates = OperationTemplate.getExpenseTemplates();
        }
        else {
            tvTitle.setText(getActivity().getResources().getString(R.string.operation_profit).toUpperCase());
            tvTitle.setTextColor(getResources().getColor(R.color.green));
            templates = OperationTemplate.getProfitTemplates();
            btnNew.setTextColor(getResources().getColor(R.color.green));
        }

        rvOperationTemplates = (RecyclerView) view.findViewById(R.id.rv_operation_templates);
        rvOperationTemplates.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvOperationTemplates.setLayoutManager(mLayoutManager);

        mAdapter = new AdRvOperationTemplates(
                getActivity(),
                templates,
                cashAccountID,
                adRvCashAccounts,
                this);
        rvOperationTemplates.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AcCreateOperation.class);
        intent.putExtra(Constants.CASH_ACCOUNT_ID, cashAccountID);
        intent.putExtra(Constants.ACTIVITY_TITLE, getResources().getString(R.string.title_activity_new_operation));
        intent.putExtra(Constants.OPERATION_TEMPLATE_TYPE, typeOperation);
        startActivity(intent);
        dismiss();
    }
}
