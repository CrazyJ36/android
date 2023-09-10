package com.crazyj36.useanothermethod;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

        // OtherClass.java is already A part of this app package(com.crazyj36.useanothermethod),
        // so the java compiler can find OtherClass(), but it should be instantiated as new to get it.
        // .test() in OtherClass() simple returns some text, so we can its' result to TextView text.
        ((TextView)findViewById(R.id.tvResult)).setText(new OtherClass().test());
    }
}
