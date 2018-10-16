package com.crazyj36.apiplaygroundsdk9;

import android.app.Activity;
import android.Manifest;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.pm.PackageManager;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.Date;
import java.util.Locale;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;

/* Ideas:
   Flashing Text
   Text Manipulation
*/

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    // permission and file
	    String[] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};

        if ((android.os.Build.VERSION.SDK_INT > 22) && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(permission, 0);
            Toast.makeText(MainActivity.this, "Please restart app", Toast.LENGTH_LONG).show();
            finish();
        }

        String storage = Environment.getExternalStorageDirectory().toString() + "/test";
	    String filesTest = "Your files from " + storage + "/tests are in a list below";
        File dir = new File(storage); // check to make sure is folder not file, app will crash
        File[] fileList = dir.listFiles(); // try to sort by name. initially sorted by date.
        if (dir.exists()) {
            if (fileList.length == 0) {
                filesTest = ("No files in " + storage + ".");
            }
            String[] names = new String[fileList.length];
            for (int i = 0; i < fileList.length; i++) {
                names[i] = fileList[i].getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
            ListView listView = findViewById(R.id.docList);
            listView.setAdapter(adapter);
        } else {
            filesTest = ("No " + storage + " directory.");
        }

        // Name of app-wide log tag
        final String logChannel = "APIPLAYGROUNDLOG";

        // Vibrate on Successfull start
        String vibrateTest = null;
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        try {
            if (vib != null && android.os.Build.VERSION.SDK_INT > 25) {
                //vib.vibrate(500);  // .vibrate(long) is deprecated
                vib.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else if (vib != null && android.os.Build.VERSION.SDK_INT < 26) {
                vib.vibrate(500);
            }
        } catch (NullPointerException nullPointerException) {
                Toast.makeText(this, "Vibrate: " + nullPointerException.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } finally {
                vibrateTest = "Vibrator access tests done";
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

        // Normal Dialog Builder Example
        Button btnDialog = findViewById(R.id.btnDialog);
        btnDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.create();
                alertDialog.setTitle("A Dialog Title");
		alertDialog.setMessage(R.string.normalDialogTxt);
                alertDialog.setCancelable(true);
                alertDialog.show();
           }
        });

        // Concatenate two strings, c-like
        final TextView tvSet = findViewById(R.id.tvSet);
        String txt1 = "Hello";
        String txt2 = "World";
        tvSet.setText(String.format(Locale.US, "%s %s!", txt1, txt2));

        // Logs
        String startMsg = "App Started";
        Log.i(logChannel, startMsg);
        Button btnLog = findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(logChannel, "Log Button clicked");
            }
        });

        // Java package Date()
        final TextView tvDate = findViewById(R.id.tvDate);
        Button btnDate = findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = new Date().toString();
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
                vibrateTest,
                filesTest,
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
        if (android.os.Build.VERSION.SDK_INT > 22) {
            resultsTextView.setTextColor(getResources().getColor(android.R.color.black, null)); // Resources.Theme=null
            resultsTextView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray, null));
        } else {
            resultsTextView.setTextColor(getResources().getColor(android.R.color.black));
            resultsTextView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
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
