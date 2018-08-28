package com.crazyj36.launcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Load code into view(Last Step)
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadApps();
        loadListView();
        //addClickListener();
    }

// Get apps activities, Put activities into array
    private PackageManager manager;
    private ArrayList<AppDetail> apps;

    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<>();
        Intent i = new Intent(Intent.ACTION_MAIN, null); // in these two lines, all intents with
        i.addCategory(Intent.CATEGORY_LAUNCHER); //> action_main and category_launcher are gathered to i

        ArrayList<ResolveInfo> availableActivities = (ArrayList<ResolveInfo>) manager.queryIntentActivities(i, 0);

// sort availableActivities labels
        Collections.sort(availableActivities, new Comparator<ResolveInfo>() {
            public int compare(ResolveInfo str1, ResolveInfo str2) {
                return str1.loadLabel(manager).toString().compareToIgnoreCase(str2.loadLabel(manager).toString());
            }
        });

// Set item info: label, package name, icon
        for (ResolveInfo ri : availableActivities) {
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }

// put array into list view
    private ListView list;
    private void loadListView() {
        list = findViewById(R.id.apps_list);
        final ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }
                ImageView appIcon = convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).icon);
                TextView appLabel = convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).label);
                TextView appName = convertView.findViewById(R.id.item_app_name);
                appName.setText(apps.get(position).name);
                return convertView;
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(pos).name.toString());
                MainActivity.this.startActivity(i);
            }
        });

    }

// Set launch package name on list item click
    //private void addClickListener() {
    //}
}

