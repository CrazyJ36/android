package com.crazyj36.complicationtest

import android.animation.StateListAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.style.CurrentUserStyleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class WatchFaceCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    var complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int
) : Renderer.CanvasRenderer2<WatchFaceCanvasRenderer.MySharedAssets>(
    surfaceHolder = surfaceHolder,
    currentUserStyleRepository = currentUserStyleRepository,
    watchState = watchState,
    canvasType = canvasType,
    interactiveDrawModeUpdateDelayMillis = 1000L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    private val tag = "COMPLICATION_TEST"
    private var colorMatrix = floatArrayOf(
        0.4f, 0.4f, 0.4f, 0f, 0f,
        0.4f, 0.4f, 0.4f, 0f, 0f,
        0.4f, 0.4f, 0.4f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
        renderer(canvas, zonedDateTime, renderParameters)
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
        renderer(canvas, zonedDateTime, renderParameters)
    }

    @SuppressLint("RestrictedApi")
    private fun renderer(
        canvas: Canvas,
        zonedDateTime: ZonedDateTime,
        renderParameters: RenderParameters
    ) {
        val complication = complicationSlotsManager.complicationSlots[0]
        val complicationWireData = complication!!.complicationData.value.asWireComplicationData()
        //Log.d(tag, complication!!.complicationData.value.toString())

        MainScope().launch {
            val collection: MutableCollection<ComplicationData> = mutableListOf()
            complicationSlotsManager.complicationSlots[0]!!.complicationData.toCollection(collection)
            collection.forEach {
                Log.d(tag, "NUANCE: $it")
            }
        }


        if (complication.complicationData.value.type == ComplicationType.SMALL_IMAGE &&
            complicationWireData.hasSmallImage()) {
            if (complicationWireData.smallImageStyle == android.support.wearable.complications.ComplicationData.Companion.IMAGE_STYLE_ICON) {
                val drawable = complicationWireData.smallImage!!.loadDrawable(context)
                drawable!!.colorFilter = ColorMatrixColorFilter(colorMatrix)
                drawable.setTintMode(PorterDuff.Mode.MULTIPLY)
                drawable.setTint(Color.WHITE)
                drawable.bounds =
                    Rect(
                        (canvas.width * 0.40).toInt(),
                        (canvas.height * 0.40).toInt(),
                        (canvas.width * 0.60).toInt(),
                        (canvas.height * 0.60).toInt()
                    )
                drawable.draw(canvas)
            } else if (complicationWireData.smallImageStyle == android.support.wearable.complications.ComplicationData.IMAGE_STYLE_PHOTO) {
                complication.render(canvas, zonedDateTime, renderParameters)
            }
        } else {
            complication.render(
                canvas,
                zonedDateTime,
                renderParameters
            )
        }

        if (renderParameters.drawMode == DrawMode.AMBIENT) {
            val paint = Paint()
            paint.setARGB(255, 255, 255, 255)
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 14f
            canvas.drawText(
                "Ambient",
                canvas.width / 2f,
                canvas.height - (canvas.height / 7).toFloat(),
                paint
            )
        }
    }

    class MySharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    override suspend fun createSharedAssets(): MySharedAssets {
        return MySharedAssets()
    }
}