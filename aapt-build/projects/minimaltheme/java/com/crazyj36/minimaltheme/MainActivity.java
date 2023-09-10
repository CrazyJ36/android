package com.crazyj36.minimaltheme;

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

    public void toast(View view) {
        Toast.makeText(MainActivity.this, "Toast", 0).show();
    }
}
