package com.crazyj36.watchfacetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.wear.activity.ConfirmationActivity

class GetComplicationPermission : ComponentActivity() {
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            if (getSharedPreferences(
                    "file_show_complication_warning",
                    Context.MODE_PRIVATE
                ).getBoolean("showComplicationWarning", true)
            ) {
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
                getSharedPreferences("file_show_complication_warning", Context.MODE_PRIVATE)
                    .edit().putBoolean(
                        "showComplicationWarning", false
                    ).apply()
                finish()
            }
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setContent {
            Box(Modifier.fillMaxSize())
        }*/
        requestPermission.launch("com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA")
    }
}