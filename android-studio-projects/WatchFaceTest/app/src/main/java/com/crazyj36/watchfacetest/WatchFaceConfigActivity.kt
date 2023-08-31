package com.crazyj36.watchfacetest

import android.content.Context
import android.content.pm.PackageManager
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
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    private lateinit var editorSession: EditorSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
            arrayOf(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"),
            0
            )
            mainConfig()
        } else {
            mainConfig()
        }
    }
    private fun mainConfig() {
        setContent {
            Box(modifier = Modifier.fillMaxSize())
        }
        Toast.makeText(
            this@WatchFaceConfigActivity,
            resources.getString(R.string.watchFaceConfigurationActivityToastText),
            Toast.LENGTH_LONG
        ).show()
        MainScope().launch(Dispatchers.Main.immediate) {
            editorSession = EditorSession.createOnWatchEditorSession(
                this@WatchFaceConfigActivity
            )
            editorSession
                .openComplicationDataSourceChooser(1)
            finish()
        }
    }
}