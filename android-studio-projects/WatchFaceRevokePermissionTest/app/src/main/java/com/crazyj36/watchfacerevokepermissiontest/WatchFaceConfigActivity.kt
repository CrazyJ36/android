package com.crazyj36.watchfacerevokepermissiontest

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.WatchFaceLayer
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WatchFaceConfigActivity : ComponentActivity() {

    private lateinit var editorSession: EditorSession
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
        setContentView(R.layout.watch_face_config)
        lifecycleScope.launch {
            async {
                editorSession = EditorSession
                    .createOnWatchEditorSession(
                        this@WatchFaceConfigActivity
                    )
            }.await()
            imageView = findViewById(R.id.imageView)
            getPreview()
        }
    }

    fun onClickComplication(view: View) {
        when {
            checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED -> {

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
        lifecycleScope.launch {
            editorSession.openComplicationDataSourceChooser(0)
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
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.argb(
                            100, 0, 0, 0
                        )
                    )
                ),
                EditorSession.DEFAULT_PREVIEW_INSTANT,
                editorSession.complicationsPreviewData.value
            )
        )
    }
}
