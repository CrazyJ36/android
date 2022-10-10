package com.crazyj36.notificationgrouptest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TestService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        // setup notification channel on api 26.
        if (Build.VERSION.SDK_INT > 26) {notificationManager.createNotificationChannel(new NotificationChannel(
                "NOTIFICATION_CHANNEL", "notification channel", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // on android 6.0 or less, don't group notifications unless the notifications are able to simply .addLine() to summary,
        NotificationCompat.Builder notification1 = new NotificationCompat.Builder(TestService.this, "NOTIFICATION_CHANNEL")
                .setContentTitle("1")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.Builder notification2 = new NotificationCompat.Builder(TestService.this, "NOTIFICATION_CHANNEL")
                .setContentTitle("2")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.Builder notification3 = new NotificationCompat.Builder(TestService.this, "NOTIFICATION_CHANNEL")
                .setContentTitle("3")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.Builder notification4 = new NotificationCompat.Builder(TestService.this, "NOTIFICATION_CHANNEL")
                .setContentTitle("4")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.Builder notification5 = new NotificationCompat.Builder(TestService.this, "NOTIFICATION_CHANNEL")
                .setContentTitle("5")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.Builder notification6 = new NotificationCompat.Builder(TestService.this, "NOTIFICATION_CHANNEL")
                .setContentTitle("6")
                .setSmallIcon(R.mipmap.ic_launcher);

        // on >= 7.0 set A group for what is not persistent/separate.
        if (Build.VERSION.SDK_INT >= 24) {
            MainActivity.toast(getApplicationContext(), "Android version is >= 7.0, grouping similar notifications to summary");
            notification1.setGroup("notificationGroup");
            notification2.setGroup("notificationGroup");
            notification3.setGroup("notificationGroup");
            notification4.setGroup("notificationGroup");
            notification5.setGroup("notificationGroup");
            notification6.setGroup("notificationGroup");
            NotificationCompat.Builder notificationSummary = new NotificationCompat.Builder(TestService.this, "NOTIFICATION_CHANNEL")
                    .setContentTitle("Summary")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setGroup("notificationGroup")
                    .setGroupSummary(true);
            notificationManager.notify(0, notificationSummary.build());
        } else {
            MainActivity.toast(getApplicationContext(), "Android version < 7.0, not grouping notifications into summary.");
        }

        // notify all after setup.
        notificationManager.notify(1, notification1.build());
        notificationManager.notify(2, notification2.build());
        notificationManager.notify(3, notification3.build());
        notificationManager.notify(4, notification4.build());
        notificationManager.notify(5, notification5.build());
        notificationManager.notify(6, notification6.build());
        NotificationCompat.Builder serviceNotificationBuilder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("service running")
                .setCategory(Notification.CATEGORY_SERVICE);
        startForeground(10, serviceNotificationBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
