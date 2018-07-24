package com.crazyj36.navigationtests;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Button1 Clicked, you can long press it to", 1).show();
                btn1.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        AlertDialog popUp1 = new Builder(MainActivity.this).create();
                        popUp1.setMessage("You've long clicked Button1");
                        popUp1.setButton(-3, "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        popUp1.show();
                        return true;
                    }
                });
            }
        });
    }
}
