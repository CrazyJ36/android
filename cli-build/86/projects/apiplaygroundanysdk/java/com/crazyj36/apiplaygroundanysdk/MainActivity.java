/* TODO: lg: Crash on call button
		 make sure storage text gets updated when it needs to
*/
package com.crazyj36.apiplaygroundanysdk;

import android.app.Activity;
import android.os.Bundle;
import android.Manifest;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ActionBar;
import android.os.Build;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.RemoteViews;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.net.Uri;
import android.util.Log;
import java.lang.System;
import java.util.Date;
import java.util.Locale;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;

public class MainActivity extends Activity {
    // Strings for methods outside of onCreate()
	String filesTest = "";
    String logChannel = "APIPLAYGROUNDLOG";
    int sdkVersion = Build.VERSION.SDK_INT;
	public static final int MY_PERMISSION = 0;
	String[] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
    ListView listView;
	String vibrateTest = "";
	String concatTxt = "";
	String storage = Environment.getExternalStorageDirectory().toString() + "/notes";
    // onCreate activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ActionBar is usable in default platform from api 11.
        if (sdkVersion >= 11) {
            ActionBar actionBar = getActionBar();
			actionBar.setSubtitle("CrazyJ36");
        }
        // If xml android:nestedScrollingEnabled doesn't work, make some ui views scroll using custom method.
        if (sdkVersion < 21) {
			ScrollView scrollViewForRawFile = findViewById(R.id.scrollViewForRawFile);
        	makeScrollable(scrollViewForRawFile);
		}
        // Vibrate on Successfull start
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
                dialog.setTitle("C Description");
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
        Button btnDialog = findViewById(R.id.btnDialogCIdeas);
        btnDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.create();
                alertDialog.setTitle("C Program Ideas");
        		alertDialog.setMessage(R.string.dialogCIdeasTxt);
                alertDialog.setCancelable(true);
                alertDialog.show();
           }
        });
         // Concatenate two strings, c-like
        String txt1 = "One";
        String txt2 = "Two";
        concatTxt = String.format(Locale.US, "%s %s printed!", txt1, txt2); // or System.out.printf("%t", var); in console
        // Set TextView text from code, maybe not best practice.
        String tvHorizontalTxt = getResources().getString(R.string.tvHorizontalTxt);
        TextView horizontalTxt = findViewById(R.id.tvHorizontal);
		horizontalTxt.setText(tvHorizontalTxt);
        // Log in onCreate() and on button click.
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
		/*if (!etPrint.isFocused()) { // This doesn't work as isFocused() doesn't do what I expected (focused on editText click.)
			etPrint.setCursorVisible(true);
		} */
        final TextView tvPrint = findViewById(R.id.tvPrint);
        Button btnPrint = findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvPrint.setText(etPrint.getText());
            }
        });
		// Activity 2 start button
        Button act2Btn = findViewById(R.id.act2btn);
        act2Btn.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View act2BtnView) {
                Intent startSecondActivity = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(startSecondActivity);
            }
        });
        // Button Press and long click
		Button btnPressLong = findViewById(R.id.btnPressLong);
		btnPressLong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(MainActivity.this, "Short Tapped", Toast.LENGTH_SHORT).show();
			}
        });
		btnPressLong.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				Toast.makeText(MainActivity.this, "Long Pressed", Toast.LENGTH_SHORT).show();
                return(true);
			}
		});
        // Button that plays an audio file
		findViewById(R.id.btnPlayAudio).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View btnPlayAudioView) {
				final TextView tvPlayAudioStatus = findViewById(R.id.tvPlayAudioStatus);
 				MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.flute);
				mediaPlayer.start();
				if (mediaPlayer.isPlaying()) {
					tvPlayAudioStatus.setText(R.string.tvMediaPlayingTxt);
        		}
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		    		public void onCompletion(MediaPlayer mediaPlayer) {
						mediaPlayer.release();
						tvPlayAudioStatus.setText(R.string.tvMediaDoneTxt);
                        Runnable runThis = new Runnable() { // The Runnable Thing, Object
							@Override
							public void run() {
							    tvPlayAudioStatus.setText(R.string.tvMediaNotPlayingTxt); // This is the statement that will occur after handlers' delay.
							}
						};
						new Handler().postDelayed(runThis, 3000); // postDelayed is like "post to cpu later". runThis will run after 3 seconds
					}
				});
			}
		});
		// Notification Button
		final NotificationManager notificationManager = (NotificationManager) getSystemService("notification"); // getSystemService() string type ("vibrate") or class type (VIBRATOR_SERVICE)
        if (notificationManager != null) {
			if (sdkVersion >= 26) {
			    NotificationChannel notificationChannel = new NotificationChannel("APIPLAYGROUNDANYSDK_NOTIFICATION_CHANNEL", "Notify Button", NotificationManager.IMPORTANCE_DEFAULT);
				notificationChannel.shouldShowLights();
				notificationManager.createNotificationChannel(notificationChannel);
			}
			findViewById(R.id.btnNotify).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View thisBtn) {
					if (sdkVersion >= 11) {
						final Notification.Builder notifyBuild;
						if (sdkVersion > 25) {
							notifyBuild = new Notification.Builder(MainActivity.this, "APIPLAYGROUNDANYSDK_NOTIFICATION_CHANNEL");
						} else {
	                        notifyBuild = new Notification.Builder(MainActivity.this);
						}
						notifyBuild.setSmallIcon(R.drawable.top_text_drawable);
						notifyBuild.setContentTitle("A notification");
						notifyBuild.setContentText("This is the notification content");
						notificationManager.notify(0, notifyBuild.build());
					} else {
						Notification notify = new Notification(R.drawable.top_text_drawable, "API Playground Any SDK", System.currentTimeMillis());
					    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
						PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
						notify.contentIntent = pendingIntent;
						RemoteViews notifView = new RemoteViews(getPackageName(), R.layout.notification_layout);
                        notifView.setTextViewText(R.id.notificationTvTitle, "API Playground Any SDK");
						notifView.setTextViewText(R.id.notificationTvContent, "This is the notification content");
						notify.contentView = notifView;
						notificationManager.notify("APIPLAYGROUNDANYSDK_NOTIFICATION_CHANNEL", 0, notify);
					}
				}
			});
		} else {
			Toast.makeText(MainActivity.this, "Notification Manager service returned null", Toast.LENGTH_LONG);
		}
        // getStorage permission & start filesTask()
        if (sdkVersion < 23) {
            filesTask();
        } else {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				filesTest = "Requesting storage permission to show files";
	        	requestPermissions(permission, MY_PERMISSION);
			} else {
				filesTask();
			}
		}
		// put some function results into custom textview
		updateResults();
		listView = findViewById(R.id.fileList);
	// end of onCreate
	}
    // Get files from sdcard, show names in listview
    public void filesTask() {
		filesTest = "Files from " + storage + " are shown below";
		listView = findViewById(R.id.fileList);
        if (sdkVersion < 21) {
            makeScrollable(listView);
        }
        File dir = new File(storage);
        final File[] fileList = dir.listFiles(); // try to sort by name. initially sorted by date.
        if (dir.exists() && dir.isDirectory()) {
			if (fileList.length == 0) {
				listView.setEnabled(false);
                filesTest = ("No files created in " + storage + ".");
				return;
			}
            String[] names = new String[fileList.length];
            for (int i = 0; i < fileList.length; i++) {
                names[i] = fileList[i].getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, names);
            View headerView = View.inflate(MainActivity.this, R.layout.list_header_view, null); // Get xml layout, make it inflatable here. null parent view.
            listView.addHeaderView(headerView, null, false);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
				public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
					if (position > 0) {
						int filePosition = position - 1;
						Uri uri;
					    if (sdkVersion < 24) {
                            uri = Uri.fromFile(fileList[filePosition]); // No too secure, though fine, broadcasts actual file pointer.
                        } else {
							uri = Uri.parse(fileList[filePosition].getPath());  // this one broadcasts a file path only
                        }
			            Intent viewFile = new Intent(Intent.ACTION_VIEW);
						viewFile.setDataAndType(uri, "text/plain");
					    startActivity(viewFile); // createChooser is not so pretty
					}
                }
            });
        } else {
			listView.setEnabled(false);
            filesTest = ("No " + storage + " directory.");
        }
		updateResults();
    }
	// show fileTasks() after granting storage permission. This whole method only applies the moment after you grant storage permission.
    @Override
	public void	onRequestPermissionsResult(int requestCode, String[] permission, int grantResults[]) {
		switch (requestCode) {
			case MY_PERMISSION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        			listView.setEnabled(true);
        			filesTask();
					updateResults();
        		} else {
        			listView.setEnabled(false);
                    filesTest = "Storage permission not granted, files list disabled";
        			updateResults();
        		}
				return;
		}
    }
    // Put results here from return value
    public void updateResults() {
		StringBuffer text = new StringBuffer();
	    final String[] results = {
	            "",
	            "1 + 2 = " + intCast(),
	            "String is from package: " + packageNameOfString(),
	            vibrateTest,
	            filesTest,
	            concatTxt,
	    };
	    results[0] = String.valueOf(results.length);
	    int z = 0;
        while (z < results.length) {
            text.append(results[z]);
            text.append("\n");
            z++;
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
    // Method to make any view scrollable. Insert View as parameter.
    public void makeScrollable(View currentView) {
        // Disable TouchEvent on ScrollView(which is current parent view) on ACTION_DOWN (tap down).
        currentView.setOnTouchListener(new View.OnTouchListener() {
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
    // Int tests.
    public String intCast() {
        int mint = 1 + 2;
        return String.valueOf(mint);
    }
    // String tests.
    public String packageNameOfString() {
        return String.class.getCanonicalName();
    }
}
