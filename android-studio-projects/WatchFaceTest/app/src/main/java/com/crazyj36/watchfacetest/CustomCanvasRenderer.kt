package com.crazyj36.watchfacetest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.min

class CustomCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    private val complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int
) : Renderer.CanvasRenderer2<CustomCanvasRenderer.MySharedAssets>(
    surfaceHolder = surfaceHolder,
    currentUserStyleRepository = currentUserStyleRepository,
    watchState = watchState,
    canvasType = canvasType,
    interactiveDrawModeUpdateDelayMillis = 1000L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    private val myWatchState = watchState
    private var count = 0
    private val darkPaint = Paint().apply {
        setARGB(225, 50, 50, 50)
    }
    private val lightPaint = Paint().apply{
        setARGB(255, 230, 230, 230)
        textSize = 24F
    }
    private val timePaint = Paint().apply {
        setARGB(255, 230, 230, 230)
        textSize = 48F
    }
    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
    }
    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
        Log.d("WATCHFACESERVICETEST", "rendering")
        if (count > 99) count = 0
        val width =  bounds.width()
        val height = bounds.height()
        val radius = min(width, height).toFloat()
        canvas.drawText(zonedDateTime.format(
                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            ),
            (width / 4).toFloat(),
            (height / 7).toFloat(),
            timePaint
        )
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            radius / 10,
            darkPaint
        )
        canvas.drawText(
            count.toString(),
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            lightPaint
        )
        if (!myWatchState.isAmbient.value!!) count++

    }

    class MySharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    override suspend fun createSharedAssets(): MySharedAssets {
        return MySharedAssets()
    }
}