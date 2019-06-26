package com.crazyj36.testfloat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.app.ActivityOptions;
import android.graphics.Rect;
import android.content.Intent;

//import static android.app.ActivityOptions.*;
import static android.content.Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT;
import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                aToast();
                Rect rectangle = new Rect(0, 100, 100, 100);
                Intent launchMain = new Intent(MainActivity.this, MainActivity.class);
                launchMain.setFlags(FLAG_ACTIVITY_MULTIPLE_TASK | FLAG_ACTIVITY_LAUNCH_ADJACENT);
                ActivityOptions.makeBasic().setLaunchBounds(rectangle);
                startActivity(launchMain, savedInstanceState);
            }
        });
    }

    public void aToast() {
        Toast.makeText(MainActivity.this, "btn1",
            Toast.LENGTH_SHORT).show();
    }
}

