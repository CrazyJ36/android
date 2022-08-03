package com.crazyj36.wearnews;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    String currentTitle;
    String currentSub;
    static ArrayList<String> recentPostsTitles = new ArrayList<>();
    static ArrayList<String> recentPostsSubs = new ArrayList<>();
    PostListAdapter postListAdapter;
    private AmbientModeSupport.AmbientController ambientController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = findViewById(R.id.info);
        new ArrayAdapter<>(getApplicationContext(), R.layout.recent_posts_list, recentPostsTitles);
        listView = findViewById(R.id.recentPostsListView);
        ambientController = AmbientModeSupport.attach(this);
        if (!isServiceRunning) info.setText(R.string.loadingMessage);
        else info.setText("");
    }
    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
        if (!isServiceRunning) info.setText(R.string.loadingMessage);
        else info.setText("");
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

                    isServiceRunning = dataMap.getBoolean("checkIsServiceRunning");
                    if (!isServiceRunning) {
                        listView.setVisibility(View.GONE);
                        info.setText(R.string.loadingMessage);
                    } else info.setText("");

                    postListAdapter = new PostListAdapter(this, recentPostsTitles);
                    listView.setAdapter(postListAdapter);

                    if (ambientController.isAmbient()) {
                        if (!isServiceRunning) info.setText(R.string.loadingMessage);
                        else info.setText(currentTitle);
                    }
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
            listView.setVisibility(View.GONE);
            info.setTextColor(getApplicationContext().getColor(R.color.dark_grey));
            if (!isServiceRunning) info.setText(R.string.loadingMessage);
            else info.setText(currentTitle);
        }
        @Override
        public void onExitAmbient() {
            listView.setVisibility(View.VISIBLE);
            info.setTextColor(getApplicationContext().getColor(R.color.white));
            if (!isServiceRunning) {
                listView.setVisibility(View.GONE);
                info.setText(R.string.loadingMessage);
            } else info.setText("");
        }

        @Override
        public void onUpdateAmbient() {
            if (!isServiceRunning) info.setText(R.string.loadingMessage);
            else info.setText(currentTitle);
        }
    }

}



