package com.crazyj36.wearnews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
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

public class UpdateService extends Service {
    static String[] dataToSend = {"", ""};
    static String lastTitle = "";
    NotificationManager notificationManager;
    Notification notification;
    static Timer timer;
    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainPhoneActivity.class), PendingIntent.FLAG_IMMUTABLE);
        NotificationChannel notificationChannel = new NotificationChannel("notifychannel", "Notify Button", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        notification = new Notification.Builder(this, "notifychannel")
                .setSmallIcon(R.drawable.ic_stat_name)  // the status icon
                .setContentTitle(getString(R.string.app_name))  // the label of the entry
                .setContentText(getString(R.string.serviceRunningTxt))  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();
        startForeground(1, notification);
        notificationManager.notify(1, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("WEARNEWS", "service starting");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Document doc;
                try {
                    //doc = Jsoup.connect("https://www.reddit.com/r/all/new/.rss").get(); // resetting to invalid mark error, but then works.
                    doc = Jsoup.connect("https://www.reddit.com/user/crazy_j36/m/myrss/new/.rss").get();
                    Element headline = doc.select("feed entry title").first();
                    Element categoryAttr = doc.select("feed entry category").first();
                    if (headline != null) {dataToSend[0] = headline.text();}
                    if (categoryAttr != null) {dataToSend[1] = categoryAttr.attr("label");}
                    if (!(lastTitle.equals(dataToSend[0]))) {
                        sendData(dataToSend);
                        Log.d("WEARNEWS", "sent new post.");
                        lastTitle = dataToSend[0];
                        MainPhoneActivity.setInfoText(getApplicationContext(), getString(R.string.newPostsText));
                    } else {
                        Log.d("WEARNEWS", "no new posts");
                        MainPhoneActivity.setInfoText(getApplicationContext(), getString(R.string.noNewPostsText));
                    }
                } catch (IOException e) {
                    dataToSend[0] = "URL ERROR:";
                    dataToSend[1] = e.getLocalizedMessage();
                    sendData(dataToSend);
                }
            }
        }, 0, 5000);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
    @Override
    public void onDestroy() {
        MainPhoneActivity.setInfoText(getApplicationContext(), "");
        notificationManager.cancel(1);
        timer.cancel();
        Log.d("WEARNEWS", "service destroyed");
        super.onDestroy();
    }
}


