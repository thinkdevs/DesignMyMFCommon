package com.thinkdevs.designmymfcommon.categories;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    public static final int REQUEST_CODE_ADD_CHILD    = 3;
    public static final int REQUEST_CODE_CHANGE_CHILD = 4;

    private RecyclerView rvChilds;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private long mParentId;
    private Category parent;
    private List<Category> childs;

    RelativeLayout rlTitleBar;
    ImageView ivLogo;
    TextView  tvName;
    Button    btnNew;

    public static FrDgChildCategories newInstance (long idCategory){

        FrDgChildCategories dialogFragment = new FrDgChildCategories();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.CATEGORY_ID ,idCategory);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentId = getArguments().getLong(Constants.CATEGORY_ID);
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
        childs = parent.getChilds();

        rlTitleBar.setBackgroundColor(getResources().getColor(parent.getColor().getResourceId()));
        ivLogo.setImageResource(parent.getIcon().getResourceId());
        tvName.setText(parent.getName());

        rvChilds = (RecyclerView) view.findViewById(R.id.rv_sub_categories);

        rvChilds.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        rvChilds.setHasFixedSize(true);
        mLayoutManager  = new LinearLayoutManager(getActivity());
        rvChilds.setLayoutManager(mLayoutManager);

        mAdapter = new AdRvChildCategories(
                this, childs);
        rvChilds.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AcCreateCategory.class);
        intent.putExtra(Constants.OPEN_AS, Category.CREATE_CHILD);
        intent.putExtra(Constants.CATEGORY_ID, parent.getId());
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
                id = data.getLongExtra(Constants.CATEGORY_ID, 0);
                ((AdRvChildCategories)mAdapter).updateAfterAdd(id);
                break;
            case REQUEST_CODE_CHANGE_CHILD:
                id = data.getLongExtra(Constants.CATEGORY_ID, 0);
                position = data.getIntExtra(Constants.CATEGORY_POSITION, 0);
                ((AdRvChildCategories)mAdapter).updateAfterChange(id, position);
                break;
        }
    }
}
