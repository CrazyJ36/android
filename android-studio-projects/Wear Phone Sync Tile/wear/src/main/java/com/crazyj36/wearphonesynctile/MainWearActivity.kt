package com.crazyj36.wearphonesynctile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.wear.compose.material.Text
import androidx.compose.foundation.layout.Column


class MainWearActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text("This app includes A Wear OS Tile that does the work of showing the current int sent from the phone app.")
            }
        }
    }
}