package com.crazyj36.watchfacetest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity

class GetComplicationPermission: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            requestPermissions(
                arrayOf(
                    "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
                ),
                0
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (getSharedPreferences(
                "file_show_complication_warning",
                Context.MODE_PRIVATE
            ).getBoolean("showComplicationWarning", true)
        ) {
            startActivity(
                Intent(this, ComplicationWarning::class.java)
                    .setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    )
            )
            getSharedPreferences("file_show_complication_warning", Context.MODE_PRIVATE)
                .edit().putBoolean("showComplicationWarning", false).apply()
        }
    }
}