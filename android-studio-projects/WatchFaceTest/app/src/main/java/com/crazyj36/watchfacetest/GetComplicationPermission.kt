package com.crazyj36.watchfacetest

import android.os.Bundle
import androidx.activity.ComponentActivity

class GetComplicationPermission : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(
            "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"),
            0)
        finish()
    }
}