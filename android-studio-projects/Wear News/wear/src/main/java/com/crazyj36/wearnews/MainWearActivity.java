package com.crazyj36.wearnews;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        info.setText(R.string.loadingMessage);
        new ArrayAdapter<String>(getApplicationContext(), R.layout.recent_posts_list, recentPostsTitles);
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
                    currentTitle = currentPost[0];
                    currentSub = currentPost[1];
                    recentPostsTitles = dataMap.getStringArrayList("recentPostsTitles");
                    recentPostsSubs = dataMap.getStringArrayList("recentPostsSubs");
                    postListAdapter = new PostListAdapter(this, recentPostsTitles);
                    listView = findViewById(R.id.recentPostsListView);
                    listView.setAdapter(postListAdapter);
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
            info.setText(currentTitle);
        }

        @Override
        public void onExitAmbient() {
            listView.setVisibility(View.VISIBLE);
            //info.setText(Time)
        }

        @Override
        public void onUpdateAmbient() {
        }
    }
}



