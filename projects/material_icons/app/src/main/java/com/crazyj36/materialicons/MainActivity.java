package com.crazyj36.materialicons;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void contacts_func(View view) {
        Toast.makeText(this, getString(R.string.contactBtnDesc), Toast.LENGTH_SHORT).show();
    }

    public void clock_func(View view) {
        Toast.makeText(this, getString(R.string.clockBtnDesc), Toast.LENGTH_SHORT).show();
    }

    public void android_func(View view) {
        Toast.makeText(this, getString(R.string.androidBtnDesc), Toast.LENGTH_SHORT).show();
    }
}
