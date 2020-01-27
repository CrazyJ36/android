package com.crazyj36.resumecounter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        count = count + 1;
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onResume() {
        Toast.makeText(MainActivity.this, String.valueOf(count), Toast.LENGTH_SHORT).show();
        super.onResume();
    }
}
