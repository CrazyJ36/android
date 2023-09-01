package com.crazyj36.watchfacetest

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.wear.activity.ConfirmationActivity

class GetComplicationPermission: ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts
                .RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("WATCHFACETEST", "granted in activityResult")
            } else {
                Log.d("WATCHFACETEST", "not granted in activityResult.")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(
            "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
        )
        when {
            checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED -> {
                /*(if (getSharedPreferences(
                        "file_show_complication_warning",
                        Context.MODE_PRIVATE
                    ).getBoolean("showComplicationWarning", true)
                ) {*/
                    val intent = Intent(
                        this,
                        ConfirmationActivity::class.java
                    ).apply {
                        putExtra(
                            ConfirmationActivity
                                .EXTRA_ANIMATION_TYPE, ConfirmationActivity
                                .SUCCESS_ANIMATION
                        )
                        putExtra(
                            ConfirmationActivity
                                .EXTRA_MESSAGE,
                            resources.getString(R.string.complicationWarningText)
                        )
                        putExtra(
                            ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS,
                            3500
                        )
                    }
                    startActivity(intent)
                    /*getSharedPreferences("file_show_complication_warning", Context.MODE_PRIVATE)
                        .edit().putBoolean("showComplicationWarning", false).apply()*/
                    Log.d("WATCHFACETEST", "granted in onCreate()")
                //d}
            } else -> {
                Log.d("WATCHFACETEST", "not granted in onCreate()")
            }
        }
    }
}