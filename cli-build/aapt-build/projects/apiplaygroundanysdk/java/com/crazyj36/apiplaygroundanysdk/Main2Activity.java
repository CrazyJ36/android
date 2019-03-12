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
import android.content.ActivityNotFoundException;
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
				if (sdkVersion < 23) {
					doCall();
    			} else {
					if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
						doCall();
					} else {
						Toast.makeText(Main2Activity.this, "Check phone permission", Toast.LENGTH_SHORT).show();
						requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, 0);
					}
				}
			}
		});
		// Launch website button
		Button btnWeb = findViewById(R.id.btnWeb);
		btnWeb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText etWeb = findViewById(R.id.etWeb);
				String url = etWeb.getText().toString();
				if (sdkVersion >= 8) {
					if (Patterns.DOMAIN_NAME.matcher(url).matches()) {
						try {
							String urlDomainFix = "http://www." + url;
							Intent webPage = new Intent(Intent.ACTION_VIEW, Uri.parse(urlDomainFix));
							startActivity(webPage);
							Toast.makeText(Main2Activity.this, "Domain name fixed\nOpening: " + urlDomainFix + "\nin browser", Toast.LENGTH_LONG).show();
						} catch (ActivityNotFoundException activityNotFound) {
							Toast.makeText(Main2Activity.this, "You entered A Domain name, but there is no browser installed" + activityNotFound.getLocalizedMessage(), Toast.LENGTH_LONG).show();
						}
					} else if (Patterns.WEB_URL.matcher(url).matches()) {
						try {
							Intent webPage = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
							startActivity(webPage);
							Toast.makeText(Main2Activity.this, "Opening url:\n" + url + "\nin browser", Toast.LENGTH_LONG).show();
						} catch (ActivityNotFoundException activityNotFound1) {
							Toast.makeText(Main2Activity.this, "You entered A Url, but there is no browser installed" + activityNotFound1.getLocalizedMessage(), Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(Main2Activity.this, "Enter A proper website name", Toast.LENGTH_SHORT).show();
					}
				} else {
					Intent webPage = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					Toast.makeText(Main2Activity.this, "Opening:\n" + url + "\nin browser", Toast.LENGTH_SHORT).show();
					try {
						startActivity(webPage);
					} catch (ActivityNotFoundException activityNotFound) {
						Toast.makeText(Main2Activity.this, "Must be typed as:\n\"http://www.website.com\"\nOr no browser installed", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
        // Btn SMS
		findViewById(R.id.btnSms).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText msgField = findViewById(R.id.etSms);
				String msg = msgField.getText().toString();
				Intent sendMsg = new Intent(Intent.ACTION_VIEW);
				sendMsg.setData(Uri.parse("sms:"));
				sendMsg.putExtra("sms_body", msg);
				if (msg.equals("")) {
					Toast.makeText(Main2Activity.this, "Enter message to send", Toast.LENGTH_SHORT);
				} else {
				    try {
                	    startActivity(sendMsg);
					} catch (ActivityNotFoundException activityNotFound) {
						Toast.makeText(Main2Activity.this, "No messaging app.", Toast.LENGTH_LONG).show();
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
