package com.crazyj36.preferencesusage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);

        TextView tvResults = findViewById(R.id.tvResult);

        // The simple way preferences and set data accordingly.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (sharedPreferences.getBoolean("preference", false) == true) {
            tvResults.setText(getResources().getString(R.string.tvResultOnTxt));
        } else {
            tvResults.setText(getResources().getString(R.string.tvResultOffTxt));
        }

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
	    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSettings:
                Intent launchPreferences = new Intent(MainActivity.this, ExamplePreferenceActivity.class);
                startActivity(launchPreferences);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
