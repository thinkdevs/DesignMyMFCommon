package com.thinkdevs.designmymfcommon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.LogoCash;

import java.util.List;

/**
 * Адаптер для таблицы с иконками счетов
 */
public class ListLogosCashAccountAdapter extends ArrayAdapter<LogoCash> {

    Resources res;

    public ListLogosCashAccountAdapter(Context context, List<LogoCash> logos){
        super(context, R.layout.item_cashes_list, logos);
        res = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogoCash logo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_cashes_list, null);
        }

        ImageView imageView = (ImageView)(convertView.findViewById(R.id.imageView_logo));
        imageView.setImageResource(logo.getResourceId());
        imageView.setTag(logo.getResourceId());

        return convertView;
    }
}
