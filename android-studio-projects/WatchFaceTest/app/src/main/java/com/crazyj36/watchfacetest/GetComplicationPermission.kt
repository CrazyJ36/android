package com.crazyj36.watchfacetest

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.FragmentActivity
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Confirmation

class GetComplicationPermission: FragmentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                if (getSharedPreferences(
                        "file_show_complication_warning",
                        Context.MODE_PRIVATE
                    ).getBoolean("showComplicationWarning", true)
                ) {
                    getSharedPreferences("file_show_complication_warning", Context.MODE_PRIVATE)
                        .edit().putBoolean("showComplicationWarning", false).apply()
                    setContent {
                        Confirmation(onTimeout = { finish() }) {
                            Text(
                                modifier = Modifier.fillMaxSize(),
                                text = resources.getString(
                                    R.string.complicationWarningText),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                finish()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            requestPermissionLauncher.launch(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            )
    }
}