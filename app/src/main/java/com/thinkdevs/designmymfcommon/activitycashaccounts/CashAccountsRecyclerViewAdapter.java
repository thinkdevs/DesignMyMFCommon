package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.mock.CashAccount;

import java.util.ArrayList;

public class CashAccountsRecyclerViewAdapter extends RecyclerView.Adapter<CashAccountsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<CashAccount> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CashAccountsRecyclerViewAdapter(ArrayList<CashAccount> mDataset) {
        this.mDataset = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CashAccountsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cash_account, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((TextView) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
