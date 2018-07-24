package com.crazyj36.myapplication;

import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static final String EXTRA_MESSAGE = "com.crazyj36.myapplication.EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn1.setOnLongClickListener(this);
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        Button btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                Toast.makeText(this, "btn 1 clicked",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn2:
                finish();
                break;
            case R.id.btn3:
                Intent showAlarms = new Intent
                        (AlarmClock.ACTION_SHOW_ALARMS);
                if (showAlarms.resolveActivity
                        (getPackageManager()) != null) {
                    startActivity(showAlarms);
                    break;
                }
        }
    }
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Intent tstAct = new Intent(this, TextActivity.class);
                EditText edit1 = (EditText) findViewById(R.id.editText1);
                String message = edit1.getText().toString();
                tstAct.putExtra(EXTRA_MESSAGE, message);
                startActivity(tstAct);
                break;
        }
        return (false);
    }
}