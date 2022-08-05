package com.crazyj36.redditforwear;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainWearActivity extends FragmentActivity implements AmbientModeSupport.AmbientCallbackProvider, DataClient.OnDataChangedListener {
    boolean isServiceRunning = false;
    TextView info;
    ListView listView;
    TextClock textClock;
    String currentTitle;
    String currentSub;
    static ArrayList<String> recentPostsTitles = new ArrayList<>();
    static ArrayList<String> recentPostsSubs = new ArrayList<>();
    private AmbientModeSupport.AmbientController ambientController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = findViewById(R.id.info);
        textClock = findViewById(R.id.textClock);
        new ArrayAdapter<>(getApplicationContext(), R.layout.recent_posts_list, recentPostsTitles);
        listView = findViewById(R.id.recentPostsListView);
        ambientController = AmbientModeSupport.attach(this);
        setup();
    }
    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
        setup();
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
                if (Objects.requireNonNull(dataItem.getUri().getPath()).compareTo("/WEARNEWS_MESSAGECHANNEL") == 0){
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                    String[] currentPost = dataMap.getStringArray("currentPost");
                    assert currentPost != null;
                    currentTitle = currentPost[0];
                    currentSub = currentPost[1];
                    recentPostsTitles = dataMap.getStringArrayList("recentPostsTitles");
                    recentPostsSubs = dataMap.getStringArrayList("recentPostsSubs");
                    Collections.reverse(recentPostsTitles);
                    Collections.reverse(recentPostsSubs);
                    listView.setAdapter(new PostListAdapter(this, recentPostsTitles));
                    isServiceRunning = dataMap.getBoolean("checkIsServiceRunning");
                    setup();
                    Log.d("WEARNEWS", "got new post");
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
            setup();
        }
        @Override
        public void onExitAmbient() {
            setup();
        }
        @Override
        public void onUpdateAmbient() {
            setup();
        }
    }
    public void setup() {
        if (!isServiceRunning && ambientController.isAmbient()) {
            listView.setVisibility(View.GONE);
            textClock.setVisibility(View.GONE);
            info.setVisibility(View.VISIBLE);
            info.setTextColor(getColor(R.color.dark_grey));
            info.setText(R.string.loadingMessage);
        } else if (!isServiceRunning && !ambientController.isAmbient()) {
            listView.setVisibility(View.GONE);
            textClock.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
            info.setTextColor(getColor(R.color.white));
            info.setText(R.string.loadingMessage);
        } else if (isServiceRunning && ambientController.isAmbient()) {
            listView.setVisibility(View.GONE);
            textClock.setVisibility(View.GONE);
            info.setVisibility(View.VISIBLE);
            info.setTextColor(getColor(R.color.dark_grey));
            info.setText(currentTitle);
        } else if (isServiceRunning && !ambientController.isAmbient()) {
            listView.setVisibility(View.VISIBLE);
            textClock.setVisibility(View.VISIBLE);
            info.setVisibility(View.GONE);
            info.setTextColor(getColor(R.color.white));
            info.setText(currentTitle);
        }
    }
}