package com.crazyj36.createshortcutandstart;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.net.URISyntaxException;

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

                /* ACTION_CREATE_SHORTCUT: As started intent, returns intent(data) representing the ShortcutInfo() result.
                using onActivityResult() Intent data */
                Intent chooseIntent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
                //chooseIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, null);
                startActivityForResult(chooseIntent, INTENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       /* Use same INTENT_REQUEST code as starting intent */
       if (requestCode == INTENT_REQUEST && resultCode == RESULT_OK) {

	       Button btnLaunch = findViewById(R.id.btnLaunch);
	       btnLaunch.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View view) {

                   /* Here, intent data from startActivityResult() is used with the same intent string */
                   //String shortcutIntent = data.toUri(0);
                   //try {
                       startActivity(data);
                   //} catch (URISyntaxException uriSyntaxException) {
                     //  Toast.makeText(MainActivity.this, uriSyntaxException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                   //}
	           }
	       });

           /* Get the string that was recieved during onActivityResult() */
           TextView tvResult = findViewById(R.id.tvResult);
           //String dataResult = data.toUri(0);

           // Name of chosen shortcut
           String dataResult = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
           tvResult.setText(dataResult);

       }
   }
}
