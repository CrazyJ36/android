package com.crazyj36.watchfacetest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.core.graphics.withRotation
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.cos
import kotlin.math.sin

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
        setARGB(255, 15, 15, 15)
        textAlign = Paint.Align.CENTER
    }
    private val textPaint = Paint().apply{
        isAntiAlias = true
        setARGB(255, 230, 230, 230)
        textSize = 24F
        textAlign = Paint.Align.CENTER
    }
    private val HOUR_MARKS = arrayOf("3", "6", "9", "12")
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
    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
        Toast.makeText(context,
            "renderHighlightLayer() triggered, drawing black.",
            Toast.LENGTH_SHORT
        ).show()
        canvas.drawColor(Color.BLACK)
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
        if (renderParameters.drawMode != DrawMode.AMBIENT) {
            /*drawNumberStyleOuterElement(
                canvas,
                bounds,
                0.45f,
                0.00584f,
                Color.DKGRAY, null,
                0.00584f,
                0.03730f
            )*/
            val textBounds = Rect()
            for (i in 0 until 4) {
                val rotation = 0.5f * (i + 1).toFloat() * Math.PI
                val dx = sin(rotation).toFloat() * 0.45f * bounds.width().toFloat()
                val dy = -cos(rotation).toFloat() * 0.45f * bounds.width().toFloat()
                textPaint.getTextBounds(HOUR_MARKS[i], 0, HOUR_MARKS[i].length, textBounds)
                canvas.drawText(
                    HOUR_MARKS[i],
                    bounds.exactCenterX() + dx - textBounds.width() / 2.0f,
                    bounds.exactCenterY() + dy + textBounds.height() / 2.0f,
                    textPaint
                )
            }
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
            for ((_, complication) in complicationSlotsManager
                .complicationSlots) {
                complication.render(canvas, zonedDateTime, renderParameters)
            }

        }

        val hourHandBorder: Path = createClockHand(
            bounds,
            0.22028f,
            0.01500f,
            0f,
            6f,
            6f
        )
        val minuteHandBorder: Path = createClockHand(
            bounds,
            0.3783f,
            0.0100f,
            0f,
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