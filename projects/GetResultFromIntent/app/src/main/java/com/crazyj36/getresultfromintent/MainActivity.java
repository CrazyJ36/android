package com.crazyj36.getresultfromintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 1;
    String tvResultsTxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnChoose = findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
                startActivityForResult(chooseIntent, REQUEST_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final TextView tvResults = findViewById(R.id.tvResults);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            tvResultsTxt = data.toUri(0);

            Button btnLaunch = findViewById(R.id.btnLaunch);
            btnLaunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String componentClass = data.getComponent().toString();
                    tvResults.append(componentClass);

                    startActivity(data);

                }
            });
        }

        tvResults.setText(tvResultsTxt);
    }
}
