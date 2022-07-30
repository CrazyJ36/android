package com.crazyj36.wearphonesync;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = findViewById(R.id.data);
        sharedPreferences = getSharedPreferences("count", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editor.putInt("count", sharedPreferences.getInt("count", 1) + 1);
                editor.apply();
            }
        });
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sendData(0);
                sendData(sharedPreferences.getInt("count", 1));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageView.setText(String.valueOf(sharedPreferences.getInt("count", 1)));
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 500, 500);
    }
    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
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
                    }
                }
            }
        }
    }
}


