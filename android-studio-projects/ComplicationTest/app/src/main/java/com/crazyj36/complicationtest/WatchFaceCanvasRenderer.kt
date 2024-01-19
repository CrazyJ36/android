package com.crazyj36.complicationtest

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
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
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
        val complicationData = complication!!.complicationData
        val complicationWireData = complicationData.value.asWireComplicationData()

        val smallImageComplicationDataClass: Class<out ComplicationData> = complicationData.value::class.java
        val smallImageField = smallImageComplicationDataClass.getDeclaredField("smallImage")
        smallImageField.isAccessible = true
        val smallImageTypeField = smallImageField.type.declaredFields[2]
        smallImageTypeField.isAccessible = true
        val test = smallImageTypeField.get(SmallImage.PLACEHOLDER)
        Log.d(tag, test!!.toString())



        /*val smallImageTypeField = fieldsOfSmallImageField[2].type.declaredFields
        var count = -1
        smallImageTypeField.forEach {
            it.isAccessible = true
            count += 1
            Log.d(tag, "smallImageTypeField: " + it.get(smallImageTypeField))
        }*/


        /*var count = -1
        fieldsOfField.forEach {
            count += 1
            it.isAccessible = true
            Log.d(tag, "Field $count is $it")
        }*/


        /*val fields = smallImageComplicationDataClass.declaredFields
        var count = -1
        fields.forEach {
            try {
                it.isAccessible = true
                val value = it.get(complicationData.value)
                count += 1
                Log.d(tag, "Field: " + count + " = " + value!!.toString())
            } catch (illegalAccessException: IllegalAccessException) {
                Log.d(tag, "Field $it is private.")
            }
        }*/

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
            Log.d(tag, "ComplicationType is not SmallImage, not customizing.")
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