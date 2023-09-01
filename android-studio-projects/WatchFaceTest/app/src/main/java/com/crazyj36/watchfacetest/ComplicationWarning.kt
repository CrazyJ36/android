package com.crazyj36.watchfacetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Confirmation

class ComplicationWarning : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Confirmation(onTimeout = { finish() }) {
                Text(text = resources.getString(R.string.complicationWarningToastText))
            }
        }
    }
}