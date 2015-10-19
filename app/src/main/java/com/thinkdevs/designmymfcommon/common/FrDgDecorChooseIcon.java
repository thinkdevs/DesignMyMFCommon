package com.thinkdevs.designmymfcommon.common;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Icon;

import java.util.List;

public class FrDgDecorChooseIcon extends DialogFragment
        implements AdapterView.OnItemClickListener {

    private static ChooseIconDialogListener sListener;
    private static long sCurrentIconId;
    private static int sTypeIcon;

    public static FrDgDecorChooseIcon newInstance (ChooseIconDialogListener listener, long currentIconId, int typeIcon){

        sListener      = listener;
        sCurrentIconId = currentIconId;
        sTypeIcon      = typeIcon;

        return new FrDgDecorChooseIcon();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Выберете иконку");

        View     view     = inflater.inflate(R.layout.fragment_dialog_choose_decor, null);
        GridView gridView = (GridView) view.findViewById(R.id.gvDecor);

        List<Icon> icons;
        if(sTypeIcon == Icon.TYPE_CASH_ACCOUNT)
            icons = Icon.getCashAccountIcons();
        else
            icons = Icon.getCategoryIcons();
        ArrayAdapter<Icon> adapter = new AdGrIcons(getActivity(), icons, sCurrentIconId);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sListener.onChooseIcon((long)view.getTag(R.string.tag_icon_id));
        dismiss();
    }

    public interface ChooseIconDialogListener {
        void onChooseIcon(long iconId);
    }
}
