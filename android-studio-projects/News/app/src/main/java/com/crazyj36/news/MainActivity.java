package com.crazyj36.news;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView result;
    String text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        findViewById(R.id.getBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();
            }
        });
    }
    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Document doc = Jsoup.connect("https://news.google.com/rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRGRqTVhZU0FtVnVHZ0pKVGlnQVAB").get();
                    //Element headline = doc.select("rss channel item title").first();
                    Document doc = Jsoup.connect("https://www.reddit.com/r/AskReddit/new/.rss").get();
                    Element headline = doc.select("feed entry title").first();

                    text = headline.text();
                } catch (IOException e) {
                    result.setText("error");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(text);
                    }
                });
            }
        }).start();
    }

}