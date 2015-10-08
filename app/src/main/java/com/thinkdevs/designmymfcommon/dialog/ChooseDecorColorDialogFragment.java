package com.thinkdevs.designmymfcommon.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.GridColorAdapter;
import com.thinkdevs.designmymfcommon.database.Color;

import java.util.List;

public class ChooseDecorColorDialogFragment extends DialogFragment
        implements AdapterView.OnItemClickListener {

    public static ChooseDecorColorDialogFragment newInstance (){

        return new ChooseDecorColorDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View     view     = inflater.inflate(R.layout.fragment_dialog_choose_decor, null);
        TextView tvTitle  = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText("Выбирете цвет");
        GridView gridView = (GridView) view.findViewById(R.id.gvDecor);

        List<Color> colors = Color.getColors();
        ArrayAdapter<Color> adapter = new GridColorAdapter(getActivity(), colors);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
