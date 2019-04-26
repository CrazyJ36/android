package com.crazyj36.buttoncase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

// Implement OnClickListener as the app is based on this. Must have it's own method OnClick.
public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        Button btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                Toast.makeText(this, "Button 1 Clicked", Toast.LENGTH_SHORT).show(); break;
            case R.id.btn2:
                Toast.makeText(this, "Button 2 Clicked", Toast.LENGTH_SHORT).show(); break;
            case R.id.btn3:
                Toast.makeText(this, "Button 3 Clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
