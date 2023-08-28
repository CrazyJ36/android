package com.crazyj36.watchfacetest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder
import androidx.core.graphics.withRotation
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.Duration
import java.time.ZonedDateTime

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
    private var count = 0
    private val textBackgroundPaint = Paint().apply {
        isAntiAlias = true
        setARGB(225, 30, 30, 30)
        textAlign = Paint.Align.CENTER
    }
    private val textPaint = Paint().apply{
        isAntiAlias = true
        setARGB(255, 230, 230, 230)
        textSize = 24F
        textAlign = Paint.Align.CENTER
    }
    private val hourHandsPaint = Paint().apply {
        isAntiAlias = true
        setARGB(255, 200, 200, 200)
        strokeWidth = 6F
    }
    private val minutesHandPaint = Paint().apply {
        isAntiAlias = true
        setARGB(255, 230, 230, 230)
        strokeWidth = 3F
    }
    private val secondsPerHourHandRotation = Duration.ofHours(12)
        .seconds
    private val secondsPerMinuteHandRotation = Duration.ofHours(1)
        .seconds
    private val index = BitmapFactory.decodeResource(context.resources, R.drawable.index_modern2)

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
        if (count > 99) count = 0
        val width = bounds.width()
        val height = bounds.height()
        canvas.drawColor(Color.BLACK)
        if (renderParameters.drawMode == DrawMode.AMBIENT) {
            canvas.drawText(
                context.resources.getString(R.string.inAmbientText),
                (width / 2).toFloat(),
                (height - (height / 4)).toFloat(),
                textPaint
            )
        } else {
            canvas.drawRoundRect(
                (width / 5).toFloat(),
                (height - (height / 2.5)).toFloat(),
                (width - (width / 5)).toFloat(),
                (height - (height / 5.5)).toFloat(),
                14F,
                14F,
                textBackgroundPaint
            )
            count++
            canvas.drawText(
                count.toString(),
                (width / 2).toFloat(),
                (height - (height / 3)).toFloat(),
                textPaint
            )
        }
        canvas.drawBitmap(
            Bitmap.createScaledBitmap(index, width, height, false),
            0F,
            0F,
            null
        )
        val hourHandBorder: Path = createClockHand(
            bounds,
            0.21028f,
            0.02336f,
            0.01f, // 0.01869f + 0.03738f / 2.0f
            8f, // 1.5f
            8f
        )
        val minuteHandBorder: Path = createClockHand(
            bounds,
            0.3783f,
            0.0120f, // 0.0163f
            0.01f, // 0.01869f + 0.03738f / 2.0f
            4f,
            4f
        )
        canvas.withRotation(
            zonedDateTime.toLocalTime()
                .toSecondOfDay()
                .rem(secondsPerHourHandRotation) * 360.0F /
                    secondsPerHourHandRotation,
            bounds.exactCenterX(),
            bounds.exactCenterY()
        ) {
            drawPath(hourHandBorder, hourHandsPaint)
        }
        canvas.withRotation(
            zonedDateTime.toLocalTime()
                .toSecondOfDay().rem(secondsPerMinuteHandRotation) * 360.0F /
                    secondsPerMinuteHandRotation,
            bounds.exactCenterX(),
            bounds.exactCenterY()
        ) {
            drawPath(minuteHandBorder, minutesHandPaint)
        }
        Log.d("WATCHFACETEST", complicationSlotsManager.complicationSlots.size.toString())
        for ((_, complication) in complicationSlotsManager
            .complicationSlots) {
            complication.render(canvas, zonedDateTime, renderParameters)
        }
    }

    private fun createClockHand(
        bounds: Rect,
        length: Float,
        thickness: Float,
        gapBetweenHandAndCenter: Float,
        roundedCornerXRadius: Float,
        roundedCornerYRadius: Float
    ): Path {
        val width = bounds.width()
        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()
        val left = centerX - thickness / 2 * width
        val top = centerY - (gapBetweenHandAndCenter + length) * width
        val right = centerX + thickness / 2 * width
        val bottom = centerY - gapBetweenHandAndCenter * width
        val path = Path()

        if (roundedCornerXRadius != 0.0f || roundedCornerYRadius != 0.0f) {
            path.addRoundRect(
                left,
                top,
                right,
                bottom,
                roundedCornerXRadius,
                roundedCornerYRadius,
                Path.Direction.CW
            )
        } else {
            path.addRect(
                left,
                top,
                right,
                bottom,
                Path.Direction.CW
            )
        }
        return path
    }
    class MySharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    override suspend fun createSharedAssets(): MySharedAssets {
        return MySharedAssets()
    }
}