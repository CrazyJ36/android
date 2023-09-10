package com.crazyj36.celebrateyourself;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnWag = findViewById(R.id.btnWag);
        final EditText etName = findViewById(R.id.etNameField);
        final Button btnTellMe = findViewById(R.id.btnTellMe);
        final ImageView wagImg = findViewById(R.id.wagImg);
        final TextView tvRude = findViewById(R.id.tvRude);
        final TextView tvResponse = findViewById(R.id.tvResponse);

        btnTellMe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
		        btnWag.setVisibility(0);
		        wagImg.setVisibility(0);
		        tvRude.setVisibility(0);
                tvResponse.setText(etName.getText().toString() + ", my name is probably cooler");
            }
        });

        btnWag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
	            wagImg.setBackgroundResource(R.drawable.blink);
                ((AnimationDrawable) wagImg.getBackground()).start();
            }
        });
    }

}
