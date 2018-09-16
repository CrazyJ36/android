package com.crazyj36.apiplayground_oldsdk;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int sdk = android.os.Build.VERSION.SDK_INT;

        TextView tv1 = findViewById(R.id.tv1);
        tv1.setText(getString(R.string.tv1Txt) + sdk);

        Button button = findViewById(R.id.btn1);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("A Dialog");
                dialog.show();
                dialog.findViewById(R.id.dialogBtn1).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Dialog Button Pushed", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.findViewById(R.id.dialogBtn2).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        // Log app start in logcat (run logcat-this)
        String startMsg = "App Started";
        Log.i("JAVAANDROIDLOG",startMsg);

        // Concatenate two strings, initialize main textview for buttons output)
        final TextView tvSet = findViewById(R.id.tvSet);
        String txt1 = "Hello";
        String txt2 = "World";
        tvSet.setText(txt1 + " " + txt2 + "!");

        // Logging: adb Logging examples:
        /* example code:
           .java: Log.v("MYLOG", "string")
           use:
           $ adb logcat MYLOG *:s
           MYLOG being the first 'TAG' parameter that logcat outputs
        */
        Button btnLog = findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("JAVAANDROIDLOG", "Log Button clicked");
            }
        });


        //Example catch-all method from Exceptiona api. this does not throw any
        try {
            final String date = new Date().toString();
        }
        catch (Exception e) {
            Log.v("MYLOG", "Date init Error: " + e);
        }

        // Java package Date()
        final TextView tvDate = findViewById(R.id.tvDate);
        final String date = new Date().toString();
        Button btnDate = findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDate.setText(date);
            }
        });

        // set textview to what was entered in textview
        final EditText etPrint = findViewById(R.id.etPrint);
        final TextView tvPrint = findViewById(R.id.tvPrint);
        Button btnPrint = findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvPrint.setText(etPrint.getText());
            }
        });


    }
}
