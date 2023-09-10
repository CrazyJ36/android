package com.crazyj36.apiplayground;

import android.app.Dialog;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load activity_main xml definition to Activity window
        setContentView(R.layout.activity_main2);
        // Coloring
        getWindow().setNavigationBarColor(Color.LTGRAY);
        // Toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        final ImageButton toolbarMenu = findViewById(R.id.toolbarMenu);
        toolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu menu = new PopupMenu(Main2Activity.this, toolbarMenu, android.view.Gravity.END);
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
        final ImageView imgBtn = findViewById(R.id.imgColorBtn);
        final boolean[] isColored = {true};
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img1 = findViewById(R.id.imgColorBtn);
                if (isColored[0]) {
                    img1.setColorFilter(0xff0000ff);
                    isColored[0] = false;
                } else {
                    img1.setColorFilter(null);
                    isColored[0] = true;
                }
            }
        });
        imgBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] imgBtnPos = new int[2];
                imgBtn.getLocationOnScreen(imgBtnPos);
                Log.i(getResources().getString(R.string.tag), "btn loc = " + imgBtnPos[0] + " " + imgBtnPos[1]);
                Toast imgBtnInfo = Toast.makeText(Main2Activity.this, "Video Icon", Toast.LENGTH_SHORT);
                imgBtnInfo.setGravity(Gravity.START | Gravity.TOP, imgBtnPos[0], imgBtnPos[1]);
                imgBtnInfo.show();
                return true;
            }
        });
        // Switch that moves imgColor
        final Switch imgColorMvSwitch = findViewById(R.id.imgColorMvSwitch);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(148, 148);
        params.gravity = Gravity.START | Gravity.CENTER_HORIZONTAL;
        imgBtn.setLayoutParams(params);
        imgColorMvSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (imgColorMvSwitch.isChecked()) {
                    params.gravity = Gravity.END | Gravity.CENTER_HORIZONTAL;
                    imgBtn.setLayoutParams(params);
                } else {
                    params.gravity = Gravity.START | Gravity.CENTER_HORIZONTAL;
                    imgBtn.setLayoutParams(params);
                }
            }
        });
        // Picture Popup Button
        findViewById(R.id.dialogImgBtn).setOnClickListener(new View.OnClickListener() {
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
        /* Checkboxes, this is the only usable logic in the case of changing one text view per 'which is checked'.
           also try 'updating list of settings' when any, no matter which are checked.
         */
        final TextView tvChkStatus = findViewById(R.id.tvChkStatus);
        final String[] chkValues = {"None Checked", "Both Checked", "First Checked", "Second Checked"};
        final CheckBox chk1 = findViewById(R.id.chk1);
        final CheckBox chk2 = findViewById(R.id.chk2);

        tvChkStatus.setText(chkValues[0]);
        chk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk1.isChecked() && !chk2.isChecked()) {
                    tvChkStatus.setText(chkValues[2]);
                } else if (!chk1.isChecked() && chk2.isChecked()) {
                    tvChkStatus.setText(chkValues[3]);
                } else if (chk1.isChecked() && chk2.isChecked()) {
                    tvChkStatus.setText(chkValues[1]);
                } else if (!chk1.isChecked() && !chk2.isChecked()) {
                    tvChkStatus.setText(chkValues[0]);
                }
            }
        });
        chk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk1.isChecked() && !chk2.isChecked()) {
                    tvChkStatus.setText(chkValues[2]);
                } else if (!chk1.isChecked() && chk2.isChecked()) {
                    tvChkStatus.setText(chkValues[3]);
                } else if (chk1.isChecked() && chk2.isChecked()) {
                    tvChkStatus.setText(chkValues[1]);
                } else if (!chk1.isChecked() && !chk2.isChecked()) {
                    tvChkStatus.setText(chkValues[0]);
                }
            }
        });
        // make accounts list
        final EditText etAccounts1 = findViewById(R.id.etAccounts1);
        final EditText etAccounts2 = findViewById(R.id.etAccounts2);
        final TextView tvAccounts1 = findViewById(R.id.tvAccounts1);
        final TextView tvAccounts2 = findViewById(R.id.tvAccounts2);
        findViewById(R.id.btnAccounts).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String user = etAccounts1.getText().toString();
                String pass = etAccounts2.getText().toString();
                tvAccounts1.append(user + "\n");
                tvAccounts2.append(pass + "\n");
            }
        });

        // Dialog with layout example
        Button btnCustom = findViewById(R.id.btnCustom);
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Main2Activity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("C Description");
                dialog.show();
                dialog.findViewById(R.id.dialogBtn1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Main2Activity.this, "Dialog Button Pushed", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.findViewById(R.id.dialogBtn2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    // Hide status bar. Rule of thumb (for games) is to never show action bar without status bar
    public void hideStatusBar(MenuItem menuItem) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Radio buttons
    public void radioBtnColorNavBarClick(View view) {
        boolean isChecked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioBtnColorNavBar1:
                if (isChecked) getWindow().setNavigationBarColor(Color.LTGRAY);
                break;
            case R.id.radioBtnColorNavBar2:
                if (isChecked) getWindow().setNavigationBarColor(Color.GREEN);
                break;
            case R.id.radioBtnColorNavBar3:
                if (isChecked) getWindow().setNavigationBarColor(Color.RED);
                break;
        }
    }

    // Catch When the Activity is destroyed. This should only happen if back button pressed.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(getResources().getString(R.string.tag), "Activity2 Stopped");
    }

    // Catch when config changes(rotate), goes with <activity android:configChanges="orientation|screenSize" /> in AndroidManifest.xml
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(getResources().getString(R.string.tag), "Activity 2 Config Changed");
    }
}
