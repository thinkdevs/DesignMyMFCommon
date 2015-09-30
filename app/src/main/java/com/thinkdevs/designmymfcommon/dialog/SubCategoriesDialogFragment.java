package com.thinkdevs.designmymfcommon.dialog;

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

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCategoryActivity;
import com.thinkdevs.designmymfcommon.adapter.RecyclerViewSubCategoriesAdapter;
import com.thinkdevs.designmymfcommon.database.ParentCategory;
import com.thinkdevs.designmymfcommon.database.SubCategory;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class SubCategoriesDialogFragment extends DialogFragment
        implements View.OnClickListener {

    private RecyclerView rvSubCategories;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static long idCategory;
    private ParentCategory parentCategory;
    private List<SubCategory> subCategories;

    RelativeLayout rlTitleBar;
    ImageView ivLogo;
    TextView  tvName;
    TextView  tvCount;
    Button    btnNew;



    public static SubCategoriesDialogFragment newInstance (
            Long idCategory){
        SubCategoriesDialogFragment.idCategory
                = idCategory;
        return new SubCategoriesDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_dialog_sub_categories, null);
        rlTitleBar    = (RelativeLayout) view.findViewById(R.id.rl_title_bar);
        ivLogo        = (ImageView) view.findViewById(R.id.iv_logo);
        tvName        = (TextView) view.findViewById(R.id.tv_name);
        tvCount       = (TextView) view.findViewById(R.id.tv_count);
        btnNew        = (Button)view.findViewById(R.id.btn_new);
        btnNew.setOnClickListener(this);
        parentCategory = ParentCategory.getById(idCategory);
        subCategories = parentCategory.getSubCategories();

        rlTitleBar.setBackgroundColor(getResources().getColor(parentCategory.getColor().getResourceId()));
        ivLogo.setImageResource(parentCategory.getLogo().getResourceId());
        tvName.setText(parentCategory.getName());
        StringBuilder sbCountSubCategories = new StringBuilder();
        sbCountSubCategories.append("( ").append(subCategories.size()).append(" )");
        tvCount.setText(sbCountSubCategories);

        rvSubCategories = (RecyclerView) view.findViewById(R.id.rv_sub_categories);
        rvSubCategories.setHasFixedSize(true);
        mLayoutManager  = new LinearLayoutManager(getActivity());
        rvSubCategories.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewSubCategoriesAdapter(
                getActivity(),
                subCategories);
        rvSubCategories.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
        intent.putExtra(Constants.IS_NEW, true);
        intent.putExtra(Constants.CATEGORY_ID, parentCategory.getId());
        intent.putExtra(Constants.ACTIVITY_TITLE, getResources().getString(R.string.action_new_category));
        startActivity(intent);
        dismiss();
    }

    public interface NoticeDialogListener {
        void onDialogOperationFromTempalateClick();
    }

}
