package com.crazyj36.createshortcutandstart;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import java.net.URISyntaxException;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;

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

                       ShortcutInfo.Builder shortcut = new ShortcutInfo.Builder(MainActivity.this, "id1");
                       shortcut.setIntent(data);
                       ShortcutInfo shortcutInfo = shortcut.build();
                       ShortcutManager shortcutManager = createShortcutResultIntent(shortcutInfo);
                       startActivity(shortcutIntent);
	           }
	       });

           TextView tvResult = findViewById(R.id.tvResult);

           // Gets Name of chosen shortcut
           // String dataResult = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);

           String dataResult = data.toUri(Intent.URI_INTENT_SCHEME);
           tvResult.setText(dataResult);

       }
   }
}
