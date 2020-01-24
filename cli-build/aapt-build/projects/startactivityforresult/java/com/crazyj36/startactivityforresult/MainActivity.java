package com.crazyj36.startactivityforresult;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

public class MainActivity extends Activity {
    int INTENT_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btnChoose);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* For intents here:
                   ACTION_PICK_ACTIVITY: returns working intent(data) to chosen activity.
                     and data.toUri(0) will show to full intent string.
                     Can startActivity(data) in onActivityResult()
                   ACTION_CREATE_SHORTCUT:

                   ACTION...:

                */
                Intent chooseIntent = new Intent(Intent.ACTION_SEND);
                chooseIntent.putStringExtra("CRA");
                startActivityForResult(chooseIntent, INTENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == INTENT_REQUEST && resultCode == RESULT_OK) {
           TextView tvResult = findViewById(R.id.tvResult);
           //String dataResult = data.toUri(0);
           //tvResult.setText(dataResult);
           String dataResult = data.toUri(0);
           tvResult.setText(dataResult);
	       Button btnLaunch = findViewById(R.id.btnLaunch);
	       btnLaunch.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View view) {
	               //startActivity(data);
	           }
	       });
       }
   }
}
