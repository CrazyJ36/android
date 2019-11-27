package com.crazyj36.readbuiltinfile;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView instruct = findViewById(R.id.instruct);
        TextView textview = findViewById(R.id.infoTxt);

        InputStream inputStream = getResources().openRawResource(R.raw.text);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int len;

        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "IO Exception. Check built-in text file.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        String string = "\n" + outputStream.toString();
        instruct.setText(R.string.instructions);
        textview.setText(string);
    }
}
