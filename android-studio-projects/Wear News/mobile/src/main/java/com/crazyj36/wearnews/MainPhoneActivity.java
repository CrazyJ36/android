package com.crazyj36.wearnews;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainPhoneActivity extends Activity {
    String messagepath = "/messagepath";
    String[] data = {null, null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    // resetting to invalid mark error, but then works.
                    //doc = Jsoup.connect("https://www.reddit.com/r/all/new/.rss").get();
                    doc = Jsoup.connect("https://www.reddit.com/r/AskReddit/new/.rss").get();
                    Element headline = doc.select("feed entry title").first();
                    Element categoryAttr = doc.select("feed entry category").first();
                    data[0] = headline.text();
                    data[1] = categoryAttr.attr("label");
                    sendData(data);
                    Log.d("WEARNEWS", "sent data");
                } catch (IOException e) {
                    data[0] = "URL ERROR:";
                    data[1] = e.getLocalizedMessage();
                    sendData(data);
                }

            }
        }, 0, 5000);



    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    private void sendData(String[] message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(messagepath);
        dataMap.getDataMap().putStringArray("message", message);
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();
        Task<DataItem> dataItemTask = Wearable.getDataClient(this).putDataItem(request);
        dataItemTask.addOnSuccessListener(new OnSuccessListener<DataItem>() {
            @Override
            public void onSuccess(DataItem dataItem) {
            }
        });
        dataItemTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

}


