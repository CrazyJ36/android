package com.crazyj36.javaandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      final TextView textView = findViewById(R.id.textView);
      final TextView textView1 = findViewById(R.id.textView1);

	// Concatenate two strings, initialize main textview for buttons output)
      String text1 = "Hello";
      String text2 = "World";
      textView.setText(text1 + " " + text2);

        // adb Logging examples:
        /* example code:
           .java: Log.v("MYLOG", "string")
           $ use adb logcat MYLOG *:s
           MYLOG being the first 'TAG' parameter that logcat outputs
        */

      String startMsg = "App Started";
      Log.i("JAVAANDROIDLOG",startMsg);

      Button button = findViewById(R.id.button);
      button.setText("Log");
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Log.i("JAVAANDROIDLOG", "Log Button clicked");
          }
      });

      Button button2 = findViewById(R.id.button2);
      button2.setText("display");
      button2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          textView1.setText("TextView Shown");

        }
      });

      final EditText editText = findViewById(R.id.editText);
      final TextView textView2 = findViewById(R.id.textView2);
      Button button3 = findViewById(R.id.button3);
      button3.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              textView2.setText(editText.getText());
          }
      });

    }
}
