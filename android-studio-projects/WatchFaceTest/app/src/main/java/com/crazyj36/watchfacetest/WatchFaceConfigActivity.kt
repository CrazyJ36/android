package com.crazyj36.watchfacetest

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Text
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WatchFaceConfigActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED) {
            setContent {
                Box(modifier = Modifier.fillMaxSize())
            }
            Toast.makeText(this@WatchFaceConfigActivity,
                resources.getString(R.string.chooseWeatherToastText),
                Toast.LENGTH_LONG).show()
            MainScope().launch(Dispatchers.Main.immediate) {
                val editorSession = EditorSession.createOnWatchEditorSession(
                    this@WatchFaceConfigActivity
                )
                editorSession
                    .openComplicationDataSourceChooser(1)
                finish()
            }
        } else {
            setContent {
                PermissionNotYetGrantedLayout()
            }
        }
    }
    @Composable
    private fun PermissionNotYetGrantedLayout() {
        LazyColumn {
            item {
                Text(text = resources.getString(R.string.permissionNotYetGrantedLayoutText))
            }
            item {
                CompactChip(onClick = {
                    requestPermissions(arrayOf(
                        "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"),
                        0
                    )
                })
            }
        }
    }
}