package com.crazyj36.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import java.util.List;

public class MainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    PackageManager packageManager = getPackageManager();

    List<ApplicationInfo> listOfPackages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    List<String> appLabel = listOfPackages.

    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_layout, appLabel);
    ListView listView = findViewById(R.id.lv);
    listView.setAdapter(arrayAdapter);

    }
}
