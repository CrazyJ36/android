package com.crazyj36.textdatabase;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
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
FileWriter fileWriter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) &&
                    !(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        final EditText editText1 = findViewById(R.id.editText1);
        Button button1 = findViewById(R.id.buttton1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String file_dir = Environment.getExternalStorageDirectory()
                        .toString() + "/text_database.txt";
                File data_file = new File(file_dir);
                try {
                    FileWriter fileWriter = new FileWriter(data_file, true);
                    String input = editText1.getText() + "\n";
                    fileWriter.append(input);
                    fileWriter.close();
                } catch (IOException ioException) {
                    Toast.makeText(MainActivity.this,
                            "Could initialize FileWriter for database file.\n" +
                                    ioException.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}