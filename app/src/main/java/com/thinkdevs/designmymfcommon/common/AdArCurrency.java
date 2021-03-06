package com.thinkdevs.designmymfcommon.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Currency;

import java.util.List;


public class AdArCurrency extends ArrayAdapter<Currency> {

    public AdArCurrency(Context context, List<Currency> currencies){
        super(context, android.R.layout.simple_list_item_1, currencies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Currency currency = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView strSymbol = ((TextView) (convertView.findViewById(android.R.id.text1)));
        strSymbol.setText(currency.getStrSymbol());
        strSymbol.setTag(R.string.tag_currency_id, currency.getId());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Currency currency = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.currency_drop_down_item, null);
        }

        ((TextView)(convertView.findViewById(R.id.textView1))).setText(currency.getStrSymbol());
        ((TextView)(convertView.findViewById(R.id.textView2))).setText(currency.getName());

        return convertView;
    }
}
