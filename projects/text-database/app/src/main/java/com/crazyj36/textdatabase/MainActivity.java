package com.crazyj36.textdatabase;

import android.app.Activity;
import android.os.Bundle;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText1 = findViewById(R.id.editText1);
        String file_dir = Environment.getExternalStorageDirectory()
                .toString() + "/text_database.txt";
        final File data_file = new File(file_dir);
        Toast.makeText(this, data_file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        
        Button button1 = findViewById(R.id.buttton1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileWriter fileWriter = new FileWriter(data_file, true);
                    fileWriter.append(editText1.getText());
                    fileWriter.close();
                } catch (IOException ioException) {
                    Toast.makeText(MainActivity.this, "Io Exception" + ioException.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}