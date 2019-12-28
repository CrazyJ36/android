package com.crazyj36.buttonlongclick;

import android.app.Activity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.Toast;

import android.view.View;

// import onClick and onLongClick from View to enable click detection.
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // button initialization 'final(global)' as it's used in other java functions.
        final Button button = findViewById(R.id.btn);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                generalToast("Short Clicked");

                // setOnLongClickListener nested within onClick in case the click is long.
                button.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        generalToast("Long Clicked");
                        // Return boolean onLongClick with A value.
                        // true because if it was physically clicked, this must have ran.
                        // If this was false, the short onClick method would also run.
                        return true;
                    }
                });

            }
        }); // end of 'new OnClickListener() {}'

    } // end of onCreate()


public void generalToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
