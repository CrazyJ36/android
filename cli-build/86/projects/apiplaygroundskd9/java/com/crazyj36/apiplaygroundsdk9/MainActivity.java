package com.crazyj36.apiplaygroundsdk9;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.os.Build;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int sdk = android.os.Build.VERSION.SDK_INT;
        TextView tv1 = findViewById(R.id.tv1);
        tv1.setText("SDK Version: " + sdk);

        Button button = findViewById(R.id.btn1);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("A Dialog");
                dialog.show();
                dialog.findViewById(R.id.dialogBtn1).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Dialog Button Pushed", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.findViewById(R.id.dialogBtn2).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }
}
