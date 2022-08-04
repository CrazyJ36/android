package com.crazyj36.wearnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class MainPhoneActivity extends Activity {
    Intent intent;
    public static ThreadLocal<TextView> info = new ThreadLocal<>();
    static String url;
    EditText editText;
    TextView urlView;
    boolean jsoupConnected;
    String testElement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        urlView = findViewById(R.id.urlView);
        urlView.setText(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("url", getString(R.string.noUrlSetText)));
        info.set(findViewById(R.id.info));
        if (UpdateService.isServiceRunning) Objects.requireNonNull(info.get()).setText(R.string.loadingMessage);
        intent = new Intent(this, UpdateService.class);
        findViewById(R.id.startServiceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UpdateService.isServiceRunning) {
                    if (!(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("url", getString(R.string.noUrlSetText)).equals(""))) startForegroundService(intent);
                }
                else Toast.makeText(getApplicationContext(), "Service already running", Toast.LENGTH_SHORT).show();

            }
        });
        findViewById(R.id.stopServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UpdateService.isServiceRunning) {
                    stopService(intent);
                    Objects.requireNonNull(info.get()).setText(getText(R.string.serviceNotRunningText));
                    Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), getString(R.string.serviceNotRunningText), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setRss(View view) {
        if (UpdateService.isServiceRunning) stopService(intent);
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("url", getString(R.string.noUrlSetText)).apply();
        url = editText.getText().toString();
        if (url.equals("")) {
            Toast.makeText(this, "Enter A reddit feed URL.", Toast.LENGTH_SHORT).show();
            urlView.setText(getString(R.string.noUrlSetText));
        } else if (!(Patterns.WEB_URL.matcher(url).matches())) {
            Toast.makeText(this, "Not A URL:\n" + url, Toast.LENGTH_SHORT).show();
            urlView.setText(getString(R.string.noUrlSetText));
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        jsoupConnected = true;
                        Document doc = Jsoup.connect(url).get();
                        Element element = doc.select("feed entry title").first();
                        assert element != null;
                        testElement = element.text();
                    } catch (IOException e) {
                        jsoupConnected = false;
                    }
                    Thread.currentThread().interrupt();
                }
            }).start();
            if (jsoupConnected) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("url", url).apply();
                urlView.setText(url);
                Toast.makeText(this, getString(R.string.startServiceText), Toast.LENGTH_SHORT).show();
            } else {
                urlView.setText(getString(R.string.noUrlSetText));
                if (UpdateService.isServiceRunning) stopService(intent);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("url", getString(R.string.noUrlSetText)).apply();
                Toast.makeText(this, getString(R.string.notAnRssFeedText), Toast.LENGTH_SHORT).show();
            }
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
    public void onResume () {
        if (UpdateService.isServiceRunning) Objects.requireNonNull(info.get()).setText(R.string.loadingMessage);
        urlView.setText(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("url", getString(R.string.noUrlSetText)));
        super.onResume();
    }
    @Override
    public void onPause () {super.onPause();}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


