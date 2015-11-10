package com.thinkdevs.designmymfcommon.categories;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkdevs.designmymfcommon.DividerItemDecoration;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class FrDgChildCategories extends DialogFragment
        implements View.OnClickListener {

    private static final String LOG_TAG = "FrDgChildCategories";
    private static boolean mDebug = true;

    public static final int REQUEST_CODE_ADD_CHILD    = 3;
    public static final int REQUEST_CODE_CHANGE_CHILD = 4;

    private RecyclerView rvChilds;
    private AdRvChildCategories mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static AdRvParentCategories sAdapterParent; //для обновления списка;

    private long mParentId;
    private int mParentPosition;
    private Category parent;
    private List<Category> mChilds;

    RelativeLayout rlTitleBar;
    ImageView ivLogo;
    TextView  tvName;
    Button    btnNew;

    public static FrDgChildCategories newInstance (long parentId,
                                                   int parentPosition,
                                                   AdRvParentCategories adapterParent){

        FrDgChildCategories dialogFragment = new FrDgChildCategories();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.CATEGORY_ID ,parentId);
        bundle.putInt(Constants.CATEGORY_POSITION, parentPosition);
        dialogFragment.setArguments(bundle);
        sAdapterParent = adapterParent;
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentId = getArguments().getLong(Constants.CATEGORY_ID);
        mParentPosition = getArguments().getInt(Constants.CATEGORY_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_dialog_child_categories, null);
        rlTitleBar    = (RelativeLayout) view.findViewById(R.id.rl_title_bar);
        ivLogo        = (ImageView) view.findViewById(R.id.iv_logo);
        tvName        = (TextView) view.findViewById(R.id.tv_name);
        btnNew        = (Button)view.findViewById(R.id.btn_new);
        btnNew.setOnClickListener(this);
        parent = Category.getById(mParentId);
        mChilds = parent.getChilds();

        rlTitleBar.setBackgroundColor(getResources().getColor(parent.getColor().getResourceId()));
        ivLogo.setImageResource(parent.getIcon().getResourceId());
        tvName.setText(parent.getName());

        rvChilds = (RecyclerView) view.findViewById(R.id.rv_sub_categories);

        rvChilds.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        rvChilds.setHasFixedSize(true);
        mLayoutManager  = new LinearLayoutManager(getActivity());
        rvChilds.setLayoutManager(mLayoutManager);

        mAdapter = new AdRvChildCategories(
                this, mChilds);
        rvChilds.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AcCreateCategory.class);
        intent.putExtra(Constants.OPEN_AS, Category.CREATE_CHILD);
        intent.putExtra(Constants.CATEGORY_ID, parent.getId());
        intent.putExtra(Constants.CATEGORY_TYPE, parent.getType());
        intent.putExtra(Constants.ACTIVITY_TITLE, getResources().getString(R.string.title_activity_new_category));
        this.startActivityForResult(intent, REQUEST_CODE_ADD_CHILD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) {
            return;
        }
        long id;
        int position;
        switch (requestCode){
            case REQUEST_CODE_ADD_CHILD:
                if(mDebug) {
                    Log.d(LOG_TAG, "REQUEST_CODE_ADD");
                }
                id = data.getLongExtra(Constants.CATEGORY_ID, 0);
                mAdapter.updateAfterAdd(id);
                sAdapterParent.updateAfterChange(mParentId);
                break;
            case REQUEST_CODE_CHANGE_CHILD:
                if(mDebug) {
                    Log.d(LOG_TAG, "REQUEST_CODE_CHANGE");
                }
                updateAfterChange(data);
                break;
        }
    }

    private void updateAfterChange(Intent data) {
        if(mDebug) {
            Log.d(LOG_TAG, "'updateAfterChange()'");
        }
        int  resultCode = data.getIntExtra(Constants.RESULT, 0);
        long id         = data.getLongExtra(Constants.CATEGORY_ID, 0);

        if(resultCode == AcCreateCategory.RESULT_UPDATE_CHILD){
            if(mDebug) {
                Log.d(LOG_TAG, "         UPDATE_CHILD");
            }
            mAdapter.updateAfterChange(id);
        }
        else if(resultCode == AcCreateCategory.RESULT_CHILD_TO_PARENT){
            if(mDebug) {
                Log.d(LOG_TAG, "         RESULT_CHILD_TO_PARENT");
            }
            mAdapter.updateAfterDelete(id);
            sAdapterParent.updateAfterAdd(id);
            long parentIdUpdate1 = data.getLongExtra(Constants.PARENT_ID_UPDATE_1, 0);
            sAdapterParent.updateAfterChange(parentIdUpdate1);
            dismiss();
        }
        else if(resultCode == AcCreateCategory.RESULT_CHILD_CHANGE_PARENT){
            if(mDebug) {
                Log.d(LOG_TAG, "         RESULT_CHILD_CHANGE_PARENT");
            }
            mAdapter.updateAfterDelete(id);
            long parentIdUpdate1 = data.getLongExtra(Constants.PARENT_ID_UPDATE_1, 0);
            long parentIdUpdate2 = data.getLongExtra(Constants.PARENT_ID_UPDATE_2, 0);
            sAdapterParent.updateAfterChange(parentIdUpdate1);
            sAdapterParent.updateAfterChange(parentIdUpdate2);
        }
    }


    /**
     * call when delete child, for update list parentCategories
     */
    public void onDeleteChild(){
        ((AdRvParentCategories)sAdapterParent).updateAfterChange(mParentId);
    }
}
