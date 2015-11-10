package com.thinkdevs.designmymfcommon.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.thinkdevs.designmymfcommon.cashaccounts.AdRvCashAccounts;

public class FrDgNotice extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String LOG_TAG = "FrDgNotice";
    private static boolean mDebug = true;

    private static final String KEY_TITLE           = "title";
    private static final String KEY_MESSAGE         = "message";
    private static final String KEY_POSITIVE_BUTTON = "positive_button";
    private static final String KEY_NEGATIVE_BUTTON = "negative_button";
    private static final String KEY_NEUTRAL_BUTTON  = "neutral_button";

    private static NoticeDialogListener sListener;

    public interface NoticeDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
        void onDialogNeutralClick();
    }

    public static FrDgNotice newInstance(NoticeDialogListener listener,
                                         String title,
                                         String message,
                                         String positiveBtn,
                                         String negativeBtn,
                                         String neutralBtn){
        if(mDebug){
            Log.d(LOG_TAG, "'newInstance()'");
        }
        FrDgNotice.sListener = listener;
        FrDgNotice dialog = new FrDgNotice();
        Bundle bundle = new Bundle();
        if(title != null) {
            bundle.putString(KEY_TITLE, title);
        }
        if(message != null) {
            bundle.putString(KEY_MESSAGE, message);
        }
        if(positiveBtn != null) {
            bundle.putString(KEY_POSITIVE_BUTTON, positiveBtn);
        }
        if(negativeBtn != null) {
            bundle.putString(KEY_NEGATIVE_BUTTON, negativeBtn);
        }
        if(neutralBtn  != null) {
            bundle.putString(KEY_NEUTRAL_BUTTON, neutralBtn);
        }
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(mDebug){
            Log.d(LOG_TAG, "***** 'onCreateDialog()' *****");
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        Bundle arguments = getArguments();
        if(arguments.containsKey(KEY_TITLE)) {
            if(mDebug){
                Log.d(LOG_TAG, "        TITLE");
            }
            adb.setTitle(arguments.getString(KEY_TITLE));
        }
        if(arguments.containsKey(KEY_MESSAGE)) {
            if(mDebug){
                Log.d(LOG_TAG, "        MESSAGE");
            }
            adb.setMessage(arguments.getString(KEY_MESSAGE));
        }
        if(arguments.containsKey(KEY_POSITIVE_BUTTON)){
            if(mDebug){
                Log.d(LOG_TAG, "        POSITIVE_BUTTON");
            }
            adb.setPositiveButton(arguments.getString(KEY_POSITIVE_BUTTON), this);
        }
        if(arguments.containsKey(KEY_NEGATIVE_BUTTON)){
            if(mDebug){
                Log.d(LOG_TAG, "        NEGATIVE_BUTTON");
            }
            adb.setNegativeButton(arguments.getString(KEY_NEGATIVE_BUTTON), this);
        }
        if(arguments.containsKey(KEY_NEUTRAL_BUTTON)){
            if(mDebug){
                Log.d(LOG_TAG, "        NEUTRAL_BUTTON");
            }
            adb.setNeutralButton(arguments.getString(KEY_NEUTRAL_BUTTON), this);
        }
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(mDebug){
            Log.d(LOG_TAG, "'onClick()'");
        }
        if(which == Dialog.BUTTON_POSITIVE){
            if(mDebug){
                Log.d(LOG_TAG, "        BUTTON_POSITIVE");
            }
            sListener.onDialogPositiveClick();
        }
        else if(which == Dialog.BUTTON_NEGATIVE){
            if(mDebug){
                Log.d(LOG_TAG, "        BUTTON_NEGATIVE");
            }
            sListener.onDialogNegativeClick();
        }
        else if(which == Dialog.BUTTON_NEUTRAL){
            if(mDebug){
                Log.d(LOG_TAG, "        BUTTON_NEUTRAL");
            }
            sListener.onDialogNeutralClick();
        }
    }
}
