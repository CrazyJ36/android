package com.crazyj36.watchfacetest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Text
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.WatchFaceLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant

class WatchFaceConfigActivity : ComponentActivity() {
    private lateinit var editorSession: EditorSession
    private lateinit var imageView: ImageView
    private lateinit var loadingTextView: TextView
    private val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("WATCHFACETEST", "onCreate() config")
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
                loadingTextView = findViewById(R.id.loadingTextView)
                delay(1000)
                loadingTextView.visibility = View.GONE
                findViewById<Button>(R.id.leftTopButton).visibility = View.VISIBLE
                findViewById<Button>(R.id.leftBottomButton).visibility = View.VISIBLE
                findViewById<Button>(R.id.rightTopButton).visibility = View.VISIBLE
                findViewById<Button>(R.id.rightBottomButton).visibility = View.VISIBLE
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
                            modifier = Modifier.fillMaxWidth(),
                            text = resources.getString(
                                R.string.permissionNotYetGrantedLayoutText
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        CompactChip(
                            onClick = {
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

    fun onClickLeftBottomComplication(view: View) {
        scope.launch(Dispatchers.Main.immediate) {
            editorSession.openComplicationDataSourceChooser(1)
            getPreview()
        }
    }

    fun onClickRightTopComplication(view: View) {
        scope.launch(Dispatchers.Main.immediate) {
            editorSession.openComplicationDataSourceChooser(2)
            getPreview()
        }
    }

    fun onClickRightBottomComplication(view: View) {
        scope.launch(Dispatchers.Main.immediate) {
            editorSession.openComplicationDataSourceChooser(3)
            getPreview()
        }
    }

    private fun getPreview() {
        imageView.setImageBitmap(
            editorSession.renderWatchFaceToBitmap(
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
