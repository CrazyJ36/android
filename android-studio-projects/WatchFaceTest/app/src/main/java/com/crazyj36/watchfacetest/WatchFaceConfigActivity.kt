package com.crazyj36.watchfacetest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Text
import androidx.wear.watchface.editor.EditorSession
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
                Box(modifier = Modifier.fillMaxSize())
            }
            Toast.makeText(this@WatchFaceConfigActivity,
                resources.getString(R.string.chooseWeatherText),
                Toast.LENGTH_LONG).show()
            MainScope().launch { delay(2000) }
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = resources.getString(
                            R.string.stillNeedPermissionText
                        ),
                        textAlign = TextAlign.Center)
                    }
                    CompactChip(onClick = {
                        startActivity(
                            Intent(applicationContext,
                            GetComplicationPermission::class.java)
                            .setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        ) },
                        label = {
                            Text(text = resources.getString(R.string.allowText))
                        }
                    )
                }
            }
        }
    }