package com.thinkdevs.designmymfcommon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Logo;

import java.util.List;


public class GridLogoAdapter extends ArrayAdapter<Logo> {

    Resources res;
    private long mCurrentIconId;

    public GridLogoAdapter(Context context, List<Logo> logos, long currentIconId){
        super(context, R.layout.icon_grid_decor_logo, logos);
        res = context.getResources();
        mCurrentIconId = currentIconId;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Logo logo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.icon_grid_decor_logo, null);
        }

        ImageView imageView = (ImageView)(convertView.findViewById(R.id.imageView));
        imageView.setImageResource(logo.getResourceId());
        if(mCurrentIconId == logo.getId())
            imageView.setColorFilter(res.getColor(R.color.teal));
        else
            imageView.setColorFilter(res.getColor(R.color.grey));

        convertView.setTag(R.string.tag_icon_id, logo.getId());

        return convertView;
    }
}
