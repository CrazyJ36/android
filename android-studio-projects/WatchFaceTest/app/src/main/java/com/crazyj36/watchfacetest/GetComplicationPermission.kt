package com.crazyj36.watchfacetest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.wear.activity.ConfirmationActivity

class GetComplicationPermission: FragmentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                //GetComplicationPermission().isGranted = true
                /*if (getSharedPreferences(
                        "file_show_complication_warning",
                        Context.MODE_PRIVATE
                    ).getBoolean("showComplicationWarning", true)
                ) {*/

                //}
                Log.d("WATCHFACETEST", "granted in activityResult")
            } else {
                Log.d("WATCHFACETEST", "not granted in activityResult.")
                //finish()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(
            "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
        )
        when {
            ContextCompat.checkSelfPermission(
                this@GetComplicationPermission,
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED -> {
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
                }
                startActivity(intent)
                /*getSharedPreferences("file_show_complication_warning", Context.MODE_PRIVATE)
                    .edit().putBoolean("showComplicationWarning", false).apply()

                 */
                Log.d("WATCHFACETEST", "granted in onCreate()")
            } else -> {
                Log.d("WATCHFACETEST", "not granted in onCreate()")
                //requestPermissionLauncher.launch(
                //    "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
                //)
            }
        }
    }
}