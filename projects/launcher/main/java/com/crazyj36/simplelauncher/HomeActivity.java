package com.crazyj36.simplelauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.attr.data;

/**
 * Created by CrazyJ36 on 11/19/2016.
 */

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void showAppsBtn(View arg0) {
        Intent makeShowAppsList = new Intent(HomeActivity.this, AppsListActivity.class);
        startActivity(makeShowAppsList);
    }
}

