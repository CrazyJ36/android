// this file is is not proper due to a poor decompiler, some sections may need to be fixed.
package com.crazyj36.celebrateyourself;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button2 = (Button) findViewById(R.id.celebrate);
        ((Button) findViewById(R.id.btn)).setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn /*2131427422*/:
                EditText name = (EditText) findViewById(R.id.nameField);
                ((TextView) findViewById(R.id.response)).setText(name.getText().toString() + ", my name is probably cooler");
                Toast.makeText(this, "celebrate about it, " + name.getText().toString(), 0).show();
                ((Button) findViewById(R.id.celebrate)).setVisibility(0);
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.celebrate /*2131427424*/:
                ((Button) findViewById(R.id.wag)).setVisibility(0);
                ((ImageView) findViewById(R.id.wagImg)).setVisibility(0);
                ((TextView) findViewById(R.id.rude)).setVisibility(0);
                break;
        }
        ((Button) findViewById(R.id.wag)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageView imageView = (ImageView) MainActivity.this.findViewById(R.id.wagImg);
                imageView.setBackgroundResource(R.drawable.blink);
                ((AnimationDrawable) imageView.getBackground()).start();
            }
        });
    }
}
