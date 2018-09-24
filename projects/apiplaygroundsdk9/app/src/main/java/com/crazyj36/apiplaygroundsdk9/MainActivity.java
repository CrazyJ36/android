package com.crazyj36.apiplaygroundsdk9;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* Ideas:
   Flashing Text
   Text Maniupulation
   change theme

*/
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        try {
            if (vib != null) {
                vib.vibrate(500);
            }
        } catch (NullPointerException nullPointerExcption) {
            Toast.makeText(this, "Vibrate: " + nullPointerExcption.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            Toast.makeText(this, "Load Vibrator.class Successful, Hardware Found. Done.", Toast.LENGTH_SHORT).show();
        }

        // Device SDK Version view
        TextView tvSdk = findViewById(R.id.tvSdk);
        tvSdk.setText(String.format(Locale.US, "%s %d", getString(R.string.tvSdkTxt), android.os.Build.VERSION.SDK_INT));

        // Read Textfile from res/raw
        TextView tvFile = findViewById(R.id.tvFile);
        InputStream inputStream = getResources().openRawResource(R.raw.file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte arrayBuff[] = new byte[64];
        int len;
        try {
            while ((len = inputStream.read(arrayBuff)) != -1) {
                outputStream.write(arrayBuff, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException ioException) {
            Toast.makeText(MainActivity.this, "IOException", Toast.LENGTH_SHORT).show();
        }
        String fileText = outputStream.toString();
        tvFile.setText(fileText);

        // Dialog with layout example
        Button btnCustom = findViewById(R.id.btnCustom);
        btnCustom.setOnClickListener(new OnClickListener() {
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

        // Concatenate two strings, c-like
        final TextView tvSet = findViewById(R.id.tvSet);
        String txt1 = "Hello";
        String txt2 = "World";
        tvSet.setText(String.format(Locale.US, "%s %s!", txt1, txt2));

        // Logs
        String startMsg = "App Started";
        Log.i("JAVAANDROIDLOG", startMsg);
        Button btnLog = findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("JAVAANDROIDLOG", "Log Button clicked");
            }
        });

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

        // set textview to what was entered in editText
        final EditText etPrint = findViewById(R.id.etPrint);
        final TextView tvPrint = findViewById(R.id.tvPrint);
        Button btnPrint = findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvPrint.setText(etPrint.getText());
            }
        });

        // From Here some function results will be put into a custom listview
        // Put results here from return value
        final String[] results = {
                "",
                "1 + 2 = " + intCast(),
                "String is from package: " + packageNameOfString(),

        };
        results[0] = String.valueOf(results.length);
        int z = 0;
        StringBuffer text = new StringBuffer();
        try {
            while (z < results.length) {
                text.append(results[z]);
                text.append("\n");
                z++;
            }
        } catch (NullPointerException ConcatNullPointer) {
            Toast.makeText(this, "My string concatenation caused NullPointerException", Toast.LENGTH_SHORT).show();
        }
        String buffOut = String.valueOf(text);
        TextView resultsTextView = findViewById(R.id.resultsTextView);
        resultsTextView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        resultsTextView.setText(buffOut);

    }

    public String intCast() {
        int mint = 1 + 2;
        return String.valueOf(mint);
    }

    public String packageNameOfString() {
        return String.class.getCanonicalName();
    }

}
