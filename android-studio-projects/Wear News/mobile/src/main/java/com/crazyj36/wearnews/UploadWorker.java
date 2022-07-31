package com.crazyj36.wearnews;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
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

public class UploadWorker extends Worker {
    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
            super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Document doc;
                String[] data = {null, null};
                try {
                    //doc = Jsoup.connect("https://www.reddit.com/r/all/new/.rss").get(); // resetting to invalid mark error, but then works.
                    doc = Jsoup.connect("https://www.reddit.com/user/crazy_j36/m/myrss/new/.rss").get();
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
        return Result.success();
    }
    private void sendData(String[] message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/message_path");
        dataMap.getDataMap().putStringArray("message", message);
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();
        Task<DataItem> dataItemTask = Wearable.getDataClient(getApplicationContext()).putDataItem(request);
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
