package com.crazyj36.watchfacetest

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Text
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA")
            == PackageManager.PERMISSION_GRANTED) {
            setContent {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = resources.getString(
                            R.string.chooseWeatherText
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
            lifecycleScope.launch(Dispatchers.Main.immediate) {
                delay(2000)
                val editorSession = EditorSession.createOnWatchEditorSession(
                    this@WatchFaceConfigActivity
                )
                editorSession
                    .openComplicationDataSourceChooser(1)
                finish()
            }
        } else {
            setContent {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = resources.getString(
                            R.string.stillNeedPermissionText
                        ),
                        textAlign = TextAlign.Center
                    )
                    CompactChip(onClick = {
                        requestPermissions(arrayOf(
                            "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
                        ), 1)
                    },
                        label = {
                            Text(text = resources.getString(R.string.allowText))
                        })
                }
            }
        }
    }
}