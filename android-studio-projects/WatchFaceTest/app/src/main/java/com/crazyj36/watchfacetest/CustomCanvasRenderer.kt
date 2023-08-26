package com.crazyj36.watchfacetest

import android.content.Context
import android.graphics.Canvas
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
import kotlin.math.cos
import kotlin.math.min
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
    private val darkPaint = Paint().apply {
        isAntiAlias = true
        setARGB(225, 50, 50, 50)
    }
    private val lightPaint = Paint().apply{
        isAntiAlias = true
        setARGB(255, 230, 230, 230)
        textSize = 24F
    }
    private val hourHandsPaint = Paint().apply {
        isAntiAlias = true
        setARGB(255, 230, 230, 230)
        strokeWidth = 6F
    }
    private val minutesHandPaint = Paint().apply {
        isAntiAlias = true
        setARGB(255, 230, 230, 230)
        strokeWidth = 3F
    }
    private val ticksPaint = Paint().apply {
        isAntiAlias = true
        setARGB(255, 100, 100, 100)
        textSize = 18F
    }
    private val ticksHours = arrayOf("3", "6", "9", "12")
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
        val radius = min(width, height).toFloat()

        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            radius / 10,
            darkPaint
        )
        val hourRotation: Float = zonedDateTime.toLocalTime()
            .toSecondOfDay().rem(secondsPerHourHandRotation) * 360.0F /
                secondsPerHourHandRotation
        val minuteRotation: Float = zonedDateTime.toLocalTime()
            .toSecondOfDay().rem(secondsPerMinuteHandRotation) * 360.0F /
                secondsPerMinuteHandRotation
        val hourHandBorder: Path = createClockHand(
            bounds,
            0.21028f,
            0.02336f,
            0.01869f + 0.03738f / 2.0f,
            1.5F,
            1.5F
        )
        val minuteHandBorder: Path = createClockHand(
            bounds,
            0.3783F,
            0.0163f,
            0.01869f + 0.03738f / 2.0f,
            1.5F,
            1.5F
        )
        canvas.withRotation(
            hourRotation,
            bounds.exactCenterX(),
            bounds.exactCenterY()
        ) {
            drawPath(hourHandBorder, hourHandsPaint)
        }
        canvas.withRotation(
            minuteRotation,
            bounds.exactCenterX(),
            bounds.exactCenterY()
        ) {
            drawPath(minuteHandBorder, minutesHandPaint)
        }

        if (renderParameters.drawMode == DrawMode.INTERACTIVE) {
            count++
            canvas.drawText(
                count.toString(),
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                lightPaint
            )
        } else if (renderParameters.drawMode == DrawMode.AMBIENT) {
            canvas.drawText(
                context.resources.getString(R.string.inAmbientText),
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                lightPaint
            )
        }
        drawNumberStyleOuterElement(
            canvas,
            bounds,
            0.45F,
            0.00584F,
            ticksPaint.color,
            0.00584F,
            0.03738F
        )
    }
    private fun drawNumberStyleOuterElement(
        canvas: Canvas,
        bounds: Rect,
        numberRadiusFraction: Float,
        outerCircleStokeWidthFraction: Float,
        outerElementColor: Int,
        numberStyleOuterCircleRadiusFraction: Float,
        gapBetweenOuterCircleAndBorderFraction: Float
    ) {
        // Draws text hour indicators (12, 3, 6, and 9).
        val textBounds = Rect()
        ticksPaint.color = outerElementColor
        for (i in 0 until 4) {
            val rotation = 0.5f * (i + 1).toFloat() * Math.PI
            val dx = sin(rotation).toFloat() * numberRadiusFraction * bounds.width().toFloat()
            val dy = -cos(rotation).toFloat() * numberRadiusFraction * bounds.width().toFloat()
            ticksPaint.getTextBounds(ticksHours[i], 0, ticksHours[i].length, textBounds)
            canvas.drawText(
                ticksHours[i],
                bounds.exactCenterX() + dx - textBounds.width() / 2.0f,
                bounds.exactCenterY() + dy + textBounds.height() / 2.0f,
                ticksPaint
            )
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