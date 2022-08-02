package com.crazyj36.wearnews;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import java.util.ArrayList;
import java.util.Objects;

public class MainWearActivity extends FragmentActivity implements AmbientModeSupport.AmbientCallbackProvider, DataClient.OnDataChangedListener {
    TextView title;
    TextView sub;
    String newPostTitle;
    String newPostSub;
    String lastTitle;
    private AmbientModeSupport.AmbientController ambientController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.title);
        sub = findViewById(R.id.sub);
        title.setText(getString(R.string.loadingMessage));
        ambientController = AmbientModeSupport.attach(this);
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
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = event.getDataItem();
                if (Objects.requireNonNull(dataItem.getUri().getPath()).compareTo("/message_path") == 0){
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                    String[] currentPost = dataMap.getStringArray("currentPost");
                    assert currentPost != null;
                    newPostTitle = currentPost[0];
                    newPostSub = currentPost[1];
                    title.setText(newPostTitle);
                    if (!(ambientController.isAmbient())) sub.setText(newPostSub);
                    lastTitle = newPostTitle;
                    Log.d("WEARNEWS", "got new post");

                    // Store A few posts on watch to be viewed when woken from ambient mode.
                    ArrayList<String> recentPosts = dataMap.getStringArrayList("recentPosts");
                    // handle recent post backup here
                }
            }
        }
    }
    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new MyAmbientCallback();
    }
    private class MyAmbientCallback extends AmbientModeSupport.AmbientCallback {
        @Override
        public void onEnterAmbient(Bundle ambientDetails) {
            title.setText(lastTitle);
            sub.setText(getString(R.string.aodModeText));
        }

        @Override
        public void onExitAmbient() {
            sub.setText(newPostSub);
        }

        @Override
        public void onUpdateAmbient() {
        }
    }
}


