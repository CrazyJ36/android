package com.crazyj36.usemethod;

import android.app.Activity;
import android.widget.TextView;
import android.os.Bundle;
import com.crazyj36.usemethod.R;
import android.widget.Toast;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    myToast();
  } 
  final void myToast() {
    int duration = Toast.LENGTH_SHORT;
    String txt = "Hi TJ";
    Toast tst = Toast.makeText(this, txt, duration);
    tst.show();
  };
}
