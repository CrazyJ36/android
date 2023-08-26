package com.crazyj36.watchfacetest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

class CustomCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    private val complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int, clearWithBackgroundTintBeforeRenderingHighlightLayer: Boolean
) : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
        surfaceHolder = surfaceHolder,
        currentUserStyleRepository = currentUserStyleRepository,
        watchState = watchState,
        canvasType = canvasType,
        interactiveDrawModeUpdateDelayMillis = 16L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    override suspend fun createSharedAssets(): SharedAssets {
        TODO("Not yet implemented")
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
        val width = bounds.width()
        val height = bounds.height()
        val radius = Math.min(width, height)
            .toFloat() / 2f
        val redPaint = Paint().apply {
            setARGB(225, 225, 0, 0)
        }
        canvas.drawCircle(
            (bounds.width() / 2).toFloat(),
            (bounds.height() / 2).toFloat(),
            radius,
            redPaint
        )
    }
}