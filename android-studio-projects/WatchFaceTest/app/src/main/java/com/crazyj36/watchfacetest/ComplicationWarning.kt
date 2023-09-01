package com.crazyj36.watchfacetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Confirmation

class ComplicationWarning : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}