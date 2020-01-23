package com.crazyj36.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.app.Dialog;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

    }

    public void showDialog(View v) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Dialog");
        dialog.show();
    }
}
