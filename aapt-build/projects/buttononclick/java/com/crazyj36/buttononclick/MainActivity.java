package com.crazyj36.buttononclick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

        Button btnJavaClick = findViewById(R.id.btnJavaClick);
        btnJavaClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generalToast("java onclick");
            }
        });

    }

    public void btnXmlClick(View view) {
        generalToast("xml onclick");
    }

    public void generalToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
