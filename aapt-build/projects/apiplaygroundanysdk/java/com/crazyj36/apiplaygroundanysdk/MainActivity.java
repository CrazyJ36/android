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
import android.os.Environment;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.os.Handler;
import android.os.Looper;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.Context;
import android.content.res.Configuration;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.net.Uri;
import android.util.Log;
import android.util.DisplayMetrics;
import java.lang.System;
import java.util.Date;
import java.util.Locale;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {

    // Strings for methods outside, or inward(in if's) of onCreate()
	String filesTest = "";
    String logChannel = "APIPLAYGROUNDLOG";
    int sdkVersion = Build.VERSION.SDK_INT;
    int CREATE_DOCUMENT_REQUEST = 0;
	public static final int MY_PERMISSION = 0;
	String[] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
    ListView listView;
    String filesDirLength = "";
	String vibrateTest = "";
	String concatTxt = "";
    String writeMyFileString = "";
    String isDocWritten = "Press New Doc, or it's already created.";
    String planets = "";
    String battery = "";
    String updaterTxt = "Update 0";
    String numbersEntered = "";
    String displayResolution = "";
    String storage;
    static PopupWindow popupWindow;

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

        vibratorInfo();

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
        String txt1 = "Hello";
        String txt2 = "World";
        concatTxt = String.format(Locale.US, "%s %s concatenated!", txt1, txt2); // or System.out.printf("%t", var); in console

        // Set TextView text from code, maybe not best practice.
        String tvHorizontalTxt = getResources().getString(R.string.tvHorizontalTxt);
        TextView horizontalTxt = findViewById(R.id.tvHorizontal);
        horizontalTxt.setText(tvHorizontalTxt);

        // Log in onCreate() and on button click.
        String startMsg = "App Started";
        Log.i(logChannel, startMsg);
        Log.println(4, logChannel, "Log.println() test"); // just like Log.i() priority 4 is the INFO constant.
        Button btnLog = findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(logChannel, "Log Button clicked");
            }
        });

        // Java package Date()
        final TextView tvDate = findViewById(R.id.
        tvDate);
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
							    tvPlayAudioStatus.setText(R.string.tvMediaNotPlayingTxt); // runs after the following delay.
                            }
						};
						new Handler(Looper.getMainLooper()).postDelayed(runThis, 3000); // postDelayed is like "post to cpu later". runThis will run after 3 seconds
					}
				});
			}
        });

		// Notification Button
        final String notifyChannel = "APIPLAYGROUNDANYSDK_NOTIFICATION_CHANNEL";
		final NotificationManager notificationManager = (NotificationManager) getSystemService("notification"); // getSystemService() string type ("vibrate") or class type (VIBRATOR_SERVICE)
		final Intent intent = new Intent(MainActivity.this, MainActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        if (notificationManager != null) {
			if (sdkVersion >= 26) {
			    NotificationChannel notificationChannel = new NotificationChannel(notifyChannel, "Notify Button", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
				notificationManager.createNotificationChannel(notificationChannel);
			}
		} else {
			Toast.makeText(MainActivity.this, "Notification Manager null, notificationChannel not set", Toast.LENGTH_SHORT).show();
		}
		findViewById(R.id.btnNotify).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View view) {
				if (notificationManager != null) {
					if (sdkVersion >= 11) {
						final Notification.Builder notifyBuild;
						if (sdkVersion >= 26) {
							notifyBuild = new Notification.Builder(MainActivity.this, notifyChannel);
						} else {
	                        notifyBuild = new Notification.Builder(MainActivity.this);
						}
						if (sdkVersion >= 21) {
							notifyBuild.setColor(9315498); // cool thing - also for below sdk 11, set colors in notification_layout.xml
						}
						notifyBuild.setSmallIcon(R.drawable.top_text_drawable);
						notifyBuild.setContentIntent(pendingIntent);
						notifyBuild.setContentTitle("A notification");
						notifyBuild.setContentText("This is the notification content");
						notifyBuild.setAutoCancel(true); // notification clears when clicked
						notifyBuild.setShowWhen(true); // Shows timestamp of when notification arose.
						notificationManager.notify(0, notifyBuild.build());
					} else {
						Notification notify = new Notification(R.drawable.top_text_drawable, "API Playground Any SDK", System.currentTimeMillis());
						notify.flags = Notification.FLAG_AUTO_CANCEL;
						notify.contentIntent = pendingIntent;
						RemoteViews notifView = new RemoteViews(getPackageName(), R.layout.notification_layout);
                        notifView.setTextViewText(R.id.notificationTvTitle, "API Playground Any SDK");
						notifView.setTextViewText(R.id.notificationTvContent, "A Notification\nThis is the notification content");
						notify.contentView = notifView;
						notify.defaults = Notification.DEFAULT_ALL;
						notificationManager.notify(notifyChannel, 0, notify);
					}
				} else {
					Toast.makeText(MainActivity.this, "Notification Manager null, notification not made", Toast.LENGTH_LONG);
				}
			}
        });

        // Create Document Button
        final Button btnCreateDoc = findViewById(R.id.btnCreateDoc);
        btnCreateDoc.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 createDocFile();
             }
        });

	    // CheckBoxes
	    final TextView tvChkStatus = findViewById(R.id.tvChkStatus);
	    final CheckBox chk1 = findViewById(R.id.chk1);
	    final CheckBox chk2 = findViewById(R.id.chk2);
	    final String[] tvChkStatusTxt = {""};
        tvChkStatus.setText("Select Some Checkboxes");
        chk1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (chk1.isChecked() && !chk2.isChecked()) {
		            tvChkStatusTxt[0] = "1 Checked";
		        } else if (!chk1.isChecked() && chk2.isChecked()) {
		            tvChkStatusTxt[0] = "2 Checked";
		        } else if (chk1.isChecked() && chk2.isChecked()) {
					tvChkStatusTxt[0] = "Both Checked";
		        } else if (!chk1.isChecked() && !chk2.isChecked()) {
					tvChkStatusTxt[0] = "None Checked";
		        }
				tvChkStatus.setText(tvChkStatusTxt[0]);
            }
		});
        chk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				if (chk1.isChecked() && !chk2.isChecked()) {
		            tvChkStatusTxt[0] = "1 Checked";
		        } else if (!chk1.isChecked() && chk2.isChecked()) {
		            tvChkStatusTxt[0] = "2 Checked";
		        } else if (chk1.isChecked() && chk2.isChecked()) {
					tvChkStatusTxt[0] = "Both Checked";
		        } else if (!chk1.isChecked() && !chk2.isChecked()) {
					tvChkStatusTxt[0] = "None Checked";
		        }
				tvChkStatus.setText(tvChkStatusTxt[0]);
            }
        });

        // Toggle
        final ToggleButton toggle1 = findViewById(R.id.toggle1);
        final TextView tvToggle = findViewById(R.id.tvToggle);
        toggle1.setChecked(false);
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleView, boolean isChecked) {
                if (toggle1.isChecked()) {
                    tvToggle.setText(getResources().getString(R.string.toggleOnTxt));
                } else if (!toggle1.isChecked()) {
                    tvToggle.setText(getResources().getString(R.string.toggleOffTxt));
                }
            }
        });

        // Write file to /data/data/com.crazyj36.apiplaygroundanysdk/
        try {
            writeMyFile();
        } catch (IOException ioException) {
            writeMyFileString = "writeMyFileIOException";
        }

        // String Array from xml
        String[] planetsArr = getResources().getStringArray(R.array.string_array);
        for (int x = 0; x < planetsArr.length; x++) {
            planets += planetsArr[x] + " ";
        }

        // Battery level and status using receiver.
        MainActivity.this.registerReceiver(MainActivity.this.batteryBroadcast,
            new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // Button update text in results view
        Button btnUpdateTxt = findViewById(R.id.btnUpdateTxt);
        btnUpdateTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updaterTxt == "Update 0") {
                    updaterTxt = "Update 1";
                } else if (updaterTxt == "Update 1") {
                    updaterTxt = "Update 0";
                }
                updateResults();
            }
        });

        // Button random numbers
        findViewById(R.id.btnRandomNumbers).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] numbers = getResources().getStringArray(R.array.numbers_array);
                Random random = new Random();
                int randomNumbersItem = random.nextInt(numbers.length);
                generalToast(numbers[randomNumbersItem]);
            }
        });

        // Grid view clickable example
        final Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        GridView gridView = findViewById(R.id.gridTextView);
        final TextView tvNumberGrid = findViewById(R.id.tvNumberGrid);
        ArrayAdapter<Integer> gridAdapter = new ArrayAdapter<Integer>(
            MainActivity.this, R.layout.grid_item, numbers);
        gridView.setAdapter(gridAdapter);
	    gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                numbersEntered = numbersEntered.concat(String.valueOf(numbers[position]));
                tvNumberGrid.setText(numbersEntered);
	        }
        });

        // Get display resolution
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        displayResolution = "Resolution Width: " + String.valueOf(displayMetrics.widthPixels)
            + " Height: " + String.valueOf(displayMetrics.heightPixels);

        // PopupWindow Button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final LinearLayout linearLayout = findViewById(R.id.rootView);
	    popupWindow = new PopupWindow(
	        inflater.inflate(R.layout.popup_window_layout, null),
	        LayoutParams.WRAP_CONTENT,
	        LayoutParams.WRAP_CONTENT
	    );
        Button btnPopupWindow = findViewById(R.id.btnPopupWindow);
        btnPopupWindow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.getContentView().findViewById(R.id.popupWindowFab).setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        generalToast("Fab Clicked");
                    }
                });
                popupWindow.showAtLocation(linearLayout, Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
            }
        });

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

    // filesTask() Get files from external /sdcard/Android/com.company.app/, show names in listview
    public void filesTask() {
        // Files in external files directory /sdcard/Android/com.crazyj36.apiplaygroundanysdk/files
        // The method of getting external files here should be done from api 29 onward.
        try {
            if (sdkVersion >= 8) {
                File appFilesDir = getExternalFilesDir(null);
                File[] appFilesList = appFilesDir.listFiles();
                filesDirLength = String.valueOf(appFilesList.length) + " files/dirs in external app storage dir.";
            }
            else {
                filesDirLength = "Get app files dir length on api < 8";
                return;
            }
        }
        catch (NullPointerException nullPointerException) {
            filesDirLength = "No Storage Mounted";
            filesTest = "No Storage Mounted";
            return;
        }
        if (sdkVersion < 29) {
            storage = Environment.getExternalStorageDirectory() + "/notes".toString(); // /sdcard/notes/
        }
        else {
	        storage = getExternalFilesDir("notes").toString(); // /sdcard/Android/com.company.app/files/notes
        }
        File dir = new File(storage);
		listView = findViewById(R.id.fileList);
        if (sdkVersion < 21) {
            makeScrollable(listView);
        }
        if (!(dir.exists() && dir.isDirectory())) {
            filesTest = "No " + storage + " directory.";
            listView.setEnabled(false);
            return;
        }
	    final File[] fileList = dir.listFiles(); // try to sort by name. initially sorted by date.
	    if (fileList.length == 0) {
			listView.setEnabled(false);
	        filesTest = ("No files in: " + dir.toString());
		    return;
		}
        filesTest = "Files at " + dir.toString() + " below";
        String[] names = new String[fileList.length];
        for (int i = 0; i < fileList.length; i++) {
            names[i] = fileList[i].getName();
        }
        Arrays.sort(names); // won't sort numbered filenames like note2, note1.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, names);
        View listHeaderView = View.inflate(MainActivity.this, R.layout.list_header_view, null); // Get xml layout, make it inflatable here. null parent view.
        listView.addHeaderView(listHeaderView, null, false);
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
                    filesTest = "Storage permission denied, files disabled";
        			updateResults();
        		}
				return;
		}
    }

    // Put results here from return value
    public void updateResults() {
		StringBuffer text = new StringBuffer();
	    final String[] results = {
                "spacer, results[0] defined below",
                updaterTxt,
                "1 + 2 = " + intCast(),
                "Basic String",
	            "String package: " + packageNameOfString(),
	            vibrateTest,
	            filesTest,
	            concatTxt,
                "The current days' hour is: " + new javaClass().getHourOfDay(),
				javaClass.myMethod(),
                "Downloads dir name: " + Environment.DIRECTORY_DOWNLOADS,
                writeMyFileString,
                filesDirLength,
                "strings.xml array: " + planets,
                battery,
                displayResolution,
                isDocWritten,
	    };
	    results[0] = "Results feild entries: " + String.valueOf(results.length);
	    int z = 0;
        while (z < results.length) {
            text.append(results[z]);
            text.append("\n\n");
            z++;
        }
	    String buffOut = String.valueOf(text);
		TextView resultsTextView = findViewById(R.id.resultsTextView);
	    resultsTextView.setTextColor(0xFF909090);
	    resultsTextView.setBackgroundColor(0xFF505050);
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

    // Menu init
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    // Called everytime menu is opened.
    public boolean onPrepareOptionsMenu(Menu menu) {
	    if (!popupWindow.isShowing()) {
	        menu.getItem(1).setEnabled(false);
	    } else {	        menu.getItem(1).setEnabled(true);
	    }
        return true;
    }

    // Menu onclicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
                Dialog dialogAbout = new Dialog(MainActivity.this);
                dialogAbout.setContentView(R.layout.dialog_about);
                dialogAbout.setTitle("About App");
                dialogAbout.show();
                // An onClick in A seperate layout must be defined this way.
                dialogAbout.findViewById(R.id.dialogAboutBtn).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                		String myGitUrl = "http://github.com/CrazyJ36?tab=repositories";
                		Intent crazyj36Git = new Intent(Intent.ACTION_VIEW);
                		crazyj36Git.setData(Uri.parse(myGitUrl));
                		startActivity(crazyj36Git);
            		}
        		});
                return true;
            case R.id.menuHideFab:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return true;
            case R.id.menuExit:
                MainActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Catch config changes(screen rotated, screen size change, etc.)
    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
		Toast.makeText(this, "Config Changed", Toast.LENGTH_SHORT).show();
    }

    // onDestroy
    @Override
    protected void onDestroy() {
        generalToast("apiplaygroundanysdk MainActivity destroyed");
        super.onDestroy();
    }

    public void generalToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    // write to /data/data/com.crazyj36.apiplaygroundanysdk
    public void writeMyFile() throws IOException {
        if (getFilesDir().exists() && getFilesDir().isDirectory()) {
            File newFile = new File(getFilesDir() + "/data_file.txt");
            FileWriter fileWriter = new FileWriter(newFile);
            fileWriter.write(getResources().getString(R.string.my_fileTxt));
            writeMyFileString = "data_file.txt written to: " + getFilesDir();
            fileWriter.close();
        }
    }

    // Create document file
    public void createDocFile() {
        Intent createDocIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        createDocIntent.addCategory(Intent.CATEGORY_OPENABLE);
        createDocIntent.setType("text/plain");
        createDocIntent.putExtra(Intent.EXTRA_TITLE, "doc.txt");
        startActivityForResult(createDocIntent, CREATE_DOCUMENT_REQUEST);
    }
    // Return from above
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_DOCUMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                 isDocWritten = "Done! " + data.getDataString();
                 updateResults();
            } else {
                 isDocWritten = "Didn't create document";
                 updateResults();
            }
        }
    }
    // Read battery state broadcast from system
    private BroadcastReceiver batteryBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            battery = "Battery level: " + String.valueOf(batteryLevel) + "%";
            updateResults();
        }
    };

    private void vibratorInfo() {
       // Vibrator info
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        try {
            if (sdkVersion < 11 && vib != null) {
				vib.vibrate(100);
                vibrateTest = "Vibrator Not null, Success";
            } else if (sdkVersion >= 26 && vib != null && vib.hasVibrator()) {
 		        vib.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
				vibrateTest = "Hasvibrator, Default amplitude, class VibrationEffect.";
            } else if (sdkVersion >= 26 && vib != null && vib.hasVibrator() && vib.hasAmplitudeControl()) {
				vib.vibrate(VibrationEffect.createOneShot(100, 10));
                vibrateTest = "Has vibrator. %15 amplitude, class Vibrator.";
            } else if (Build.VERSION.SDK_INT >= 11 && sdkVersion < 26 && vib != null && vib.hasVibrator()) {
                vib.vibrate(100);
				vibrateTest = "Has vibrator. Default amplitude, class Vibrator.";
            } else if (sdkVersion >= 11 && !(vib.hasVibrator())) { // Checks NullPointerException next in catch.
				vibrateTest = "No vibrate hardware.";
            }
        } catch (NullPointerException nullPointerException) {
                vibrateTest = "Vibrate 'null' error:\n" + nullPointerException.getLocalizedMessage();
        }
    }

}
