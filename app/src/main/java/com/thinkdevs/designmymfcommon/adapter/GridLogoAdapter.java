package com.thinkdevs.designmymfcommon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Icon;

import java.util.List;


public class GridLogoAdapter extends ArrayAdapter<Icon> {

    Resources res;
    private long mCurrentIconId;

    public GridLogoAdapter(Context context, List<Icon> icons, long currentIconId){
        super(context, R.layout.icon_grid_decor_logo, icons);
        res = context.getResources();
        mCurrentIconId = currentIconId;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Icon icon = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.icon_grid_decor_logo, null);
        }

        ImageView imageView = (ImageView)(convertView.findViewById(R.id.imageView));
        imageView.setImageResource(icon.getResourceId());
        if(mCurrentIconId == icon.getId())
            imageView.setColorFilter(res.getColor(R.color.teal));
        else
            imageView.setColorFilter(res.getColor(R.color.grey));

        convertView.setTag(R.string.tag_icon_id, icon.getId());

        return convertView;
    }
}
