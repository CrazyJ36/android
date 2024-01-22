package com.crazyj36.complicationtest

import android.app.PendingIntent
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
import androidx.wear.watchface.complications.data.ComplicationText
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
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
    private var complication: ComplicationSlot? = null
    private var shortTextComplicationDataBuilder: ShortTextComplicationData.Builder? = null
    private var shortTextComplicationData: ShortTextComplicationData? = null
    private var dataSourceText: ComplicationText? = null
    private var dataSourceContentDescription: ComplicationText? = null
    private var dataSourceTitle: ComplicationText? = null
    private var dataSourceSmallImage: SmallImage? = null
    private var dataSourceMonochromaticImage: MonochromaticImage? = null
    private var dataSourceAmbientImage: Icon? = null
    private var dataSourceTapAction: PendingIntent? = null
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
        dataSourceText = null
        dataSourceContentDescription = null
        dataSourceTitle = null
        dataSourceSmallImage = null
        dataSourceMonochromaticImage = null
        dataSourceAmbientImage = null
        dataSourceTapAction = null

        canvas.drawColor(Color.BLACK)
        complication = complicationSlotsManager.complicationSlots[0]
        val complicationType = complication!!.complicationData.value.type
        val data = complication!!.complicationData.value.toString()
        Log.d(tag, data)
        when (complicationType) {
            ComplicationType.SHORT_TEXT -> {
                shortTextComplicationData =
                    complication!!.complicationData.value as ShortTextComplicationData
                getShortTextComplicationDataFields()
                if (dataSourceText != null && dataSourceContentDescription != null) {
                    shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                        dataSourceText!!,
                        dataSourceContentDescription!!
                    )
                } else if (dataSourceText != null) {
                    shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                        dataSourceText!!,
                        PlainComplicationText.Builder(
                            "Content description not provided by DataSource"
                        ).build()
                    )
                }
                if (dataSourceTitle != null) {
                    shortTextComplicationDataBuilder!!.setTitle(dataSourceTitle)
                }
                if (dataSourceMonochromaticImage != null) {
                    val imagePkg = data.split("pkg=")[1].split(" id=")[0]
                    val imageId = data.split("id=")[1].split(")")[0]
                    shortTextComplicationDataBuilder!!.setMonochromaticImage(
                        MonochromaticImage.Builder(
                            Icon.createWithResource(imagePkg, Integer.decode(imageId))
                        ).build()
                    )
                }
                if (dataSourceSmallImage != null) {
                    val imagePkg = data.split("pkg=")[1].split(" id=")[0]
                    val imageId = data.split("id=")[1].split(")")[0]
                    shortTextComplicationDataBuilder!!.setSmallImage(
                        SmallImage.Builder(
                            Icon.createWithResource(
                                imagePkg, Integer.decode(imageId)
                            ),
                            SmallImageType.ICON
                        ).build()
                    )
                }
                if (dataSourceAmbientImage != null) {
                    if (renderParameters.drawMode == DrawMode.AMBIENT) {
                        shortTextComplicationDataBuilder!!.setSmallImage(
                            SmallImage.Builder(
                                dataSourceAmbientImage!!,
                                SmallImageType.ICON
                            ).build()
                        )
                    }
                }
                if (dataSourceTapAction != null) {
                    shortTextComplicationDataBuilder!!.setTapAction(dataSourceTapAction)
                }
                complication!!.renderer.loadData(
                    shortTextComplicationDataBuilder!!.build(),
                    false
                )
                complication!!.render(canvas, zonedDateTime, renderParameters)
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
    private fun getShortTextComplicationDataFields() {
        dataSourceText  = shortTextComplicationData!!.text
        if (shortTextComplicationData!!.contentDescription != null) {
            dataSourceContentDescription = shortTextComplicationData!!.contentDescription
        }
        if (shortTextComplicationData!!.title != null) {
            dataSourceTitle = shortTextComplicationData!!.title
        }
        if (shortTextComplicationData!!.smallImage != null) {
            dataSourceSmallImage = shortTextComplicationData!!.smallImage
        }
        if (shortTextComplicationData!!.monochromaticImage != null) {
            dataSourceMonochromaticImage = shortTextComplicationData!!.monochromaticImage
        }
        if (shortTextComplicationData!!.smallImage?.ambientImage != null) {
            Log.d(tag, "has ambientImage")
            dataSourceAmbientImage = shortTextComplicationData!!.smallImage!!.ambientImage!!
        } else {
            Log.d(tag, "no ambientImage")
        }
        if (shortTextComplicationData!!.tapAction != null) {
            dataSourceTapAction = shortTextComplicationData!!.tapAction
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