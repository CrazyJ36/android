package com.crazyj36.apiplaygroundanysdk;

import android.app.Activity;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.net.Uri;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Patterns;

public class Main2Activity extends Activity {
    // variables available to whole class
	final int sdkVersion = android.os.Build.VERSION.SDK_INT;
    // start of app
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
		// btn call
		findViewById(R.id.btnCall).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View callBtnView) {
				String callPerm = "Manifest.permission.CALL_PHONE";
				if ((sdkVersion >= 23) && (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
					requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, 0);
				}
				if (sdkVersion < 23) {
					doCall();
    			} else {
					if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
						doCall();
					} else {
						Toast.makeText(Main2Activity.this, "Check phone permission", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
    // End of onCreate
    }
	// call phone method
	public void doCall() {
		EditText etCall = findViewById(R.id.etCall);
		String num = etCall.getText().toString();
		if (!Patterns.PHONE.matcher(num).matches()) {
			Toast.makeText(Main2Activity.this, "Enter normal phone number", Toast.LENGTH_SHORT).show();
		} else {
			Uri data = Uri.parse("tel:" + num);
			Intent call = new Intent(Intent.ACTION_CALL, data);
			Toast.makeText(Main2Activity.this, "Calling " + num, Toast.LENGTH_SHORT).show();
			startActivity(call);
		}
	}
}
