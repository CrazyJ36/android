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
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateService extends Service {
    static String[] currentPost = {"", ""};
    static String lastTitle;
    static String secondTitle;
    static String thirdTitle;
    static String fourthTitle;
    static String fifthTitle;
    static String lastSub;
    static String secondSub;
    static String thirdSub;
    static String fourthSub;
    static String fifthSub;
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
        isServiceRunning = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Document doc;
                try {
                    //doc = Jsoup.connect("https://www.reddit.com/r/all/new/.rss").get(); // resetting to invalid mark error, but then works.
                    doc = Jsoup.connect("https://www.reddit.com/user/crazy_j36/m/myrss/new/.rss").get();
                    Element headline = doc.select("feed entry title").first();
                    Element categoryAttr = doc.select("feed entry category").first();
                    if (headline != null && categoryAttr != null) {
                        if (!headline.text().equals(lastTitle)) {
                            recentPostsTitles.add(headline.text());
                            recentPostsSubs.add(categoryAttr.attr("label"));
                            if (recentPostsTitles.size() == 1) {
                                lastTitle = recentPostsTitles.get(0);
                                lastSub = recentPostsSubs.get(0);
                                MainPhoneActivity.setInfo2Text(getApplicationContext(), "latest: " + recentPostsTitles.get(0));
                            } else if (recentPostsTitles.size() == 2) {
                                lastTitle = recentPostsTitles.get(1);
                                lastSub = recentPostsSubs.get(1);
                                secondTitle = recentPostsTitles.get(0);
                                secondSub = recentPostsSubs.get(0);
                                MainPhoneActivity.setInfo2Text(getApplicationContext(), "latest: " + recentPostsTitles.get(1));
                                MainPhoneActivity.setInfo3Text(getApplicationContext(), "backup 1: " + recentPostsTitles.get(0));
                            } else if (recentPostsTitles.size() == 3) {
                                lastTitle = recentPostsTitles.get(2);
                                lastSub = recentPostsSubs.get(2);
                                secondTitle = recentPostsTitles.get(1);
                                secondSub = recentPostsSubs.get(1);
                                thirdTitle = recentPostsTitles.get(0);
                                thirdSub = recentPostsSubs.get(0);
                                MainPhoneActivity.setInfo2Text(getApplicationContext(), "latest: " + recentPostsTitles.get(2));
                                MainPhoneActivity.setInfo3Text(getApplicationContext(), "backup 1: " + recentPostsTitles.get(1));
                                MainPhoneActivity.setInfo4Text(getApplicationContext(), "backup 2: " + recentPostsTitles.get(0));
                            } else if (recentPostsTitles.size() == 4) {
                                lastTitle = recentPostsTitles.get(3);
                                lastSub = recentPostsSubs.get(3);
                                secondTitle = recentPostsTitles.get(2);
                                secondSub = recentPostsSubs.get(2);
                                thirdTitle = recentPostsTitles.get(1);
                                thirdSub = recentPostsSubs.get(1);
                                fourthTitle = recentPostsTitles.get(0);
                                fourthSub = recentPostsSubs.get(0);
                                MainPhoneActivity.setInfo2Text(getApplicationContext(), "latest: " + recentPostsTitles.get(3));
                                MainPhoneActivity.setInfo3Text(getApplicationContext(), "backup 2: " + recentPostsTitles.get(2));
                                MainPhoneActivity.setInfo4Text(getApplicationContext(), "backup 3: " + recentPostsTitles.get(1));
                                MainPhoneActivity.setInfo5Text(getApplicationContext(), "backup 4: " + recentPostsTitles.get(0));
                            } else if (recentPostsTitles.size() == 5) {
                                lastTitle = recentPostsTitles.get(4);
                                lastSub = recentPostsSubs.get(4);
                                secondTitle = recentPostsTitles.get(3);
                                secondSub = recentPostsSubs.get(3);
                                thirdTitle = recentPostsTitles.get(2);
                                thirdSub = recentPostsSubs.get(2);
                                fourthTitle = recentPostsTitles.get(1);
                                fourthSub = recentPostsSubs.get(1);
                                fifthTitle = recentPostsTitles.get(0);
                                fifthSub = recentPostsSubs.get(0);
                                MainPhoneActivity.setInfo2Text(getApplicationContext(), "latest: " + recentPostsTitles.get(4));
                                MainPhoneActivity.setInfo3Text(getApplicationContext(), "backup 2: " + recentPostsTitles.get(3));
                                MainPhoneActivity.setInfo4Text(getApplicationContext(), "backup 3: " + recentPostsTitles.get(2));
                                MainPhoneActivity.setInfo5Text(getApplicationContext(), "backup 4: " + recentPostsTitles.get(1));
                                MainPhoneActivity.setInfo6Text(getApplicationContext(), "backup 5: " + recentPostsTitles.get(0));
                            } else {
                                recentPostsTitles.remove(0);
                                recentPostsSubs.remove(0);
                                lastTitle = recentPostsTitles.get(4);
                                lastSub = recentPostsSubs.get(4);
                                secondTitle = recentPostsTitles.get(3);
                                secondSub = recentPostsSubs.get(3);
                                thirdTitle = recentPostsTitles.get(2);
                                thirdSub = recentPostsSubs.get(2);
                                fourthTitle = recentPostsTitles.get(1);
                                fourthSub = recentPostsSubs.get(1);
                                fifthTitle = recentPostsTitles.get(0);
                                fifthSub = recentPostsSubs.get(0);
                                MainPhoneActivity.setInfo2Text(getApplicationContext(), "latest: " + lastTitle);
                                MainPhoneActivity.setInfo3Text(getApplicationContext(), "backup 1: " + secondTitle);
                                MainPhoneActivity.setInfo4Text(getApplicationContext(), "backup 2: " + thirdTitle);
                                MainPhoneActivity.setInfo5Text(getApplicationContext(), "backup 3: " + fourthTitle);
                                MainPhoneActivity.setInfo6Text(getApplicationContext(), "backup 4: " + fifthTitle);
                            }
                            currentPost[0] = lastTitle;
                            currentPost[1] = categoryAttr.attr("label");
                            sendData(currentPost, recentPostsTitles, recentPostsSubs);
                            Log.d("WEARNEWS", "sent new post.");
                            MainPhoneActivity.setInfoText(getApplicationContext(), getString(R.string.newPostsText));

                            //only send recent posts every 15min.
                        } else {
                            Log.d("WEARNEWS", "no new posts");
                            MainPhoneActivity.setInfoText(getApplicationContext(), getString(R.string.noNewPostsText));
                        }

                    }
                } catch (IOException e) {
                    currentPost[0] = "URL ERROR:";
                    currentPost[1] = e.getLocalizedMessage();
                    sendData(currentPost, recentPostsTitles, recentPostsSubs);
                }
            }
        }, 0, 5000);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendData(String[] currentPost, ArrayList<String> recentPostsTitles, ArrayList<String> recentPostsSubs) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/WEARNEWS_PATHTOWATCH");
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
    /*public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = event.getDataItem();
                if (Objects.requireNonNull(dataItem.getUri().getPath()).compareTo("/WEARNEWS_PATHTOPHONE") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                }
            }

        }
    }*/
    @Override
    public void onDestroy() {
        MainPhoneActivity.setInfoText(getApplicationContext(), "");
        notificationManager.cancel(1);
        timer.cancel();
        isServiceRunning = false;
        sendData(currentPost, recentPostsTitles, recentPostsSubs);

        Log.d("WEARNEWS", "service destroyed");
        super.onDestroy();
    }
}


