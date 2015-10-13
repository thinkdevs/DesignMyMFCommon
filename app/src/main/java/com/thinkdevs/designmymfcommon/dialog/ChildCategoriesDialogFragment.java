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

import com.thinkdevs.designmymfcommon.DividerItemDecoration;
import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.activity.NewCategoryActivity;
import com.thinkdevs.designmymfcommon.adapter.RecyclerViewChildCategoriesAdapter;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.utills.Constants;

import java.util.List;

public class ChildCategoriesDialogFragment extends DialogFragment
        implements View.OnClickListener {

    private RecyclerView rvSubCategories;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static long idCategory;
    private Category parentCategory;
    private List<Category> subCategories;

    RelativeLayout rlTitleBar;
    ImageView ivLogo;
    TextView  tvName;
    TextView  tvCount;
    Button    btnNew;



    public static ChildCategoriesDialogFragment newInstance (
            Long idCategory){
        ChildCategoriesDialogFragment.idCategory
                = idCategory;
        return new ChildCategoriesDialogFragment();
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
        parentCategory = Category.getById(idCategory);
        subCategories  = parentCategory.getChilds();

        rlTitleBar.setBackgroundColor(getResources().getColor(parentCategory.getColor().getResourceId()));
        ivLogo.setImageResource(parentCategory.getIcon().getResourceId());
        tvName.setText(parentCategory.getName());

        rvSubCategories = (RecyclerView) view.findViewById(R.id.rv_sub_categories);

        rvSubCategories.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        rvSubCategories.setHasFixedSize(true);
        mLayoutManager  = new LinearLayoutManager(getActivity());
        rvSubCategories.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewChildCategoriesAdapter(
                getActivity(),
                subCategories);
        rvSubCategories.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
        intent.putExtra(Constants.OPEN_AS, Category.CREATE_CHILD);
        intent.putExtra(Constants.CATEGORY_ID, parentCategory.getId());
        intent.putExtra(Constants.ACTIVITY_TITLE, getResources().getString(R.string.title_activity_new_category));
        startActivity(intent);
        dismiss();
    }

    public interface NoticeDialogListener {
        void onDialogOperationFromTempalateClick();
    }

}
