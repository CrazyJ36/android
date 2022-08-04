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
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainPhoneActivity extends Activity {
    Intent intent;
    public static ThreadLocal<TextView> info = new ThreadLocal<>();
    static String url;
    EditText editText;
    TextView urlView;
    Document document;
    Element element;
    int connectRetries = 0;
    static String connectException;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        urlView = findViewById(R.id.urlView);
        info.set(findViewById(R.id.info));
        if (UpdateService.isServiceRunning)
            Objects.requireNonNull(info.get()).setText(R.string.loadingMessage);
        intent = new Intent(this, UpdateService.class);
        findViewById(R.id.startServiceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UpdateService.isServiceRunning) {
                    if (!(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("url", getString(R.string.noUrlSetText))).equals(getString(R.string.noUrlSetText)))
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
                    Objects.requireNonNull(info.get()).setText(getText(R.string.serviceNotRunningText));
                    Toast.makeText(getApplicationContext(), getString(R.string.stoppedServiceText), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.serviceNotRunningText), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setRss(View view) {
        connectRetries = 0;
        if (UpdateService.isServiceRunning) stopService(intent);
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("url", getString(R.string.noUrlSetText)).apply();
        url = editText.getText().toString();
        if (url.equals("")) {
            Toast.makeText(this, getString(R.string.enterSomethingText), Toast.LENGTH_SHORT).show();
            urlView.setText(getString(R.string.noUrlSetText));
        } else if (!(Patterns.WEB_URL.matcher(url).matches())) {
            Toast.makeText(this, "Not A URL:\n" + url, Toast.LENGTH_SHORT).show();
            urlView.setText(getString(R.string.noUrlSetText));
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            while (connectRetries < 3) {
                                try {
                                    document = Jsoup.connect(url).get();
                                } catch (IOException e) {
                                    Log.d("WEARNEWS", e.getLocalizedMessage());

                                }
                                element = document.select("feed entry title").first();
                                if (element != null) {
                                    Log.d("WEARNEWS", "element not null");
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("url", url).apply();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            urlView.setText(url);
                                            Toast.makeText(getApplicationContext(), getString(R.string.startServiceText), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    timer.cancel();
                                    Thread.currentThread().interrupt();
                                } else {
                                    Log.d("WEARNEWS", "element null");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Objects.requireNonNull(info.get()).setText("");
                                            Toast.makeText(getApplicationContext(), getString(R.string.notAnRssFeedText), Toast.LENGTH_LONG).show();
                                            urlView.setText(getString(R.string.noUrlSetText));
                                        }
                                    });
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("url", getString(R.string.noUrlSetText)).apply();
                                    if (UpdateService.isServiceRunning) stopService(intent);
                                }
                                connectRetries++;
                            }
                        }
                    }, 0, 1000);
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
        if (UpdateService.isServiceRunning)
            Objects.requireNonNull(info.get()).setText(R.string.loadingMessage);
        urlView.setText(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("url", getString(R.string.noUrlSetText)));
        super.onResume();
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


