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
import java.util.Timer;
import java.util.TimerTask;

public class UpdateService extends Service {
    static String[] currentPost = {"", ""};
    static String lastTitle;
    static String secondTitle;
    static String thirdTitle;
    static String fourthTitle;
    static String fifthTitle;
    static ArrayList<String> recentPosts = new ArrayList<>();
    int postCount = 0;
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
                            recentPosts.add(headline.text());
                            if (recentPosts.size() == 1) {
                                lastTitle = recentPosts.get(0);
                                MainPhoneActivity.setInfo2Text(getApplicationContext(), "latest: " + lastTitle);
                            } else if (recentPosts.size() == 2) {
                                secondTitle = recentPosts.get(1);
                                MainPhoneActivity.setInfo3Text(getApplicationContext(), "backup 1: " + secondTitle);
                            } else if (recentPosts.size() == 3) {
                                thirdTitle = recentPosts.get(2);
                                MainPhoneActivity.setInfo4Text(getApplicationContext(), "backup 2: " + thirdTitle);
                            } else if (recentPosts.size() == 4) {
                                fourthTitle = recentPosts.get(0);
                                MainPhoneActivity.setInfo5Text(getApplicationContext(), "backup 3: " + fourthTitle);
                            } else if (recentPosts.size() == 5) {
                                fifthTitle = recentPosts.get(0);
                                MainPhoneActivity.setInfo6Text(getApplicationContext(), "backup 4: " + fifthTitle);

                            } else {
                                recentPosts.remove(0);
                                lastTitle = recentPosts.get(4);
                                secondTitle = recentPosts.get(3);
                                thirdTitle = recentPosts.get(2);
                                fourthTitle = recentPosts.get(1);
                                fifthTitle = recentPosts.get(0);MainPhoneActivity.setInfo2Text(getApplicationContext(), "latest: " + lastTitle);
                                MainPhoneActivity.setInfo3Text(getApplicationContext(), "backup 1: " + secondTitle);
                                MainPhoneActivity.setInfo4Text(getApplicationContext(), "backup 2: " + thirdTitle);
                                MainPhoneActivity.setInfo5Text(getApplicationContext(), "backup 3: " + fourthTitle);
                                MainPhoneActivity.setInfo6Text(getApplicationContext(), "backup 4: " + fifthTitle);

                            }

                            currentPost[0] = lastTitle;
                            currentPost[1] = categoryAttr.attr("label");
                            sendData(currentPost, recentPosts);
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
                    sendData(currentPost, recentPosts);
                }
            }
        }, 0, 5000);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendData(String[] currentPost, ArrayList<String> recentPosts) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/message_path");
        dataMap.getDataMap().putStringArray("currentPost", currentPost);
        dataMap.getDataMap().putStringArrayList("recentPosts", recentPosts);
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
        isServiceRunning = false;
        recentPosts.removeAll(recentPosts);
        Log.d("WEARNEWS", "service destroyed");
        super.onDestroy();
    }
}


