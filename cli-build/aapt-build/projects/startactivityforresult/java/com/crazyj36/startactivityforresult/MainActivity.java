package com.crazyj36.startactivityforresult;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;

public class MainActivity extends Activity {

    static final int INTENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btnChoose);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseIntent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
                startActivityForResult(chooseIntent, INTENT_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == INTENT_REQUEST && resultCode == RESULT_OK) {
           TextView tvResult = findViewById(R.id.tvResult);
           String dataResult = data.toUri(2);
           tvResult.setText(dataResult + "\n\n can parseUri after this to start what was chosen. Try it.");
       }
   }
}
