package com.thinkdevs.designmymfcommon.categories;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Spinner;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.common.FrDgNotice;

public class SpHierarchy extends Spinner implements FrDgNotice.NoticeDialogListener {

    private static final String LOG_TAG = "SpHierarchy";
    private static boolean mDebug = true;

    static final int INDEX_PARENT   = 0;
    static final int INDEX_CHILD    = 1;

    private Context mContext;
    private boolean mHasShowNotice;
    private FrDgNotice mDialogNotice;
    private boolean userAllowChange;
    private boolean mAllowNotice;

    public SpHierarchy(Context context) {
        super(context);
    }

    public SpHierarchy(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setAllowNotice(boolean allowNotice) {
        this.mAllowNotice = allowNotice;
    }

    @Override
    public boolean performClick() {
        if(mDebug) {
            Log.d(LOG_TAG, "'performClick()'");
        }
        if((!mHasShowNotice || !userAllowChange)
                && getSelectedItemPosition() == INDEX_PARENT && mAllowNotice) {
            mDialogNotice = FrDgNotice.newInstance(
                    SpHierarchy.this,
                    mContext.getString(R.string.dg_title_change_hierarchy),
                    mContext.getString(R.string.msg_change_hierarchy_category),
                    mContext.getString(R.string.dg_btn_positive),
                    mContext.getString(R.string.dg_btn_negative),
                    null);
            mDialogNotice.show(((Activity) mContext).getFragmentManager(), "dialog_notice");
            return true;
        }
        else {
            super.performClick();
            return true;
        }
    }


    @Override
    public void onDialogPositiveClick() {
        if(mDebug) {
            Log.d(LOG_TAG, "'onDialogPositiveClick()'");
        }
        mHasShowNotice = true;
        userAllowChange = true;
        super.performClick();
    }

    @Override
    public void onDialogNegativeClick() {
        if(mDebug) {
            Log.d(LOG_TAG, "'onDialogNegativeClick()'");
        }
        userAllowChange = false;
    }

    @Override
    public void onDialogNeutralClick() {

    }
}
