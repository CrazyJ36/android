package com.crazyj36.watchfacetest

import android.os.Bundle
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
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Text
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    private lateinit var editorSession: EditorSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
        MainScope().launch(Dispatchers.Main.immediate) {
            delay(3000)
            editorSession = EditorSession.createOnWatchEditorSession(
                this@WatchFaceConfigActivity
            )
            delay(3000)
            editorSession
                .openComplicationDataSourceChooser(1)
            finish()
        }
    }
    @Composable
    fun WearApp() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = resources.getString(
                    R.string.watchFaceConfigurationActivityText
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}