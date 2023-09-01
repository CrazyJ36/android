package com.crazyj36.watchfacetest

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity

class GetComplicationPermission: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA")
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
                ),
                0
            )
            finish()
        }*/
    }
}