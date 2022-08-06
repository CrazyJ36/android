package com.crazyj36.wearnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainPhoneActivity extends Activity {
    Intent intent;
    public static ThreadLocal<TextView> info = new ThreadLocal<>();
    String url;
    static ArrayList<String> urls = new ArrayList<>();
    String[] preferenceArray;
    EditText editText;
    ListView urlView;
    Gson gson = new Gson();
    Document document;
    Element element;
    Timer timer;
    int connectRetries;
    boolean repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        urlView = findViewById(R.id.urlView);
        urlView.setAdapter(new UrlListAdapter(getApplicationContext(), urls));
        preferenceArray = gson.fromJson(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("urls", getString(R.string.noUrlSetText)), String[].class);
        for (int preferenceCount = 0; preferenceCount <= preferenceArray.length; preferenceCount++) {
            Log.d("WEARNEWS", "Entered URL: " + preferenceArray[preferenceCount]);
        }
        info.set(findViewById(R.id.info));
        if (UpdateService.isServiceRunning) Objects.requireNonNull(info.get()).setText(R.string.loadingMessage);
        else Objects.requireNonNull(info.get()).setText(R.string.serviceNotRunningText);
        intent = new Intent(this, UpdateService.class);
        findViewById(R.id.startServiceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UpdateService.isServiceRunning) {
                    if (preferenceArray.length > 0)
                        startForegroundService(intent);
                    else Toast.makeText(getApplicationContext(), getString(R.string.notStartingServiceUntilUrlIsSetText), Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), getString(R.string.serviceAlreadyRunningText), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.stopServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UpdateService.isServiceRunning) {
                    stopService(intent);
                    Toast.makeText(getApplicationContext(), getString(R.string.stoppedServiceText), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.serviceNotRunningText), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void setRss(View view) {
        repeat = true;
        url = editText.getText().toString();
        if (url.equals("")) {
            stopService(intent);
            Objects.requireNonNull(info.get()).setText(R.string.serviceNotRunningText);
            Toast.makeText(this, getString(R.string.enterSomethingText), Toast.LENGTH_SHORT).show();
        } else if (!(Patterns.WEB_URL.matcher(url).matches())) {
            stopService(intent);
            Objects.requireNonNull(info.get()).setText(R.string.serviceNotRunningText);
            Toast.makeText(this, "Not A URL:\n" + url, Toast.LENGTH_SHORT).show();
        } else {
            connectRetries = 0;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("WEARNEWS", "trying to connect...");
                            if (connectRetries == 2) {
                                timer.cancel();
                                Thread.currentThread().interrupt();
                            }
                            try {
                                document = Jsoup.connect(url).get();
                                try {
                                    element = document.select("feed entry title").first();
                                } catch (Exception exception) {
                                    Log.e("WEARNEWS", exception.getLocalizedMessage());
                                    element = document.select("rss channel item").first();
                                }
                                if (element != null) {
                                    if (repeat) {
                                        urls.add(url);
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("urls", gson.toJson(urls)).apply();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (UpdateService.isServiceRunning) {
                                                    Toast.makeText(MainPhoneActivity.this, getString(R.string.urlAddedText), Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(MainPhoneActivity.this, getString(R.string.startServiceText), Toast.LENGTH_SHORT).show();
                                                repeat = false;
                                            }
                                        });
                                    }
                                } else {
                                    if (repeat) {
                                        stopService(intent);
                                        Objects.requireNonNull(info.get()).setText(R.string.serviceNotRunningText);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MainPhoneActivity.this, getString(R.string.notAnRssFeedText), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        repeat = false;
                                    }

                                }
                            } catch (IOException e) {
                                Log.d("WEARNEWS", e.getLocalizedMessage());
                            }
                            connectRetries++;
                        }
                    }, 0, 500);
                }
            }).start();
        }
    }
    public static void setInfoText(Context context, String text) {
        context.getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(info.get()).setText(text);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (UpdateService.isServiceRunning) Objects.requireNonNull(info.get()).setText(R.string.loadingMessage);
        else Objects.requireNonNull(info.get()).setText(getString(R.string.serviceNotRunningText));
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}