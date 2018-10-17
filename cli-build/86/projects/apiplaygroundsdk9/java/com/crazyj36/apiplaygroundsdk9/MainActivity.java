package com.crazyj36.apiplaygroundsdk9;

import android.app.Activity;
import android.Manifest;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Build;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.view.DragEvent;
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
    String filesTest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    // storage permission and files
        if ((android.os.Build.VERSION.SDK_INT > 22) && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        if (Build.VERSION.SDK_INT < 23) {
            filesTask();
        } else {
	        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
		        filesTask();
	        } else {
	            filesTest = "Grant storage permission, then restart app.";
	        }
        }

        // Name of app-wide log tag
        final String logChannel = "APIPLAYGROUNDLOG";

        // Vibrate on Successfull start
        String vibrateTest = "";
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        try {
            if (Build.VERSION.SDK_INT < 11 && vib != null) {
				vib.vibrate(400);
                vibrateTest = "Vibrated for 400ms. Can't check for vibrate hardware. Though this is not null.";
            } else if (Build.VERSION.SDK_INT >= 26 && vib != null && vib.hasVibrator()) {
 		        vib.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE));
				vibrateTest = "Hasvibrator. Vibrated for 400ms, at default amplitude using class VibrationEffect.";
            } else if (Build.VERSION.SDK_INT >= 26 && vib != null && vib.hasVibrator() && vib.hasAmplitudeControl()) {
				vib.vibrate(VibrationEffect.createOneShot(400, 10));
                vibrateTest = "Has vibrator. Vibrated for 400ms at %15 available amplitude using class Vibrator.";
            } else if (Build.VERSION.SDK_INT < 26 && Build.VERSION.SDK_INT >= 11 && vib != null && vib.hasVibrator()) {
                vib.vibrate(400);
				vibrateTest = "Has vibrator. Vibrated for 400ms at default amplitude using class Vibrator.";
            } else if (Build.VERSION.SDK_INT >= 11 && !(vib.hasVibrator())) { // Checks NullPointerException next in catch.
				vibrateTest = "Vibrate: Hardware has no motor.";
            }
        } catch (NullPointerException nullPointerException) {
                vibrateTest = "Vibrate 'null' error: " + nullPointerException.getLocalizedMessage();
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
        String txt1 = "One";
        String txt2 = "Two";
        tvSet.setText(String.format(Locale.US, "%s %s printed!", txt1, txt2));

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

    public void filesTask() {
		ListView listView = findViewById(R.id.fileList);
	    String storage = Environment.getExternalStorageDirectory().toString() + "/test";
        filesTest = "Files from: " + storage + " are below.";
        File dir = new File(storage); // check to make sure test is folder, not file, app will crash
        File[] fileList = dir.listFiles(); // try to sort by name. initially sorted by date.
        if (dir.exists()) {
            if (fileList.length == 0) {
                filesTest = ("No files in " + storage + ".");
            }
            String[] names = new String[fileList.length];
            for (int i = 0; i < fileList.length; i++) {
                names[i] = fileList[i].getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
        } else {
            filesTest = ("No " + storage + " directory.");
        }

        // Disable TouchEvent on ScrollView(which is current parent view) on ACTION_DOWN (tap down).
        if (Build.VERSION.SDK_INT < 21) {
	        listView.setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
							// Disallow ScrollView to intercept touch events.
	                		v.getParent().requestDisallowInterceptTouchEvent(true);
  							break;
     					case MotionEvent.ACTION_UP:
							v.getParent().requestDisallowInterceptTouchEvent(false);
							break;
				    }
	                // Handle ListView touch events.
    				v.onTouchEvent(event);
					return true;
                }
			});
		}

    }

}
