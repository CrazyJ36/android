package com.crazyj36.watchfacetest

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Text
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.TapEvent
import androidx.wear.watchface.TapType
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.WatchFaceLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.Instant


class WatchFaceConfigActivity: ComponentActivity(), WatchFace.TapListener {
    private lateinit var editorSession: EditorSession
    override fun onTapEvent(tapType: Int, tapEvent: TapEvent, complicationSlot: ComplicationSlot?) {
        if (tapType == TapType.UP) {
            Toast.makeText(
                this,
                "tapped",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED) {
            /*Toast.makeText(this@WatchFaceConfigActivity,
                resources.getString(R.string.chooseWeatherToastText),
                Toast.LENGTH_LONG).show()*/

            MainScope().launch(Dispatchers.Main.immediate) {
                editorSession = EditorSession.createOnWatchEditorSession(
                    this@WatchFaceConfigActivity
                )
                //editorSession
                //    .openComplicationDataSourceChooser(1)
                //finish()
                val bitmap = editorSession.renderWatchFaceToBitmap(
                    RenderParameters(
                        DrawMode.INTERACTIVE,
                        WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                        RenderParameters.HighlightLayer(
                            RenderParameters.HighlightedElement.AllComplicationSlots,
                            Color.GREEN,
                            Color.argb(128, 0, 0, 0)
                        )
                    ),
                    Instant.now(),
                    editorSession.complicationsPreviewData.value
                ).asImageBitmap()
                setContent {
                    Image(bitmap, "Preview of edited complications.")
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
}