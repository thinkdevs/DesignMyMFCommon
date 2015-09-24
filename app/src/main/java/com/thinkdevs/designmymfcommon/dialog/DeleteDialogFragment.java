package com.thinkdevs.designmymfcommon.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.thinkdevs.designmymfcommon.adapter.RecyclerViewCashAccountsAdapter;

public class DeleteDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "myLogs";
    private static NoticeDialogListener listener;
    private static String message;

    public interface NoticeDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
    }

    private RecyclerViewCashAccountsAdapter adapter;

    public static final DeleteDialogFragment newInstance (NoticeDialogListener listener, String message){
        DeleteDialogFragment.listener = listener;
        DeleteDialogFragment.message = message;
        return new DeleteDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Удалить?");
        adb.setMessage(message);
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
