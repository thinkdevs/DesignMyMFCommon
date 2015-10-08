package com.thinkdevs.designmymfcommon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Color;

import java.util.List;


public class GridColorAdapter extends ArrayAdapter<Color> {

    Resources res;

    public GridColorAdapter(Context context, List<Color> colors){
        super(context, R.layout.item_grid_decor_color, colors);
        res = context.getResources();
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Color color = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_grid_decor_color, null);
        }

        convertView.findViewById(R.id.lv_color)
                .setBackgroundColor(res.getColor(color.getResourceId()));
        convertView.findViewById(R.id.lv_color).setTag(color.getResourceId());

        return convertView;
    }
}
