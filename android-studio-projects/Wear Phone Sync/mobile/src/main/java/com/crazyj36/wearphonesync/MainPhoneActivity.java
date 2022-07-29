package com.crazyj36.wearphonesync;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class MainPhoneActivity extends Activity implements DataClient.OnDataChangedListener, View.OnClickListener {

    TextView messageView;
    Button btn;
    String messagepath = "/message_path";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Phone", Toast.LENGTH_SHORT).show();
        messageView = findViewById(R.id.data);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("count", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        messageView.setText(String.valueOf(sharedPreferences.getInt("count",0)));
        new Thread() {
            public void run() {
                editor.putInt("count", sharedPreferences.getInt("count", 0) + 1);
                editor.apply();
                sendData(sharedPreferences.getInt("count", 0));
                editor.putInt("count", sharedPreferences.getInt("count", 0) - 1);
                editor.apply();
                sendData(sharedPreferences.getInt("count", 0));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "synced", Toast.LENGTH_SHORT).show();
                    }
                });
                Thread.currentThread().interrupt();
            }
        }.start();
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
    @Override
    public void onClick(View v) {
        editor.putInt("count", sharedPreferences.getInt("count", 0) + 1);
        editor.apply();
        messageView.setText(String.valueOf(sharedPreferences.getInt("count",0)));
        sendData(sharedPreferences.getInt("count", 0));
    }
    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (messagepath.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    int message = dataMapItem.getDataMap().getInt("message");
                    editor.putInt("count", message);
                    editor.apply();
                    messageView.setText(String.valueOf(sharedPreferences.getInt("count", 0)));
                }
            }
        }
    }
    private void sendData(int message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(messagepath);
        dataMap.getDataMap().putInt("message", message);
        PutDataRequest request = dataMap.asPutDataRequest();
        request.setUrgent();
        Task<DataItem> dataItemTask = Wearable.getDataClient(this).putDataItem(request);
        dataItemTask.addOnSuccessListener(new OnSuccessListener<DataItem>() {
            @Override
            public void onSuccess(DataItem dataItem) {}
        });
        dataItemTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        });
    }
}


