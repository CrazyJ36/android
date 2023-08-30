package com.crazyj36.watchfacetest

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Text
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class WatchFaceConfigActivity: ComponentActivity() {
    //private lateinit var scope : CoroutineScope
    //lateinit var editorSession: EditorSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WATCHFACETEST", "WatcchFaceConfigActivity opened")
        setContent {
            wearApp()
        }
       /* scope.launch(Dispatchers.Main.immediate) {
            editorSession =
                EditorSession.createOnWatchEditorSession(
                    this@WatchFaceConfigActivity
                )
            editorSession
                .openComplicationDataSourceChooser(1)
        }*/

    }
    @Composable
    fun wearApp() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement =
                Arrangement.Center
        ) {
            Text(text = "config activity")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //editorSession.close()
    }

}