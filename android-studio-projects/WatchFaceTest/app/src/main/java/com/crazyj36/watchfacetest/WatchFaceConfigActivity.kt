package com.crazyj36.watchfacetest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.fillMaxSize())
        }
        Toast.makeText(
            this@WatchFaceConfigActivity,
            resources.getString(R.string.watchFaceConfigurationActivityToastText),
            Toast.LENGTH_LONG
        ).show()
        MainScope().launch(Dispatchers.Main.immediate) {
            //delay(2000)
            val editorSession = EditorSession.createOnWatchEditorSession(
                this@WatchFaceConfigActivity
            )
            editorSession
                .openComplicationDataSourceChooser(1)
            finish()
        }
    }
}