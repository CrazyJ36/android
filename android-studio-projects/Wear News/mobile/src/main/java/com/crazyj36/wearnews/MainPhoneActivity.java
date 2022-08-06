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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainPhoneActivity extends Activity {
    Intent intent;
    public static ThreadLocal<TextView> info = new ThreadLocal<>();
    String url;
    static ArrayList<String> urls = new ArrayList<>();
    EditText editText;
    ListView urlView;
    static Set<String> set;
    Document document;
    Element element;
    static boolean includeLabel;
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
        set = new HashSet<>(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet("urls", new HashSet<String>()));
        Log.d("WEARNEWS", "URLS Size: " + set.size());
        urls.clear();
        for (String str : set) {
            urls.add(str);
            Log.d("WEARNEWS", "set contains: " + str);
            for (int i = 0; i < urls.size(); i++) {
                Log.d("WEARNEWS", "Actual URLS: " + urls.get(i));
            }
        }
        info.set(findViewById(R.id.info));
        if (UpdateService.isServiceRunning)
            Objects.requireNonNull(info.get()).setText(R.string.loadingMessage);
        else Objects.requireNonNull(info.get()).setText(R.string.serviceNotRunningText);
        intent = new Intent(this, UpdateService.class);
        findViewById(R.id.startServiceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UpdateService.isServiceRunning) {
                    if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet("urls", set).size() > 0) {
                        startForegroundService(intent);
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.notStartingServiceUntilUrlIsSetText), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.serviceAlreadyRunningText), Toast.LENGTH_SHORT).show();
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
        } else if (!Patterns.WEB_URL.matcher(url).matches()) {
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
                                includeLabel = true;
                                boolean tryNext = false;

                                    element = document.select("feed entry title").first();
                                    if (element == null) tryNext = true;
                                if (tryNext) {
                                    includeLabel = false;
                                    try {
                                        element = document.select("rss channel item title").first();
                                    } catch (Exception e) {
                                        stopService(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Objects.requireNonNull(info.get()).setText(R.string.serviceNotRunningText);
                                                Toast.makeText(MainPhoneActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        timer.cancel();
                                        Thread.currentThread().interrupt();
                                    }
                                }

                                if (element != null) {
                                    if (repeat) {
                                        set.add(url);
                                        Log.d("WEARNEWS", "Added to set");
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putStringSet("urls", set).apply();
                                        urls.add(url);
                                        for (String str : set) {
                                            Log.d("WEARNEWS", "set contains: " + str);
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                urlView.invalidateViews();
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
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Objects.requireNonNull(info.get()).setText(R.string.serviceNotRunningText);
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
        if (UpdateService.isServiceRunning)
            Objects.requireNonNull(info.get()).setText(R.string.loadingMessage);
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