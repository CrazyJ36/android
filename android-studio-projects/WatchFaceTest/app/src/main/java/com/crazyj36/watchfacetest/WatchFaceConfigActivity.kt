package com.crazyj36.watchfacetest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Text
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    private lateinit var editorSession: EditorSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setContent {
            WearApp()
        }*/
        MainScope().launch(Dispatchers.Main.immediate) {
            EditorSession.createOnWatchEditorSession(
                this@WatchFaceConfigActivity
            )
            Toast.makeText(
                this@WatchFaceConfigActivity,
                resources.getString(R.string.watchFaceConfigurationToastText),
                Toast.LENGTH_LONG
            ).show()
            editorSession
                .openComplicationDataSourceChooser(1)
            finish()
        }
    }
    /*@Composable
    fun WearApp() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = resources.getString(
                    R.string.watchFaceConfigurationActivityText
                )
            )
        }
    }*/
}