package com.crazyj36.complicationtest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
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
    interactiveDrawModeUpdateDelayMillis = 16L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    private val tag = "COMPLICATION_TEST"
    private var complication: ComplicationSlot? = null
    private val paint = Paint()

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

        when (complication!!.complicationData.value.type) {
            ComplicationType.SHORT_TEXT -> {
                val dataSource = complication!!.complicationData.value as ShortTextComplicationData
                val newDataBuilder: ShortTextComplicationData.Builder =
                    if (dataSource.contentDescription != null) {
                        ShortTextComplicationData.Builder(
                            dataSource.text,
                            dataSource.contentDescription!!
                        )
                    } else {
                        ShortTextComplicationData.Builder(
                            dataSource.text,
                            PlainComplicationText.Builder(
                                "Content description not provided by complication DataSource."
                            ).build()
                        )
                    }
                if (dataSource.title != null) {
                    newDataBuilder.setTitle(dataSource.title)
                }
                if (dataSource.monochromaticImage != null) {
                    newDataBuilder.setMonochromaticImage(
                        dataSource.monochromaticImage
                    )
                } else {
                    if (renderParameters.drawMode == DrawMode.AMBIENT &&
                        dataSource.smallImage?.ambientImage != null
                    ) {
                        Log.d(tag, "Using ambientImage.")
                        newDataBuilder.setSmallImage(
                            SmallImage.Builder(
                                dataSource.smallImage?.ambientImage!!.setTint(Color.WHITE),
                                SmallImageType.ICON
                            ).build()
                        )
                    } else if (dataSource.smallImage?.image != null) {
                        newDataBuilder.setSmallImage(
                            SmallImage.Builder(
                                dataSource.smallImage?.image!!.setTint(Color.WHITE),
                                SmallImageType.ICON
                            ).build()
                        )
                    }
                }
                if (dataSource.tapAction != null) {
                    newDataBuilder.setTapAction(dataSource.tapAction)
                }
                complication!!.renderer.loadData(
                    newDataBuilder.build(),
                    false
                )
                complication!!.render(canvas, zonedDateTime, renderParameters)
            }

            ComplicationType.SMALL_IMAGE -> {
                val dataSource = complication!!.complicationData.value as SmallImageComplicationData
                if (dataSource.smallImage.type == SmallImageType.ICON) {
                    val drawable: Drawable? = if (renderParameters.drawMode == DrawMode.AMBIENT &&
                        dataSource.smallImage.ambientImage != null
                    ) {
                        Log.d(tag, "Using ambientImage.")
                        dataSource.smallImage.ambientImage!!.loadDrawable(context)
                    } else {
                        dataSource.smallImage.image.loadDrawable(context)
                    }
                    drawable!!.colorFilter = ColorMatrixColorFilter(
                        floatArrayOf(
                            0.4f, 0.4f, 0.4f, 0f, 0f,
                            0.4f, 0.4f, 0.4f, 0f, 0f,
                            0.4f, 0.4f, 0.4f, 0f, 0f,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                    drawable.setTintMode(PorterDuff.Mode.MULTIPLY)
                    drawable.setTint(Color.WHITE)
                    drawable.bounds = Rect(
                        (canvas.width * 0.45).toInt(),
                        (canvas.height * 0.45).toInt(),
                        (canvas.width * 0.55).toInt(),
                        (canvas.height * 0.55).toInt()
                    )
                    drawable.draw(canvas)
                } else if (dataSource.smallImage.type == SmallImageType.PHOTO) {
                    complication!!.render(canvas, zonedDateTime, renderParameters)
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
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 18f
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