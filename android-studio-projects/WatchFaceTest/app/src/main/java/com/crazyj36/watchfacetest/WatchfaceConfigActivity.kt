package com.crazyj36.watchfacetest

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    private lateinit var scope : CoroutineScope
    lateinit var editorSession: EditorSession
    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.d("WATCHFACETEST", "WatcchFaceConfigActivity opened")

        scope.launch(Dispatchers.Main.immediate) {
            editorSession =
                EditorSession.createOnWatchEditorSession(
                    this@WatchFaceConfigActivity
                )
            editorSession
                .openComplicationDataSourceChooser(1)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        editorSession.close()
    }

}