package com.crazyj36.openwebpage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import android.content.Intent;
import android.net.Uri;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);
    }

    // Function that opens the webpage
    public void openWebpage(View v) {

        Intent launchIntent = new Intent(Intent.ACTION_VIEW);

        Uri msnSite = Uri.parse("http://www.msn.com");
        launchIntent.setData(msnSite);  // setDataAndType(msnSite, "text/html") caused duplicate pages to open, slowing the onClick down.

        startActivity(launchIntent);

    }
}
