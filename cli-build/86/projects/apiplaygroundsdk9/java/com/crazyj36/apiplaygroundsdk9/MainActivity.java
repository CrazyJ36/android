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
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.net.Uri;
import java.util.Date;
import java.util.Locale;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ArrayIndexOutOfBoundsException;
import java.io.IOException;
import java.io.File;

public class MainActivity extends Activity {

    // Strings for methods outside of onCreate()
    String filesTest = "";
    String openFileTest = "";
    String logChannel = "APIPLAYGROUNDLOG";

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

        // Vibrate on Successfull start
        String vibrateTest = "";
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        try {
            if (Build.VERSION.SDK_INT < 11 && vib != null) {
				vib.vibrate(100);
                vibrateTest = "Vibrated for 100ms. Can't check for vibrate hardware. Success (Not null).";
            } else if (Build.VERSION.SDK_INT >= 26 && vib != null && vib.hasVibrator()) {
 		        vib.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
				vibrateTest = "Hasvibrator. Vibrated for 200ms, at default amplitude using class VibrationEffect.";
            } else if (Build.VERSION.SDK_INT >= 26 && vib != null && vib.hasVibrator() && vib.hasAmplitudeControl()) {
				vib.vibrate(VibrationEffect.createOneShot(1000, 10));
                vibrateTest = "Has vibrator. Vibrated for 1000ms at %15 available amplitude using class Vibrator.";
            } else if (Build.VERSION.SDK_INT < 26 && Build.VERSION.SDK_INT >= 11 && vib != null && vib.hasVibrator()) {
                vib.vibrate(300);
				vibrateTest = "Has vibrator. Vibrated for 300ms at default amplitude using class Vibrator.";
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

        // Log button click
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

        // Set textview to what was entered in EditText
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
                openFileTest,
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
    // Int tests.
    public String intCast() {
        int mint = 1 + 2;
        return String.valueOf(mint);
    }
    // String tests.
    public String packageNameOfString() {
        return String.class.getCanonicalName();
    }

    // Get files from sdcard, show names in listview
    public void filesTask() {
		ListView listView = findViewById(R.id.fileList);
	    String storage = Environment.getExternalStorageDirectory().toString() + "/test";
        filesTest = "Files from: " + storage + " are below.";
        File dir = new File(storage);
        final File[] fileList = dir.listFiles(); // try to sort by name. initially sorted by date.
        if (dir.exists() && dir.isDirectory()) {
            if (fileList.length == 0) {
                filesTest = ("No files created in " + storage + ".");
            }
            String[] names = new String[fileList.length];
            for (int i = 0; i < fileList.length; i++) {
                names[i] = fileList[i].getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, names);
            View headerView = View.inflate(MainActivity.this, R.layout.list_header_view, null); // Get xml layout, make it inflatable here. null parent view.
            listView.addHeaderView(headerView);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
				public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
					openFileTest = String.valueOf(position);
					if (position > 0) {
						int filePosition = position - 1;
						Uri uri = null;
						if (Build.VERSION.SDK_INT < 25) {
							uri = Uri.fromFile(fileList[filePosition]);
				            Intent viewFile = new Intent();
				            viewFile.setDataAndType(uri, "text/plain");
		                    viewFile.setAction(Intent.ACTION_VIEW);
						    startActivity(viewFile);
						}
					}
                }
            });

        } else {
            filesTest = ("No " + storage + " directory. Create it to see corresponding files.");
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
