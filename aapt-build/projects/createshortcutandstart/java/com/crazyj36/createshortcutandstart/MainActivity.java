package com.crazyj36.createshortcutandstart;

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

                /* ACTION_CREATE_SHORTCUT: Used with Intent, returns parcelable intent(data) representing the shortcut
                use startActivityForResult and onActivityResult() to chosen shortcut. */
                Intent chooseIntent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
                startActivityForResult(chooseIntent, INTENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       // Check intent result
       if (requestCode == INTENT_REQUEST && resultCode == RESULT_OK) {

           // Show chosen shortcuts' intent and name.
           TextView tvResult = findViewById(R.id.tvResult);
           String dataResult = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME) //
               + "\n" + data.toUri(0);
           tvResult.setText(dataResult);

	       Button btnLaunch = findViewById(R.id.btnLaunch);
	       btnLaunch.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View view) {

                       // data can be parsed and the extra for received for the shortcuts' intent is android.intent.extra.shortcut.INTENT
                       Intent shortcutIntent = data.getParcelableExtra("android.intent.extra.shortcut.INTENT");
                       startActivity(shortcutIntent);

	           }
	       });

       }
   }
}
