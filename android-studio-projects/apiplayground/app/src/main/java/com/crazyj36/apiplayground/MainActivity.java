package com.crazyj36.apiplayground;
/* Ideas
   1 function that starts a dialog or toast.
   print all buttons/functions results in that

   for notifications try increase counter on every click,
   then add another notification with new notification id

   log button

   Try to decrease number of layout files by implementing the dialog views in java with Dialog.Builder
 */

import android.Manifest;
import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String ApiPlaygroudLogChannel = "APIPLAYGROUNDLOG";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Variables
        final FloatingActionButton fab = findViewById(R.id.fab);

        //Disables AutoFill. Easier in xml layout, though this statement can make autofill available to sdks that use it eg. 26 oreo
        if (Build.VERSION.SDK_INT >= 26)

            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_YES_EXCLUDE_DESCENDANTS);
        // Phone/ storage permission
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 1);
        // Toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);

        setActionBar(toolbar);

        final ImageButton toolbarMenu = findViewById(R.id.toolbarMenu);
        toolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu menu = new PopupMenu(MainActivity.this, toolbarMenu, android.view.Gravity.END);
                menu.getMenuInflater().inflate(R.menu.options_activity1, menu.getMenu());
                if (fab.isShown()) {
                    menu.getMenu().findItem(R.id.showFab).setEnabled(false);
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int menuItemChosen = menuItem.getItemId();
                        if (menuItemChosen == R.id.toolbarExit) {
                            MainActivity.this.finishAffinity();
                        } else if (menuItemChosen == R.id.toolbarAboutActivity1) {
                            Toast.makeText(MainActivity.this, "This screen demos several Widgets and API calls", Toast.LENGTH_SHORT).show();
                        } else if (menuItemChosen == R.id.showFab) {
                            if (!fab.isShown()) {
                                fab.show();
                            }
                        } else if (menuItemChosen == R.id.toolbarAccentToggle) {
                            ViewGroup viewGroup = findViewById(R.id.act1RootView);
                            getTheme().applyStyle(R.style.AppThemeDark, true);
                            viewGroup.setBackgroundColor(getResources().getColor(R.color.backgroundDark, getTheme()));
                            getLayoutInflater().inflate(R.layout.activity_main, viewGroup);
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        // ActionBar Subtitle
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.setSubtitle("CrazyJ36");

        // Fab Button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutAppDialog();
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fab.cancelPendingInputEvents();
                fab.hide();
                return true;
            }
        });

        // Activity2 Button
        findViewById(R.id.activity2Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act2Intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(act2Intent);
            }
        });
        // Dialog
        findViewById(R.id.dialogButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new Builder(MainActivity.this).create();
                dialog.setTitle("Info");
                dialog.setMessage("Dialog Button Clicked");
                dialog.setCancelable(false);
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        //Button click/long
        findViewById(R.id.toastButton).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "Button Long Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        findViewById(R.id.toastButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        //Audio Button
        findViewById(R.id.audioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To have several music files as variables:
                //int music = getResources().getIdentifier("flute","raw", "com.crazyj36.myapplication");
                final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.flute);
                final TextView songInfoView = findViewById(R.id.songInfo);
                mediaPlayer.start();
                if (mediaPlayer.isPlaying()) {
                    songInfoView.setText(R.string.mediaPlayingTxt);
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                songInfoView.setText(R.string.songInfoTxt);
                            }
                        }, 3000);
                        songInfoView.setText(R.string.mediaPlayerReleaseTxt);
                    }
                });
            }
        });
        //Button Call Phone
        findViewById(R.id.callButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usrNum = findViewById(R.id.numField);
                String num = usrNum.getText().toString();
                Uri data = Uri.parse("tel:" + num);
                Intent call = new Intent(Intent.ACTION_CALL, data);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Check Phone Permission", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.PHONE.matcher(num).matches()) {
                    Toast.makeText(MainActivity.this, "Type A regular phone number", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Calling " + num, Toast.LENGTH_SHORT).show();
                    startActivity(call);
                }
            }
        });
        //Button URL Launch
        findViewById(R.id.urlButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = findViewById(R.id.urlField);
                String url = input.getText().toString();
                if (Patterns.DOMAIN_NAME.matcher(url).matches() || Patterns.WEB_URL.matcher(url).matches()) {
                    try {
                        Intent webPage = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(webPage);
                        Toast.makeText(MainActivity.this, "Launching " + url + " in browser", Toast.LENGTH_SHORT).show();
                    } catch (ActivityNotFoundException activityNotFound) {
                        Toast.makeText(MainActivity.this, "You must type \"http://\",\nor no internet browser installed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Must be typed as \"http://www.website.com\"", Toast.LENGTH_LONG).show();
                }
            }
        });
        //Button SMS
        findViewById(R.id.smsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText msgField = findViewById(R.id.smsField);
                String usrMsg = msgField.getText().toString();
                Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                sendMsg.setData(Uri.parse("sms:"));
                sendMsg.putExtra("sms_body", usrMsg);
                if (usrMsg.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter text to send", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(sendMsg);
                }
            }
        });
        //Switch
        final Switch switch1 = findViewById(R.id.switch1);
        switch1.setChecked(false);
        final String[] chkSwitchStr = {"OFF"};
        switch1.setText(R.string.switchOffTxt);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch1.isChecked()) {
                    switch1.setText(R.string.switchOnTxt);
                    chkSwitchStr[0] = "ON";
                } else if (!switch1.isChecked()) {
                    switch1.setText(R.string.switchOffTxt);
                    chkSwitchStr[0] = "OFF";
                }
            }
        });
        // Check Switch Button
        findViewById(R.id.chkSwitchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, chkSwitchStr[0], Toast.LENGTH_SHORT).show();
            }
        });
        // Notification
        final NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("APIPLAYGROUND_NOTIFICATION_CHANNEL", "Notify Button", NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            } else {
                Toast.makeText(this, "Notification Manager Service returned null", Toast.LENGTH_SHORT).show();
            }
        }
        final Intent notificationClickIntent = new Intent(MainActivity.this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, notificationClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        findViewById(R.id.notifyBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationManager != null) {
                    NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(MainActivity.this, "APIPLAYGROUND_NOTIFICATION_CHANNEL");
                    notifyBuild.setSmallIcon(R.drawable.notify);
                    notifyBuild.setContentTitle("API Playground Notification");
                    notifyBuild.setContentText("This is the content of notification");
                    notifyBuild.setContentIntent(pendingIntent);
                    notifyBuild.setAutoCancel(true);
                    notificationManager.notify(0, notifyBuild.build()); /* Id here is random. Stands for the individual id
                    of this notification. If I click notifyBtn A second time, A new notification would not be created,
                    as this notification id is already used in notifications .Only used here.
                    If I want to start another notification, or repeat this one, A new NotificationCompat.Builder must be instantiated with A
                    new notification id must be used. */
                }
            }
        });
        // Random Button
        Button btn = findViewById(R.id.randomBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] useWords = myWords();
                Random random = new Random();
                int index = random.nextInt(useWords.length);
                Toast.makeText(MainActivity.this, useWords[index], Toast.LENGTH_SHORT).show();
            }
        });
        // SnackBar Button
        final CoordinatorLayout topLayout = findViewById(R.id.act1CoordinatorLayoutView);
        findViewById(R.id.snackbarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(topLayout, "This Is A SnackBar", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.WHITE).setAction("Toast", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "SnackBar Button Pushed", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        // Bottom Sheet Button
        findViewById(R.id.bottomSheetBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet);
                bottomSheetDialog.show();

            }
        });
        // Vibrate Btn
        findViewById(R.id.vibBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(MainActivity.this.getSystemService(Vibrator.class)).hasVibrator()) {
                    try {
                        Objects.requireNonNull(MainActivity.this.getSystemService(Vibrator.class)).vibrate(500);
                    } catch (NullPointerException nullPointerException) {
                        Toast.makeText(MainActivity.this, "Null Pointer Exception Raised", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Vibrator Hardware in Device", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Battery Check
        findViewById(R.id.batBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog batDialog = new AlertDialog.Builder(MainActivity.this).setCancelable(false).create();
                BatteryManager batManager = Objects.requireNonNull(MainActivity.this.getSystemService(BatteryManager.class));
                int batLevel = batManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                String batCharging;
                if (batManager.isCharging()) {
                    batCharging = "Charging";
                } else {
                    batCharging = "Discharging";
                }
                batDialog.setMessage("Battery Percentage: " + batLevel + "\n" + batCharging);
                batDialog.setTitle("Battery");
                batDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface batDialog, int batRetInt) {
                        String batRetStr = Integer.toString(batRetInt);
                        Log.i(getResources().getString(R.string.tag), batRetStr);
                        batDialog.dismiss();
                    }
                });
                batDialog.show();
            }
        });
        // Grid Buttons Methods are outside of onCreate() Implemented finding buttons MainActivity
        Button grid1 = findViewById(R.id.grid1);
        grid1.setOnClickListener(this);
        Button grid2 = findViewById(R.id.grid2);
        grid2.setOnClickListener(this);
        Button grid3 = findViewById(R.id.grid3);
        grid3.setOnClickListener(this);
        Button grid4 = findViewById(R.id.grid4);
        grid4.setOnClickListener(this);

        // Log Button
        Button logBtn = findViewById(R.id.logBtn);
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ApiPlaygroudLogChannel, "Log Button Clicked");
            }
        });

        // Files List
        ListView listView = findViewById(R.id.listViewFiles);
        String notesPath = Environment.getExternalStorageDirectory().toString() + "/notes";
        File dir = new File(notesPath);

        final File[] fileList = dir.listFiles();
        assert fileList != null;
        String[] names = new String[fileList.length];
        for (int i = 0; i < fileList.length; i++) {
            names[i] = fileList[i].getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_view, names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse((fileList[position].getPath()));
                Intent viewFile = new Intent(Intent.ACTION_VIEW);
                viewFile.setDataAndType(uri, "text/plain");
                startActivity(viewFile);

            }
        });

        // End of onCreate()
    }

    public void gridTstMsg(String num) {
        String pt1 = "Grid ";
        String pt3 = " Clicked";
        Toast.makeText(this, pt1 + num + pt3, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.grid1):
                gridTstMsg("1");
                break;
            case R.id.grid2:
                gridTstMsg("2");
                break;
            case R.id.grid3:
                gridTstMsg("3");
                break;
            case R.id.grid4:
                gridTstMsg("4");
                break;
        }
    }

    // Catch When the Activity is destroyed in case this shouldn't happen
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getResources().getString(R.string.tag), "Activity1 Stopped");
    }

    // Catch when config changes(rotate), goes with <activity android:configChanges="orientation|screenSize" /> in AndroidManifest.xml
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(getResources().getString(R.string.tag), "Activity1 Config Changed");
    }

    // Dev info Dialog
    public void aboutAppDialog() {
        final AlertDialog.Builder aboutDialog = new AlertDialog.Builder(MainActivity.this);
        aboutDialog.setTitle("About Developer");
        aboutDialog.setIcon(R.drawable.profile);
        aboutDialog.setView(R.layout.profile_layout);
        // aboutDialog.setMessage("CrazyJ36 - 2019");
        aboutDialog.create();
        aboutDialog.show();
    }

    // A re-usable array
    public final String[] myWords() {
        return new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen"};
    }

    public void goToGit(View view) {
        Intent gitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/CrazyJ36?tab=repositories"));
        startActivity(gitUrl);
    }

    public void otherMethodsStringToast(View view) {
        // CharSequence of this Toast statement initializes A new otherMethods class and uses the string returned from function.
        Toast.makeText(MainActivity.this, new otherMethods().otherMethodsStringFunc(), Toast.LENGTH_SHORT).show();
    }

    public void hourToast(View view) {
        Toast.makeText(this, "The current days' hour is: " + new otherMethods().getHourOfDay(), Toast.LENGTH_SHORT).show();
    }
}
