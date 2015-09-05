package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.thinkdevs.designmymfcommon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CashAccountsActivity extends Activity {

    private static final String TITLE = "title";
    private static final String ICON = "icon";

    private List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

    // Use this to add items to the list that the ListPopupWindow will use
    private void addItem(String title, int iconResourceId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(TITLE, title);
        map.put(ICON, iconResourceId);
        data.add(map);
    }

    private void showListMenu(View anchor) {
        ListPopupWindow popupWindow = new ListPopupWindow(this);
        ListAdapter adapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.activity_list_item, // You may want to use your own cool layout
                new String[] {TITLE, ICON}, // These are just the keys that the data uses
                new int[] {android.R.id.text1, android.R.id.icon}); // The view ids to map the data to
        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(adapter);
        popupWindow.setHorizontalOffset(-250);
        popupWindow.setWidth(300); // note: don't use pixels, use a dimen resource
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }); // the callback for when a list item is selected
        popupWindow.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_cards);
        addItem("Edit", R.drawable.ic_mode_edit_black_24dp);
        addItem("Remove", R.drawable.ic_delete_black_24dp);

        findViewById(R.id.iv_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListMenu(v);
            }
        });

//        findViewById(R.id.btn_expense).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View view = getLayoutInflater().inflate(R.layout.card_operation_popup_windows, null);
//                PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                ListView listView = (ListView) view.findViewById(R.id.lv_operations);
//
//                data = new ArrayList<HashMap<String, Object>>();
//                ListAdapter adapter = new SimpleAdapter(
//                        CashAccountsActivity.this,
//                        data,
//                        android.R.layout.activity_list_item, // You may want to use your own cool layout
//                        new String[] {TITLE, ICON}, // These are just the keys that the data uses
//                        new int[] {android.R.id.text1, android.R.id.icon}); // The view ids to map the data to
//                addItem("Edit", R.drawable.ic_mode_edit_black_24dp);
//                addItem("Remove", R.drawable.ic_delete_black_24dp);
//                popupWindow.showAsDropDown(v, 0, 18, Gravity.CENTER);
//            }
//        });
    }
}
