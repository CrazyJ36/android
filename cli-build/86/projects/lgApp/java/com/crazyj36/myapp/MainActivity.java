package com.crazyj36.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   	setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.tv1);
        // you can use local resource variables using: getResources().getType(int id);
        tv.setTextColor(getResources().getColor(R.color.mred));
        // my self-created R.color.mred resource id is in the file: res/values/colors.xml
        // Or use the built-in(also no need for imports, which is neat):
        // android.graphics.Color.BLUE);
        tv.setText("AndroidManifest.xml: Wallpaper can show through with <activity android:theme=\"@android:style/Theme.Wallpaper\"");


	//              casting                          or error: View cannot be converted into textview(pre nougat)
	TextView tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setText("tv2");

        Toast.makeText(this, "Done Loading", Toast.LENGTH_SHORT).show();
    }
}
