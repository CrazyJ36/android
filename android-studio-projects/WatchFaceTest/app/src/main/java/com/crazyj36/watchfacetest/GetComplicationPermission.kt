package com.crazyj36.watchfacetest

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity

class GetComplicationPermission: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(
            "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
        ), 0)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        val showComplicationWarning: Boolean =
            getSharedPreferences("file_show_complication_warning",
                Context.MODE_PRIVATE).getBoolean("showComplicationWarning", true)
        if (showComplicationWarning) {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.complicationWarningText),
                Toast.LENGTH_LONG
            ).show()
        }
        getSharedPreferences("file_show_complication_warning",
            Context.MODE_PRIVATE).edit().putBoolean("showComplicationWarning", false)
            .apply()
    }
}