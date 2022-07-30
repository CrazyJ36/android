package com.crazyj36.wearphonesync;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Timer;
import java.util.TimerTask;

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

        sendData(0); // sending placeholder int causes A change, allowing onDataChanged to run code.
        sendData(sharedPreferences.getInt("count", 1));
        sendData(0);
        sendData(sharedPreferences.getInt("count", 1));
        sendData(0);
        sendData(sharedPreferences.getInt("count", 1));
        sendData(0);
        sendData(sharedPreferences.getInt("count", 1));


    }
    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
        sendData(0);
        sendData(sharedPreferences.getInt("count", 1));
        sendData(0);
        sendData(sharedPreferences.getInt("count", 1));
        sendData(0);
        sendData(sharedPreferences.getInt("count", 1));
        sendData(0);
        sendData(sharedPreferences.getInt("count", 1));
    }
    @Override
    public void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }
    @Override
    public void onClick(View v) {
        editor.putInt("count", sharedPreferences.getInt("count", 1) + 1);
        editor.apply();
        sendData(sharedPreferences.getInt("count", 1));
    }
    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (messagepath.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    int message = dataMapItem.getDataMap().getInt("message");

                    if (message > 0) {
                        if (sharedPreferences.getInt("count", 1) < message) {
                            editor.putInt("count", message);
                            editor.apply();
                            messageView.setText(String.valueOf(message));
                            sendData(0);
                            sendData(message);
                            sendData(0);
                            sendData(message);
                            sendData(0);
                            sendData(message);
                            sendData(0);
                            sendData(message);
                        } else if (sharedPreferences.getInt("count", 1) > message) {
                            messageView.setText(String.valueOf(sharedPreferences.getInt("count", 1)));
                            sendData(0);
                            sendData(sharedPreferences.getInt("count", 1));
                            sendData(0);
                            sendData(sharedPreferences.getInt("count", 1));
                            sendData(0);
                            sendData(sharedPreferences.getInt("count", 1));
                            sendData(0);
                            sendData(sharedPreferences.getInt("count", 1));
                        } else if (sharedPreferences.getInt("count", 1) == message){
                            messageView.setText(String.valueOf(message));
                        }
                    }
                    Log.d("SEND_DATA_TEST", "data changed on message_path");
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
            public void onSuccess(DataItem dataItem) {
                Log.d("SEND_DATA_TEST", "data sent");
            }
        });
        dataItemTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "sendData failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


