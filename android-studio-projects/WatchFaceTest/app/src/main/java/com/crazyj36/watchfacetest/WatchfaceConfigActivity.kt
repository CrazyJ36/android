package com.crazyj36.watchfacetest

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    private lateinit var scope : CoroutineScope
    private lateinit var editorSession: EditorSession

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        scope.launch(Dispatchers.Main.immediate) {
            editorSession
                .openComplicationDataSourceChooser(1)

        }
    }
}