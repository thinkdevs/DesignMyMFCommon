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

public class ListLogoCashSpinnerAdapter extends ArrayAdapter<LogoCash> {

    Resources res;

    public ListLogoCashSpinnerAdapter(Context context, List<LogoCash> logos){
        super(context, R.layout.icon_item, logos);
        res = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogoCash logo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.icon_item, null);
        }

        ImageView imageView = (ImageView)(convertView.findViewById(R.id.imageView));
        imageView.setImageResource(logo.getResourceId());
        imageView.setTag(logo.getResourceId());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LogoCash logo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.icon_item, null);
        }

        ImageView imageView = (ImageView)(convertView.findViewById(R.id.imageView));
        imageView.setImageResource(logo.getResourceId());
        imageView.setTag(logo.getResourceId());

        return convertView;
    }
}
