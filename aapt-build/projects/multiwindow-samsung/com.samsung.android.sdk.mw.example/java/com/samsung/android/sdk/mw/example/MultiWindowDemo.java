package com.samsung.android.sdk.mw.example;

import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.samsung.android.sdk.multiwindow.SMultiWindow;

public class MultiWindowDemo extends ListActivity {
    private static final int MENU_IS_MULTI_WINDOW_SUPPORT = 0;
    private static final int MENU_MULTI_WINDOW_FUNCTION = 1;
    private SMultiWindow mMultiWindow = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter(this, 17367043, new String[]{"1. Is MultiWindow Support", "2. MultiWindow Function"}));
        getListView().setTextFilterEnabled(true);
        this.mMultiWindow = new SMultiWindow();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.multi_window_example, menu);
        return true;
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            this.mMultiWindow.initialize(this);
            if (position == 0) {
                displayPopup(position);
            } else if (position == MENU_MULTI_WINDOW_FUNCTION) {
                startActivity(new Intent(this, MultiWindowDemoFunction.class));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Unsupported device", MENU_MULTI_WINDOW_FUNCTION).show();
        }
    }

    private void displayPopup(int position) {
        Builder builder = new Builder(this);
        builder.setTitle(getTitle(position));
        builder.setMessage(getDemoResult(position));
        builder.setNeutralButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private String getTitle(int position) {
        switch (position) {
            case MENU_IS_MULTI_WINDOW_SUPPORT /*0*/:
                return "Is MultiWindow Support";
            default:
                return "MultiWindowDemo";
        }
    }

    private String getDemoResult(int position) {
        if (this.mMultiWindow == null) {
            return null;
        }
        switch (position) {
            case MENU_IS_MULTI_WINDOW_SUPPORT /*0*/:
                if (this.mMultiWindow.isFeatureEnabled(MENU_MULTI_WINDOW_FUNCTION)) {
                    return "This device support MultiWindow";
                }
                return "This device doesn't support MultiWindow";
            default:
                return "MultiWindowDemo";
        }
    }
}
