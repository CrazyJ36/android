package com.crazyj36.wearphonesync;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainPhoneActivity extends Activity implements DataClient.OnDataChangedListener {
    TextView messageView;
    String messagepath = "/messagepath";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Timer timer;
    TimerTask timerTask;
    int tryLimit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = findViewById(R.id.data);
        sharedPreferences = getSharedPreferences("count", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        messageView.setText(String.valueOf(sharedPreferences.getInt("count", 1)));
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editor.putInt("count", sharedPreferences.getInt("count", 1) + 1);
                editor.apply();
                sendData(sharedPreferences.getInt("count", 1));
                Log.d("WEARPPHONESYNCTEST", "Sent new preference");
                messageView.setText(String.valueOf(sharedPreferences.getInt("count", 1)));
            }
        });
        tryLimit = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tryLimit < 10) {
                            sendData(0);
                            sendData(sharedPreferences.getInt("count", 1));
                            tryLimit++;
                            Log.d("WEARPHONESYNCTEST", "Sent preference: " + tryLimit);
                        } else timer.cancel();
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }
    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
        tryLimit = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tryLimit < 10) {
                            sendData(0);
                            sendData(sharedPreferences.getInt("count", 1));
                            tryLimit++;
                            Log.d("WEARPHONESYNCTEST", "Sent preference: " + String.valueOf(tryLimit));
                        } else timer.cancel();
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }
    @Override
    public void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
        timer.cancel();
    }
    private void sendData(int message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(messagepath);
        dataMap.getDataMap().putInt("message", message);
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
                // dataitem is null.
            }
        });
    }
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = event.getDataItem();
                if (Objects.requireNonNull(dataItem.getUri().getPath()).compareTo(messagepath) == 0){
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                    int message = dataMap.getInt("message");
                    if (message > sharedPreferences.getInt("count", 1)) {
                        editor.putInt("count", message);
                        editor.apply();
                        sendData(0);
                        sendData(message);
                        Log.d("WEARPHONESYNCTEST", "Got new preference");
                        messageView.setText(String.valueOf(message));
                    } else if (sharedPreferences.getInt("count", 1) > message) {
                        sendData(0);
                        sendData(sharedPreferences.getInt("count", 1));
                        Log.d("WEARPHONESYNCTEST", "Sent preference");
                        messageView.setText(String.valueOf(sharedPreferences.getInt("count", 1)));
                    }
                }
            }
        }
    }
}


