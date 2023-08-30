package com.crazyj36.watchfacetest

import android.os.Bundle
import android.util.Log
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
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.watchface.editor.ChosenComplicationDataSource
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    private lateinit var editorSession: EditorSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
        Toast.makeText(
            this@WatchFaceConfigActivity,
            resources.getString(R.string.watchFaceConfigurationToastText),
            Toast.LENGTH_LONG
        ).show()
        MainScope().launch(Dispatchers.Main.immediate) {
            editorSession =
                EditorSession.createOnWatchEditorSession(
                    this@WatchFaceConfigActivity
                )
            editorSession
                .openComplicationDataSourceChooser(1)
        }
    }
    @Composable
    fun WearApp() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement =
            Arrangement.Center
        ) {
            Text(
                text = resources.getString(
                    R.string.watchFaceConfigurationActivityText
                )
            )
        }
    }
}