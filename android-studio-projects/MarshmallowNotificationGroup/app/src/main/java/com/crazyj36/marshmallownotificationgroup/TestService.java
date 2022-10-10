package com.crazyj36.marshmallownotificationgroup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TestService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder serviceNotificationBuilder = new NotificationCompat.Builder(this, "10")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("service running")
                .setCategory(Notification.CATEGORY_SERVICE);
        startForeground(10, serviceNotificationBuilder.build());
        // Not for >= android 8.1, no notificationchannel.
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MainActivity.toast(getApplicationContext(), "service running");
        Notification notification0 = new NotificationCompat.Builder(TestService.this, "0")
                .setContentTitle("0")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        Notification notification1 = new NotificationCompat.Builder(TestService.this, "1")
                .setContentTitle("1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        Notification notification2 = new NotificationCompat.Builder(TestService.this, "2")
                .setContentTitle("2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        Notification notification3 = new NotificationCompat.Builder(TestService.this, "3")
                .setContentTitle("3")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        Notification notification4 = new NotificationCompat.Builder(TestService.this, "4")
                .setContentTitle("4")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        Notification notification5 = new NotificationCompat.Builder(TestService.this, "5")
                .setContentTitle("5")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(0, notification0);
        notificationManager.notify(1, notification1);
        notificationManager.notify(2, notification2);
        notificationManager.notify(3, notification3);
        notificationManager.notify(4, notification4);
        notificationManager.notify(5, notification5);
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
