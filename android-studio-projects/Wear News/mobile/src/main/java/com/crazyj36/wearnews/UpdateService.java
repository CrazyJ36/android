package com.crazyj36.wearnews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class UpdateService extends Service {
    Random random = new Random();
    static String[] currentPost = {"", ""};
    static String lastTitle;
    static ArrayList<String> recentPostsTitles = new ArrayList<>();
    static ArrayList<String> recentPostsSubs = new ArrayList<>();
    NotificationManager notificationManager;
    Notification notification;
    static Timer timer;
    static boolean isServiceRunning;
    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        MainPhoneActivity.setInfoText(getApplicationContext(), getString(R.string.loadingMessage));
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainPhoneActivity.class), PendingIntent.FLAG_IMMUTABLE);
        NotificationChannel notificationChannel = new NotificationChannel("notifychannel", "Notify Button", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        notification = new Notification.Builder(this, "notifychannel")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.serviceRunningTxt))
                .setContentIntent(contentIntent)
                .build();
        startForeground(1, notification);
        notificationManager.notify(1, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("WEARNEWS", "service starting");
        isServiceRunning = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Document doc;
                try {
                    doc = Jsoup.connect(MainPhoneActivity.urls.toArray(new String[MainPhoneActivity.urls.size()])[random.nextInt(MainPhoneActivity.urls.size())]).get();
                    Element headline = doc.select("feed entry title").first();
                    Element categoryAttr = doc.select("feed entry category").first();
                    if (headline != null && categoryAttr != null) {
                        if (!headline.text().equals(lastTitle)) {
                            recentPostsTitles.add(headline.text());
                            recentPostsSubs.add(categoryAttr.attr("label"));
                            if (recentPostsTitles.size() == 1) lastTitle = recentPostsTitles.get(0);
                            else if (recentPostsTitles.size() == 2) lastTitle = recentPostsTitles.get(1);
                            else if (recentPostsTitles.size() == 3) lastTitle = recentPostsTitles.get(2);
                            else if (recentPostsTitles.size() == 4) lastTitle = recentPostsTitles.get(3);
                            else if (recentPostsTitles.size() == 5) lastTitle = recentPostsTitles.get(4);
                            else {
                                recentPostsTitles.remove(0);
                                recentPostsSubs.remove(0);
                                lastTitle = recentPostsTitles.get(4);
                            }
                            currentPost[0] = lastTitle;
                            currentPost[1] = categoryAttr.attr("label");
                            sendData(currentPost, recentPostsTitles, recentPostsSubs);
                            Log.d("WEARNEWS", "sent new post.");
                            MainPhoneActivity.setInfoText(getApplicationContext(), getString(R.string.newPostsText));
                        } else {
                            Log.d("WEARNEWS", "no new posts");
                            MainPhoneActivity.setInfoText(getApplicationContext(), getString(R.string.noNewPostsText));
                        }
                    }
                } catch (IOException e) {
                    currentPost[0] = "Set new url in main app.";
                    currentPost[1] = "IOException";
                    sendData(currentPost, recentPostsTitles, recentPostsSubs);
                }
            }
        }, 0, 3000);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendData(String[] currentPost, ArrayList<String> recentPostsTitles, ArrayList<String> recentPostsSubs) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/WEARNEWS_MESSAGECHANNEL");
        dataMap.getDataMap().putStringArray("currentPost", currentPost);
        dataMap.getDataMap().putStringArrayList("recentPostsTitles", recentPostsTitles);
        dataMap.getDataMap().putStringArrayList("recentPostsSubs", recentPostsSubs);
        dataMap.getDataMap().putBoolean("checkIsServiceRunning", isServiceRunning);
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
        super.onDestroy();
        isServiceRunning = false;
        sendData(currentPost, recentPostsTitles, recentPostsSubs);
        timer.cancel();
        notificationManager.cancel(1);
        MainPhoneActivity.setInfoText(getApplicationContext(), getString(R.string.serviceNotRunningText));
        Log.d("WEARNEWS", "service destroyed");
    }
}


