package com.crazyj36.launcher;
// import android necessities

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
// import widgets
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
// import java tools
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// start app class
public class AppsListActivity extends Activity {

    // create app on screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        // run app in stages, to make it easier on main thread
        loadApps();
        loadListView();
        addClickListener();
    }

    // initialize specific android packages
    private PackageManager manager;
    private ArrayList<AppDetail> apps;

    // This Section will load apps to memory
    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<ResolveInfo> availableActivities = (ArrayList<ResolveInfo>) manager.queryIntentActivities(i, 0);
        // Sort apps by name
        Collections.sort(availableActivities, new Comparator<ResolveInfo>() {
            public int compare(ResolveInfo emp1, ResolveInfo emp2) {
                return emp1.loadLabel(manager).toString().compareToIgnoreCase(emp2.loadLabel(manager).toString());
            }
        });
        for (ResolveInfo ri : availableActivities) {
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }


    // This section puts into apps to a list view
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
                return convertView;
            }
        };
        list.setAdapter(adapter);
    }


    // do stuff with the apps
    private void addClickListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(pos).name.toString());
                AppsListActivity.this.startActivity(i);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Dialog pkgDiag = new Dialog(AppsListActivity.this);
                pkgDiag.setContentView(R.layout.dialog);
                pkgDiag.setTitle(apps.get(i).name);
                pkgDiag.show();
                pkgDiag.findViewById(R.id.pkgUninstallBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse("package:" + apps.get(i).name.toString());
                        Intent uninstall = new Intent(Intent.ACTION_DELETE, uri);
                        startActivity(uninstall);
                    }
                });
                return false;
            }
        });
    }


    // done
}

