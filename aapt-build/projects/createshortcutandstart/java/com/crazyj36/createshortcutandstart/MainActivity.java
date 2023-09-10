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

        ((Button)findViewById(R.id.btnChoose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* ACTION_CREATE_SHORTCUT: Used with Intent, returns parcelable intent(data) representing the shortcut.
                Use startActivityForResult and onActivityResult() to use or get info from chosen shortcut. */
                Intent chooseIntent = new Intent(Intent.ACTION_CREATE_SHORTCUT); // or string "android.intent.action.CREATE_SHORTCUT"
                startActivityForResult(chooseIntent, INTENT_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       // Check intent result
       if (requestCode == INTENT_REQUEST && resultCode == RESULT_OK) {

           // get all intent bundle info fields into one string.
           String info = new String();
           for (String key : data.getExtras().keySet()) { // Bundle bundleOfExtras = data.getExtras() for array.
               info = info + "\n" + key + "=\n" + data.getExtras().get(key) + "\n";
           }
           ((TextView)findViewById(R.id.tvResult)).setText(info); // Shortened cast

	       Button btnLaunch = findViewById(R.id.btnLaunch);
	       btnLaunch.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View view) {

                       // get intent returned by CREATE_SHORTCUT. This is the only way I've come across.
                       // getParcelableExtra(String) retrieves extended data from the intent, android.intent.extra.shortcut.INTENT being A defualt extra.
                      //  in english: get the shortcuts' intent, which is A parcelable, from the intents' bundle.
                       // simply doing startActivity(data) gets ActivityNotFoundException, so we have to make A new intent with previous intents extra.
                       Intent shortcutIntent = data.getParcelableExtra("android.intent.extra.shortcut.INTENT");
                       // this works to, though deprecated.
                       // Intent shortcutIntent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
                       startActivity(shortcutIntent);

	           }
	       });

       }

   }
}
