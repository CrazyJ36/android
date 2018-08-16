package com.crazyj36.javaandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.util.Date;

public class MainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

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
