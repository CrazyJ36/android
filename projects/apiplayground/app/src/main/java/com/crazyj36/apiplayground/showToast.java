package com.crazyj36.apiplayground;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class showToast extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVisible(false);
        Toast.makeText(getApplicationContext(), "Widget Button Pressed", Toast.LENGTH_LONG).show();
        finishAndRemoveTask();
    }

}

