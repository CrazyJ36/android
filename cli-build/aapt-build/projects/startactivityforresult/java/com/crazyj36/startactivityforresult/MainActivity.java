package com.crazyj36.startactivityforresult;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    // Makeshift reference to match amongst intents later.
    int INTENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btnChoose);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* ACTION_PICK_ACTIVITY: As started intent, returns working intent(data) string that can start picked activity.
                using onActivityResult() Intent data */
                Intent chooseIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
                startActivityForResult(chooseIntent, INTENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       /* Use same INTENT_REQUEST code as starting  intent */
       if (requestCode == INTENT_REQUEST && resultCode == RESULT_OK) {

	       Button btnLaunch = findViewById(R.id.btnLaunch);
	       btnLaunch.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View view) {

                   /* Here, intent data from startActivityResult() is used with the same intent string */
	               startActivity(data);

	           }
	       });

           /* Get the string that was recieved during onActivityResult() */
           TextView tvResult = findViewById(R.id.tvResult);
           String dataResult = data.toUri(0);
           tvResult.setText(dataResult);

       }
   }
}
