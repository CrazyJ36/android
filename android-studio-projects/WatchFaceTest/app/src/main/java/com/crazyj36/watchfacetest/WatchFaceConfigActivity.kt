package com.crazyj36.watchfacetest

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.contentColorFor
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.WatchFaceLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.Instant


class WatchFaceConfigActivity: ComponentActivity() {
    private lateinit var editorSession: EditorSession
    private lateinit var watchFacePreview: ImageBitmap
    private val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED) {

            /*Toast.makeText(this@WatchFaceConfigActivity,
                resources.getString(R.string.chooseWeatherToastText),
                Toast.LENGTH_LONG).show()*/

            scope.launch(Dispatchers.Main.immediate) {
                /*EditorSession.createOnWatchEditorSession(
                    this@WatchFaceConfigActivity
                ).openComplicationDataSourceChooser(1)
                finish()*/
                editorSession = EditorSession
                    .createOnWatchEditorSession(
                        this@WatchFaceConfigActivity
                    )
                watchFacePreview =
                    editorSession.renderWatchFaceToBitmap(
                        RenderParameters(
                            DrawMode.INTERACTIVE,
                            WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                            RenderParameters.HighlightLayer(
                                RenderParameters.HighlightedElement
                                    .AllComplicationSlots,
                                android.graphics.Color.GREEN,
                                android.graphics.Color.argb(
                                    120, 0, 0,0)
                            )
                        ),
                        Instant.now(),
                        editorSession.complicationsPreviewData.value
                    ).asImageBitmap()
                setContent {
                    val currentPreview = remember {watchFacePreview}
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            currentPreview,
                            "preview of edited watch face"
                        )
                        OutlinedButton(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 4.dp, top = 4.dp)
                                .border(1.dp, Color.Red),
                            shape = CircleShape,
                            onClick = { onClickLeftBottomComplication() }) {
                            Text(text = "TEST")
                        }
                    }
                }
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
                        Text(text = resources.getString(
                            R.string.permissionNotYetGrantedLayoutText),
                            textAlign = TextAlign.Center)
                    }
                    item {
                        CompactChip(onClick = {
                            requestPermissions(arrayOf(
                                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"),
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
    private fun onClickLeftBottomComplication() {
        if (editorSession != null) {
            scope.launch(Dispatchers.Main.immediate) {
                editorSession
                    .openComplicationDataSourceChooser(1)
            }
            watchFacePreview =
                editorSession.renderWatchFaceToBitmap(
                    RenderParameters(
                        DrawMode.INTERACTIVE,
                        WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                        RenderParameters.HighlightLayer(
                            RenderParameters.HighlightedElement
                                .AllComplicationSlots,
                            android.graphics.Color.GREEN,
                            android.graphics.Color.argb(
                                120, 0, 0,0)
                        )
                    ),
                    Instant.now(),
                    editorSession.complicationsPreviewData.value
                ).asImageBitmap()
        }
    }
}