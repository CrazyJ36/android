package com.crazyj36.scales;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.CharArrayWriter;

public class MainActivity Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView;
        listView = (ListView) findViewById(R.id.list);

        String orig = "C C#";
        char note1 = orig.charAt(0);
        String note2 = orig.substring(1,4);

        final String var1 = ( note2+" " + note1);
        Toast.makeText(getApplicationContext(), var1, Toast.LENGTH_LONG)
                .show();

        String[] values = new String[]{"Major", "Natural Minor", "Harmonic Minor", "Melodic Minor",
                "Whole Tone", "Mixolydian", "Phrygian  Dominant", "Pentatonic Major",
                "Pentatonic Minor",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemValue = (String) listView.getItemAtPosition(position);
                AlertDialog ipopUp = new AlertDialog.Builder(MainActivity.this).create();
                ipopUp.setMessage("Variants of "+itemValue + "are:"+ var1);
                ipopUp.setButton(AlertDialog.BUTTON_NEUTRAL, "Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                ipopUp.show();
            }

        });
    }
}
