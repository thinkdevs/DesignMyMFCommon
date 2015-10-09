package com.thinkdevs.designmymfcommon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Color;

import java.util.List;


public class GridColorAdapter extends ArrayAdapter<Color> {

    Resources res;
    private long mCurrentColorId;

    public GridColorAdapter(Context context, List<Color> colors, long currentColorId){
        super(context, R.layout.item_grid_decor_color, colors);
        res = context.getResources();
        mCurrentColorId = currentColorId;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Color color = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.icon_grid_decor_logo, null);
        }

        ImageView imageView = (ImageView)(convertView.findViewById(R.id.imageView));
        if(color.getId() == mCurrentColorId)
            imageView.setImageResource(R.drawable.ic_checkbox_marked_white_48dp);
        else
            imageView.setImageResource(R.drawable.ic_checkbox_blank_outline_white_48dp);

        convertView.setTag(R.string.tag_color_id, color.getId());

        imageView.setColorFilter(res.getColor(color.getResourceId()));
        return convertView;
    }
}
