package com.crazyj36.complicationtest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.util.Log
import android.view.SurfaceHolder
import androidx.core.content.res.ResourcesCompat
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.ShortTextComplicationData
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
    interactiveDrawModeUpdateDelayMillis = 16L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    private val tag = "COMPLICATION_TEST"
    private var complication: ComplicationSlot? = null
    private var shortTextComplicationDataBuilder: ShortTextComplicationData.Builder? = null
    private var shortTextComplicationData: ShortTextComplicationData? = null
    private val paint = Paint()
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
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
        canvas.drawColor(Color.BLACK)
        complication = complicationSlotsManager.complicationSlots[0]
        val complicationType = complication!!.complicationData.value.type
        val data = complication!!.complicationData.value.toString()
        Log.d(tag, data)
        when (complicationType) {
            ComplicationType.SHORT_TEXT -> {
                try {
                    val imagePkg = data.split("pkg=")[1].split(" id=")[0]
                    val imageId = data.split("id=")[1].split(")")[0]
                    shortTextComplicationData =
                        complication!!.complicationData.value as ShortTextComplicationData
                    shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                        shortTextComplicationData!!.text,
                        shortTextComplicationData!!.contentDescription!!
                    ).setMonochromaticImage(
                        MonochromaticImage.Builder(
                            Icon.createWithResource(imagePkg, Integer.decode(imageId))
                        ).build()
                    ).setTapAction(shortTextComplicationData!!.tapAction)
                    complication!!.renderer.loadData(
                        shortTextComplicationDataBuilder!!.build(),
                        false
                    )
                    complication!!.render(canvas, zonedDateTime, renderParameters)
                } catch (exception: Exception) {
                    Log.d(tag, "complicationData inaccurate.\n" + exception.localizedMessage)
                }
            }

            ComplicationType.SMALL_IMAGE -> {
                try {
                    val imageType = data.split("type=")[1].split(",")[0]
                    if (imageType == "ICON") {
                        val imagePkg = data.split("pkg=")[1].split(" id=")[0]
                        val imageId = data.split("id=")[1].split(")")[0]
                        val drawable = ResourcesCompat.getDrawable(
                            context.packageManager.getResourcesForApplication(imagePkg),
                            Integer.decode(imageId),
                            null
                        )
                        drawable!!.colorFilter = ColorMatrixColorFilter(colorMatrix)
                        drawable.setTintMode(PorterDuff.Mode.MULTIPLY)
                        drawable.setTint(Color.WHITE)
                        drawable.bounds = Rect(
                            (canvas.width * 0.40).toInt(),
                            (canvas.height * 0.40).toInt(),
                            (canvas.width * 0.60).toInt(),
                            (canvas.height * 0.60).toInt()
                        )
                        drawable.draw(canvas)
                    } else if (imageType == "PHOTO") {
                        complication!!.render(canvas, zonedDateTime, renderParameters)
                    }
                } catch (exception: Exception) {
                    Log.d(tag, "complicationData inaccurate:\n" + exception.localizedMessage)
                }
            }

            ComplicationType.EMPTY -> {
                // canvas.drawColor(Color.BLACK) at first in render() takes care of this.
            }

            ComplicationType.NO_DATA -> {
                complication!!.render(canvas, zonedDateTime, renderParameters)
            }

            ComplicationType.NOT_CONFIGURED -> {
                complication!!.render(canvas, zonedDateTime, renderParameters)
            }

            else -> {
                Log.d(tag, "Unsupported complication type")
            }
        }

        if (renderParameters.drawMode == DrawMode.AMBIENT) {
            Log.d(tag, "Ambient")
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