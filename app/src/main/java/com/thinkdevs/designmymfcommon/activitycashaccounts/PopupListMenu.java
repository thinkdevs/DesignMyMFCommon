package com.thinkdevs.designmymfcommon.activitycashaccounts;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.thinkdevs.designmymfcommon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopupListMenu {

    private static final String TITLE = "title";
    private static final String ICON = "icon";
    private static Context context;

    private static List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();


    // Use this to add items to the list that the ListPopupWindow will use
    public static void addItem(String title, int iconResourceId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(TITLE, title);
        map.put(ICON, iconResourceId);
        data.add(map);
    }

    public static void showListMenu(View anchor) {
        ListPopupWindow popupWindow = new ListPopupWindow(context);
        ListAdapter adapter = new SimpleAdapter(
                context,
                data,
                android.R.layout.activity_list_item, // You may want to use your own cool layout
                new String[] {TITLE, ICON}, // These are just the keys that the data uses
                new int[] {android.R.id.text1, android.R.id.icon}); // The view ids to map the data to
        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(adapter);
        popupWindow.setHorizontalOffset(-250);
        popupWindow.setWidth((int) context.getResources().getDimension(R.dimen.menu_width)); // note: don't use pixels, use a dimen resource
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(
                        context,
                        parent.getRootView().toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }); // the callback for when a list item is selected
        popupWindow.show();
    }

    public static void setContext(Context context) {
        PopupListMenu.context = context;
        if(data.size() == 0) {
            addItem(
                    context.getResources().getString(R.string.edit),
                    R.drawable.ic_mode_edit_black_24dp);
            addItem(
                    context.getResources().getString(R.string.remove),
                    R.drawable.ic_delete_black_24dp);
        }
    }
}
