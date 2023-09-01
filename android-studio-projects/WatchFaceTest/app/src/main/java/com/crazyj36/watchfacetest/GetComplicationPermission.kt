package com.crazyj36.watchfacetest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.wear.activity.ConfirmationActivity

class GetComplicationPermission : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                when (isGranted) {
                    /*(if (getSharedPreferences(
                                "file_show_complication_warning",
                                Context.MODE_PRIVATE
                            ).getBoolean("showComplicationWarning", true)
                        ) {*/
                    true -> {
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
                        Log.d("WATCHFACETEST", "granted in activityResult")
                    }

                    false -> {
                        Log.d("WATCHFACETEST", "not granted in activityResult.")
                    }
                }
            }
        requestPermissionLauncher.launch(
            "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
        )
    }
}