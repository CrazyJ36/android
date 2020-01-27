package com.crazyj36.buttoncase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

// implement onClickListener so that the base MainActivity class can use one onClick method.
public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // If I were to try to put these above, globally under MainActivity method,
        // then try to use them in onClick method below, error would be that case statements
        // require int, not Button. Their type is button.
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);

        // btn1.setOnClickListener(this) means that the onClick method for these buttons will be
        // attached to 'this' MainActivity class.
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // view.getId() is A method in the view class that gets this views'(MainActivity) ids,
        // including the buttons. Returns this views identifier.
        switch(view.getId()) {
            case R.id.btn1:
                // Must break after each case, or all cases would be run.
                myToast("btn1"); break;
            case R.id.btn2:
                myToast("btn2"); break;
            case R.id.btn3:
                myToast("btn3"); break;
        }
    }

    public void myToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}

