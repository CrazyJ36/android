package com.crazyj36.scales;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.CharArrayWriter;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // make inverted orders here starting with root notes, feed order to setmessage
        final String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        String[] scales = new String[]{"Major", "Minor"};

        final ListView listView;
        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_expandable_list_item_1, android.R.id.text1, scales);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();


                String chosen = (String) listView.getItemAtPosition(position);
                if (chosen == "Major") {
                    // put these into a function with auto number/space.
                    dialog.setMessage(notes[0] + " " + notes[2] + " " + notes[4] + " " + notes[5] + " " + notes[7] + " " + notes[9] + " " + notes[11] + " " + notes[0]);
                } else if (chosen == "Minor") {
                    dialog.setMessage(notes[0] + " " + notes[2] +  " " + notes[3] + " " + notes[5] + " " + notes[7] + " " + notes[8] + " " + notes[10] + " " + notes[0]);
                }

                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }

        });
    }
}
