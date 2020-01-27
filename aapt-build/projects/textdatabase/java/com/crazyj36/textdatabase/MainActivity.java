package com.crazyj36.textdatabase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);
        final EditText et = findViewById(R.id.et);
        final Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Needed to add in AndroidManifest.xml permission WRITE_EXTERNAL_STORAGE
                String file_dir = getExternalFilesDir(null).toString() + "/textdatabase.txt";
                File file = new File(file_dir);
                String text = et.getText() + "\n";
                try {
                    FileWriter fileWriter = new FileWriter(file, true);
                    fileWriter.append(text);
                    fileWriter.close();
                } catch (IOException ioException) {
                    Toast.makeText(MainActivity.this, ioException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
