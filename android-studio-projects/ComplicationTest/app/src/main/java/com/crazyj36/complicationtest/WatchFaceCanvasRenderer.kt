package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.wearable.complications.ComplicationData
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.style.CurrentUserStyleRepository
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

    private val paint = Paint()
    private var complication: ComplicationSlot? = null
    private var complicationWireData: ComplicationData?  = null
    private var drawable: Drawable? = null
    private var colorMatrix: FloatArray? = null

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
        complication = complicationSlotsManager.complicationSlots[0]
        complicationWireData = complication!!.complicationData.value.asWireComplicationData()

        if (complication!!.complicationData.value.type == ComplicationType.SMALL_IMAGE &&
            complicationWireData!!.hasSmallImage()) {

            if (complicationWireData!!.smallImageStyle == ComplicationData.IMAGE_STYLE_ICON) {
                colorMatrix = floatArrayOf(
                    1f, 0f, 0f, 0f, 50f,
                    1f, 0f, 0f, 0f, 50f,
                    1f, 0f, 0f, 0f, 50f,
                    0f, 0f, 0f, 1f, 0f
                )
                drawable = complicationWireData!!.smallImage!!.loadDrawable(context)
                drawable!!.colorFilter = ColorMatrixColorFilter(colorMatrix!!)
                drawable!!.setTintMode(PorterDuff.Mode.MULTIPLY)
                drawable!!.setTint(Color.WHITE)
                drawable!!.bounds =
                    Rect(
                        (canvas.width * 0.40).toInt(),
                        (canvas.height * 0.40).toInt(),
                        (canvas.width * 0.60).toInt(),
                        (canvas.height * 0.60).toInt()
                    )
                drawable!!.draw(canvas)
            } else if (complicationWireData!!.smallImageStyle == ComplicationData.IMAGE_STYLE_PHOTO) {
                complication!!.render(canvas, zonedDateTime, renderParameters)
            }
        } else {
            complication!!.render(
                canvas,
                zonedDateTime,
                renderParameters
            )
        }

        if (renderParameters.drawMode == DrawMode.AMBIENT) {
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