package com.crazyj36.watchfacerevokepermissiontest

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
import kotlinx.coroutines.MainScope
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
    private val uiState: StateFlow<EditWatchFaceUiState> =
        flow<EditWatchFaceUiState> {
            editorSession = EditorSession.createOnWatchEditorSession(
                this@WatchFaceConfigActivity
            )
            emitAll(
                combine(
                    editorSession.userStyle,
                    editorSession.complicationsPreviewData
                ) { userStyle, complicationsPreviewData ->
                    yield()
                    EditWatchFaceUiState.Success(
                        createWatchFacePreview(userStyle, complicationsPreviewData)
                    )
                }
            )
        }
            .stateIn(
                MainScope(),
                SharingStarted.Eagerly,
                EditWatchFaceUiState.Loading("Initializing")
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.watch_face_config)
        imageView = findViewById(R.id.imageView)
        lifecycleScope.launch {
            uiState.collect { uiState: EditWatchFaceUiState ->
                when (uiState) {
                    is EditWatchFaceUiState.Loading -> {

                    }
                    is EditWatchFaceUiState.Success -> {
                        updateWatchFaceEditorPreview(uiState.userStylesAndPreview)
                    }
                    is EditWatchFaceUiState.Error -> {

                    }
                }
            }
        }

    }
    private fun updateWatchFaceEditorPreview(
        userStylesAndPreview: UserStylesAndPreview
    ) {
        imageView.setImageBitmap(userStylesAndPreview.previewImage)
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

            }

            else -> {
                requestPermissionLauncher.launch("com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA")
            }
        }
    }

    private fun createWatchFacePreview(
        userStyle: UserStyle,
        complicationsPreviewData: Map<Int, ComplicationData>
    ): UserStylesAndPreview {
        val bitmap = editorSession.renderWatchFaceToBitmap(
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
            editorSession.previewReferenceInstant,
            complicationsPreviewData
        )
        return UserStylesAndPreview(
            previewImage = bitmap
        )

    }
    sealed class EditWatchFaceUiState {
        data class Success(val userStylesAndPreview: UserStylesAndPreview): EditWatchFaceUiState()
        data class Loading(val message: String): EditWatchFaceUiState()
        data class Error(val exception: Throwable): EditWatchFaceUiState()
    }
    data class UserStylesAndPreview(
        val previewImage: Bitmap
    )
}