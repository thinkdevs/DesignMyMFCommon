package com.thinkdevs.designmymfcommon.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.thinkdevs.designmymfcommon.activitycashaccounts.CashAccountsRecyclerViewAdapter;

public class DialogDelete extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "myLogs";
    private static NoticeDialogListener listener;

    public interface NoticeDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
    }

    private  CashAccountsRecyclerViewAdapter adapter;

    public static final DialogDelete newInstance (NoticeDialogListener listener){
        DialogDelete.listener = listener;
        return new DialogDelete();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Удалить?");
        adb.setMessage("При удалении счета будут удалены все операции");
        adb.setPositiveButton("Да", this);
        adb.setNegativeButton("Нет", this);
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                Log.d(LOG_TAG, "Positive");
                listener.onDialogPositiveClick();
                break;
            case Dialog.BUTTON_NEGATIVE:
                Log.d(LOG_TAG, "Negative");
                listener.onDialogNegativeClick();
                break;
            default:
                break;
        }
    }
}
