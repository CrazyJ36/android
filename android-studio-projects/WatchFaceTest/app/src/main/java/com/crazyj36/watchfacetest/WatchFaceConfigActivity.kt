package com.crazyj36.watchfacetest

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.material.Text
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.WatchFaceLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.Instant


class WatchFaceConfigActivity : ComponentActivity() {
    private lateinit var editorSession: EditorSession
    private lateinit var imageView: ImageView
    private val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setContentView(R.layout.watch_face_preview)
            scope.launch(Dispatchers.Main.immediate) {
                editorSession = EditorSession
                    .createOnWatchEditorSession(
                        this@WatchFaceConfigActivity
                    )
                imageView = findViewById(R.id.imageView)
                getPreview()
            }
        } else {
            setContent {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Text(
                            text = resources.getString(
                                R.string.permissionNotYetGrantedLayoutText
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        CompactChip(onClick = {
                            requestPermissions(
                                arrayOf(
                                    "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
                                ),
                                0
                            )
                            finish()
                        },
                            label = {
                                Text(resources.getString(R.string.allowButtonText))
                            }
                        )
                    }
                }
            }
        }
    }

    fun onClickLeftTopComplication(view: View) {
        scope.launch(Dispatchers.Main.immediate) {
            editorSession.openComplicationDataSourceChooser(0)
            getPreview()
        }

    }
    private fun getPreview() {
        imageView.setImageBitmap(editorSession.renderWatchFaceToBitmap(
            RenderParameters(
                DrawMode.INTERACTIVE,
                WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                RenderParameters.HighlightLayer(
                    RenderParameters.HighlightedElement
                        .AllComplicationSlots,
                    android.graphics.Color.GREEN,
                    android.graphics.Color.argb(
                        120, 0, 0, 0
                    )
                )
            ),
            Instant.now(),
            editorSession.complicationsPreviewData.value
        )
        )
    }
}
