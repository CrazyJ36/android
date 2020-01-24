package com.crazyj36.getresultfromintent;

import android.app.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.content.Intent.URI_INTENT_SCHEME;


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
            TextView textView = findViewById(R.id.tvResult);
            String dataResult = data.toUri(URI_INTENT_SCHEME);
            textView.setText(dataResult);
        }
    }

}
