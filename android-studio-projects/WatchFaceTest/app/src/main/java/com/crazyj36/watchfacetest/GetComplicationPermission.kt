package com.crazyj36.watchfacetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.FragmentActivity
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Confirmation

class GetComplicationPermission: FragmentActivity() {
    private var isGranted: Boolean = false
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                //GetComplicationPermission().isGranted = true
                /*if (getSharedPreferences(
                        "file_show_complication_warning",
                        Context.MODE_PRIVATE
                    ).getBoolean("showComplicationWarning", true)
                ) {*/
                val intent = Intent(this,
                    ConfirmationActivity::class.java).apply {
                        putExtra(ConfirmationActivity
                            .EXTRA_ANIMATION_TYPE, ConfirmationActivity
                            .SUCCESS_ANIMATION)
                        putExtra(ConfirmationActivity
                            .EXTRA_MESSAGE,
                            resources.getString(R.string.complicationWarningText))
                    }
                startActivity(intent)
                    /*getSharedPreferences("file_show_complication_warning", Context.MODE_PRIVATE)
                        .edit().putBoolean("showComplicationWarning", false).apply()

                     */
                    Log.d("WATCHFACETEST", "granted")
                //}
            } else {
                Log.d("WATCHFACETEST", "not granted.")
                //finish()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            requestPermissionLauncher.launch(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            )
    }
}