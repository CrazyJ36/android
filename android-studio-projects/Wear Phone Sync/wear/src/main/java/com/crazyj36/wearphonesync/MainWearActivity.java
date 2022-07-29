package com.crazyj36.wearphonesync;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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

public class MainWearActivity extends FragmentActivity implements AmbientModeSupport.AmbientCallbackProvider, DataClient.OnDataChangedListener {

    TextView messageView;
    Button btn;
    String messagepath = "/message_path";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Wear", Toast.LENGTH_SHORT).show();
        messageView = findViewById(R.id.data);
        btn = findViewById(R.id.btn);
        sharedPreferences = getSharedPreferences("count", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("count", sharedPreferences.getInt("count", 0) + 1);
                editor.apply();
                sendData(sharedPreferences.getInt("count", 0));
            }
        });
        messageView.setText(String.valueOf(sharedPreferences.getInt("count", 0)));
        sendData(0);
    }
    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
        messageView.setText(String.valueOf(sharedPreferences.getInt("count", 0)));
        sendData(0);
    }
    @Override
    public void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
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
                        if (message > sharedPreferences.getInt("count", 0)) {
                            editor.putInt("count", message);
                            editor.apply();
                            messageView.setText(String.valueOf(message));
                        } else { // other app just opened.
                            messageView.setText(String.valueOf(sharedPreferences.getInt("count", 0)));
                            sendData(sharedPreferences.getInt("count", 0));
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "got 0", Toast.LENGTH_SHORT).show();
                    }
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

            }
        });
        dataItemTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "sendData failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new AmbientModeSupport.AmbientCallback() {
            public void onEnterAmbient(Bundle ambientDetails) {

            }
            public void onExitAmbient(Bundle ambientDetails) {

            }
        };
    }
}


