package com.crazyj36.complicationtest

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.WatchFaceLayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class WatchFaceConfigActivity : ComponentActivity() {
    private lateinit var imageView: ImageView
    private lateinit var editorSession: EditorSession
    private lateinit var bitmap: Bitmap
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
    private val uiState: Flow<EditWatchFaceUiState> =
        flow { this.emitAll()
            editorSession = EditorSession.createOnWatchEditorSession(
                this@WatchFaceConfigActivity
            )
            val bitmap = editorSession.renderWatchFaceToBitmap(
                RenderParameters(
                    DrawMode.INTERACTIVE,
                    WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                    null
                ),
                editorSession.previewReferenceInstant,
                slotIdToComplicationData = null
            )
            imageView.setImageBitmap(bitmap)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.watch_face_config)
        imageView = findViewById(R.id.imageView)
        lifecycleScope.launch {
            uiState.collect {
                imageView.setImageBitmap(bitmap)
            }
        }

    }

    fun onClickComplication(view: View) {
        when {
            checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) == PackageManager.PERMISSION_GRANTED -> {
                MainScope().launch {
                    editorSession.openComplicationDataSourceChooser(0)
                }
            }
            shouldShowRequestPermissionRationale(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            )
            -> {
                Toast.makeText(
                    this@WatchFaceConfigActivity,
                    getString(R.string.grantPermissionText),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                requestPermissionLauncher.launch("com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA")
            }
        }
    }

    sealed class EditWatchFaceUiState {
        data class Success(val successBitmap: Bitmap): EditWatchFaceUiState()
        data class Loading(val message: String): EditWatchFaceUiState()
    }
    /*data class UserStylesAndPreview(
        val previewImage: Bitmap
    )*/
}