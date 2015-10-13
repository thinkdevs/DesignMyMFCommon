package com.thinkdevs.designmymfcommon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Category;

import java.util.List;

public class ListCategoriesSpinnerAdapter extends ArrayAdapter<Category> {

    private Resources mResources;

    public ListCategoriesSpinnerAdapter(Context context, List<Category> categories){
        super(context, R.layout.card_category, categories);
        mResources = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.card_category, null);
        }

        //Сохранение id категории
        CardView cardView = (CardView) convertView.findViewById(R.id.cv_parent_category);
        cardView.setTag(R.string.tag_category_id, category.getId());

        //Цвет фона логотипа
//        FrameLayout flLogo = (FrameLayout)convertView.findViewById(R.id.fl_logo);
//        flLogo.setBackgroundColor(
//                (mResources.getColor(category.getColor().getResourceId())));
//        flLogo.setTag(
//                R.string.tag_resource_id, (category.getColor().getResourceId()));

        GradientDrawable drawable = (GradientDrawable)convertView.findViewById(R.id.iv_category_logo).getBackground();
        drawable.setColor(mResources.getColor(category.getColor().getResourceId()));

        //Логотип
        ImageView ivCategoryLogo = (ImageView) convertView.findViewById(R.id.iv_category_logo);
        ivCategoryLogo.setImageResource(category.getIcon().getResourceId());
        ivCategoryLogo.setTag(R.string.tag_resource_id, category.getIcon().getResourceId());

        //Имя категории
        TextView tvCategoryName = (TextView) convertView.findViewById(R.id.tv_category_name);
        tvCategoryName.setText(category.getName());

        //Скрытие счетчика подкатегорий
        convertView.findViewById(R.id.tv_amount).setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
