package com.thinkdevs.designmymfcommon.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.adapter.GridColorAdapter;
import com.thinkdevs.designmymfcommon.database.Color;

import java.util.List;

public class ChooseDecorColorDialogFragment extends DialogFragment
        implements AdapterView.OnItemClickListener {

    private static ChooseColorDialogListener sListener;
    private static long sCurrentColorId;

    public static ChooseDecorColorDialogFragment newInstance (ChooseColorDialogListener listener, long currentColorId){

        sListener = listener;
        sCurrentColorId = currentColorId;

        return new ChooseDecorColorDialogFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Выберете цвет");

        View     view     = inflater.inflate(R.layout.fragment_dialog_choose_decor, null);
        GridView gridView = (GridView) view.findViewById(R.id.gvDecor);

        List<Color> colors = Color.getColorsWithoutSystems();
        ArrayAdapter<Color> adapter = new GridColorAdapter(getActivity(), colors, sCurrentColorId);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sListener.onChooseColor((long)view.getTag(R.string.tag_color_id));
        dismiss();
    }

    public interface ChooseColorDialogListener {
        void onChooseColor(long colorId);
    }
}
