package com.crazyj36.watchfacerevokepermissiontest

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.WatchFaceLayer
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WatchFaceConfigActivity() : ComponentActivity() {
    private lateinit var imageView: ImageView
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (!it) {
            Toast.makeText(
                this@WatchFaceConfigActivity,
                getString(R.string.grantPermissionText),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainScope().launch {
            setContentView(R.layout.watch_face_config)
            imageView = findViewById(R.id.imageView)
            getPreview()
        }
    }

    fun onClickComplication(view: View) {
        when {
            checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED -> {
                MainScope().launch {
                    MyWatchFaceService.editorSession.openComplicationDataSourceChooser(0)
                    getPreview()
                }
            }

            shouldShowRequestPermissionRationale(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            )
            -> {

            }

            else -> {
                requestPermissionLauncher.launch("com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA")
            }
        }
    }

    private fun getPreview() {
        imageView.setImageBitmap(
            MyWatchFaceService.editorSession.renderWatchFaceToBitmap(
                RenderParameters(
                    DrawMode.INTERACTIVE,
                    WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                    RenderParameters.HighlightLayer(
                        RenderParameters.HighlightedElement
                            .AllComplicationSlots,
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.argb(
                            100, 0, 0, 0
                        )
                    )
                ),
                EditorSession.DEFAULT_PREVIEW_INSTANT,
                MyWatchFaceService.editorSession.complicationsPreviewData.value
            )
        )
    }
}