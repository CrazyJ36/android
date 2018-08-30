package com.crazyj36.apiplayground;

import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;

import static android.view.Gravity.END;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        final ImageButton toolbarMenu = findViewById(R.id.toolbarMenu);
        toolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu menu = new PopupMenu(Main2Activity.this, toolbarMenu, END);
                menu.getMenuInflater().inflate(R.menu.options_activity2, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int menuItemChosen = menuItem.getItemId();
                        if (menuItemChosen == R.id.toolbarAboutActivity2) {
                            Toast.makeText(Main2Activity.this, "Drawable Stuff", Toast.LENGTH_SHORT).show();
                        } else if (menuItemChosen == R.id.toolbarExit2) {
                            Main2Activity.this.finishAffinity();
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });

        // Image Color Button
        ImageView imgBtn = findViewById(R.id.act2VideoIcon);
        final boolean[] isColored = {true};
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img1 = findViewById(R.id.act2VideoIcon);
                if (isColored[0]) {
                    img1.setColorFilter(0xff0000ff);
                    isColored[0] = false;
                } else {
                    img1.setColorFilter(null);
                    isColored[0] = true;
                }
            }
        });

        // Picture Popup Button
        findViewById(R.id.act2PicBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder picDialog = new AlertDialog.Builder(Main2Activity.this);
                picDialog.setTitle("A Nature Picture");
                picDialog.setView(R.layout.picpopup);
                picDialog.setCancelable(true);
                picDialog.create();
                picDialog.show();
            }
        });

        // List view
        final String[] mainWords = new MainActivity().myWords();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_view, mainWords);
        ListView listView1 = findViewById(R.id.listView1);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String listClickText = mainWords[i] + " selected, " + "List index " + l;
                Toast.makeText(Main2Activity.this, listClickText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Catch When the Activity is destroyed. This should only happen if back button pressed.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Activity 2 stopped", Toast.LENGTH_SHORT).show();
    }

    // Catch when config changes(rotate), goes with <activity android:configChanges="orientation|screenSize" /> in AndroidManifest.xml
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "Activity 2 Config Changed", Toast.LENGTH_SHORT).show();
    }
}
