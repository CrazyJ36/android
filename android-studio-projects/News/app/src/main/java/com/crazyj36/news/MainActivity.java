package com.crazyj36.news;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.Bundle;
import android.app.Activity;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    TextView title;
    TextView sub;
    String titleTxt = null;
    String subTxt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.title);
        sub = findViewById(R.id.sub);
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
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
                Looper.prepare();
                try {
                    Document doc = Jsoup.connect("http://www.reddit.com/r/AskReddit+Music/new/.rss?sort=new").get();
                    //Document doc = Jsoup.connect("http://www.reddit.com/r/all/new/.rss").get();
                    Element headline = doc.select("feed entry title").first();
                    Element categoryAttr = doc.select("feed entry category").first();
                    titleTxt = headline.text();
                    subTxt = categoryAttr.attr("label");
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        title.setText(titleTxt);
                        sub.setText(subTxt);
                    }
                });
            }
        }).start();
    }

}