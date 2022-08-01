package com.crazyj36.wearnews;

import static com.crazyj36.wearnews.MainPhoneActivity.info;
import static com.crazyj36.wearnews.MainPhoneActivity.setInfoText;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
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

public class UpdateService extends Service {

    static String[] data = {"", ""};
    static String lastTitle = "";
    Looper serviceLooper;
    ServiceHandler serviceHandler;
    NotificationManager notificationManager;
    static Timer timer;
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
        timer = new Timer();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainPhoneActivity.class), 0);
        NotificationChannel notificationChannel = new NotificationChannel("notifychannel", "Notify Button", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new Notification.Builder(this, "notifychannel")
                .setSmallIcon(R.drawable.ic_launcher)  // the status icon
                .setTicker(getString(R.string.serviceRunningTxt))  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Wear News")  // the label of the entry
                .setContentText(getString(R.string.serviceRunningTxt))  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();
        notificationManager.notify(1, notification);
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        if (MainPhoneActivity.restart) {
            this.sendBroadcast(broadcastIntent);
        } else {
            info.setText("");
            notificationManager.cancel(1);
            stopService(broadcastIntent);
            Toast.makeText(this, "service destroyed", Toast.LENGTH_SHORT).show();
            timer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message serviceMessage = serviceHandler.obtainMessage();
                serviceMessage.arg1 = startId;
                serviceHandler.sendMessage(serviceMessage);
                Document doc;
                try {
                    //doc = Jsoup.connect("https://www.reddit.com/r/all/new/.rss").get(); // resetting to invalid mark error, but then works.
                    doc = Jsoup.connect("https://www.reddit.com/user/crazy_j36/m/myrss/new/.rss").get();
                    Element headline = doc.select("feed entry title").first();
                    Element categoryAttr = doc.select("feed entry category").first();
                    data[0] = headline.text();
                    data[1] = categoryAttr.attr("label");
                    if (!(lastTitle.equals(data[0]))) {
                        sendData(data);
                        Log.d("WEARNEWS", "sent new post.");
                        lastTitle = data[0];
                        setInfoText(getString(R.string.newPostsText));
                    } else {
                        Log.d("WEARNEWS", "no new posts");
                        setInfoText(getString(R.string.noNewPostsText));
                    }
                } catch (IOException e) {
                    data[0] = "URL ERROR:";
                    data[1] = e.getLocalizedMessage();
                    sendData(data);
                }
            }
        }, 0, 5000);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
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
}
class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (MainPhoneActivity.restart) {
            Log.i("WEARNEWS", "Service tried to stop");
            context.startForegroundService(new Intent(context, UpdateService.class));
            Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        }
    }
}

