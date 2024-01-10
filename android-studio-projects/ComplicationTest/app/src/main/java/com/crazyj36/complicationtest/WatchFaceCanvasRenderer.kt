package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.support.wearable.complications.ComplicationData
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PhotoImageComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.LocalDateTime
import java.time.ZonedDateTime

class WatchFaceCanvasRenderer(
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

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {

    }

    @SuppressLint("RestrictedApi")
    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {

        val complication = complicationSlotsManager.complicationSlots[0]
        val complicationWireData = complication!!.complicationData.value.asWireComplicationData()
        var dataSourceDataSource: ComponentName? = null
        var dataSourceTapAction: PendingIntent? = null
        var dataSourceText: CharSequence? = null
        var dataSourceContentDescription: CharSequence? = null
        var dataSourceTitle: CharSequence? = null
        var dataSourceIcon: Icon? = null
        var dataSourceBurnInProtectionIcon: Icon? = null
        var dataSourceSmallImage: Icon? = null
        var dataSourceBurnInProtectionSmallImage: Icon? = null
        var dataSourceLargeImage: Icon? = null
        val paint = Paint()

        if (complicationWireData.dataSource != null) {
            Log.d(tag, "hasDataSource")
            dataSourceDataSource = complication.complicationData.value.dataSource
        }
        if (complicationWireData.hasTapAction()) {
            Log.d(tag, "hasTapAction")
            dataSourceTapAction = complication.complicationData.value.tapAction
        }
        if (complicationWireData.hasShortText()) {
            Log.d(tag, "hasShortText")
            dataSourceText = complicationWireData.shortText!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData.hasContentDescription()) {
            Log.d(tag,"hasContentDescription")
            dataSourceContentDescription = complicationWireData.contentDescription!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData.hasShortTitle()) {
            Log.d(tag, "hasShortTitle")
            dataSourceTitle = complicationWireData.shortTitle!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData.hasIcon()) {
            Log.d(tag, "hasIcon")
            dataSourceIcon = complicationWireData.icon!!.setTint(Color.BLUE)
        }
        if (complicationWireData.hasBurnInProtectionIcon()) {
            Log.d(tag, "hasBurnInProtectionIcon")
            dataSourceBurnInProtectionIcon = complicationWireData.burnInProtectionIcon!!
        }
        if (complicationWireData.hasSmallImage()) {
            Log.d(tag, "hasSmallImage")
            dataSourceSmallImage = complicationWireData.smallImage
        }
        if (complicationWireData.hasBurnInProtectionSmallImage()) {
            Log.d(tag, "hasBurnInProtectionSmallImage")
            dataSourceBurnInProtectionSmallImage = complicationWireData.burnInProtectionSmallImage
        }
        if (complicationWireData.hasLargeImage()) {
            Log.d(tag, "hasLargeImage")
            dataSourceLargeImage = complicationWireData.largeImage
        }
        if (complicationWireData.type == ComplicationData.Companion.TYPE_SHORT_TEXT) {
            Log.d(tag, "Complication is ComplicationType.SHORT_TEXT")
            var shortTextComplicationDataBuilder: ShortTextComplicationData.Builder? = null
            if (dataSourceText != null && dataSourceContentDescription != null) {
                Log.d(tag, "Setting text")
                shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                    PlainComplicationText.Builder(dataSourceText).build(),
                    PlainComplicationText.Builder(dataSourceContentDescription).build()
                )
            }
            if (shortTextComplicationDataBuilder != null) {
                if (dataSourceDataSource != null) {
                    Log.d(tag, "Setting dataSource")
                    shortTextComplicationDataBuilder.setDataSource(dataSourceDataSource)
                }
                if (dataSourceTapAction != null) {
                    Log.d(tag, "Setting tapAction")
                    shortTextComplicationDataBuilder.setTapAction(dataSourceTapAction)
                }
                if (dataSourceTitle != null) {
                    Log.d(tag, "Setting title")
                    shortTextComplicationDataBuilder.setTitle(
                        PlainComplicationText.Builder(dataSourceTitle).build()
                    )
                }
                if (dataSourceIcon != null) {
                    Log.d(tag, "Setting icon")
                    dataSourceIcon.setTint(Color.BLUE)
                    shortTextComplicationDataBuilder.setMonochromaticImage(
                        MonochromaticImage.Builder(
                            dataSourceIcon
                        ).build()
                    )
                }
                if (dataSourceBurnInProtectionIcon != null) {
                    Log.d(tag, "Setting burnInProtectionIcon")
                    dataSourceBurnInProtectionIcon.setTint(Color.BLUE)
                    shortTextComplicationDataBuilder.setMonochromaticImage(
                        MonochromaticImage.Builder(
                            dataSourceBurnInProtectionIcon
                        ).build()
                    )
                }
                if (dataSourceSmallImage != null) {
                    Log.d(tag, "Setting smallImage")
                    dataSourceSmallImage.setTint(Color.BLUE)
                    shortTextComplicationDataBuilder.setSmallImage(
                        SmallImage.Builder(
                            dataSourceSmallImage,
                            SmallImageType.ICON
                        ).build()
                    )
                }
                if (dataSourceBurnInProtectionSmallImage != null) {
                    dataSourceBurnInProtectionSmallImage.setTint(Color.BLUE)
                    shortTextComplicationDataBuilder.setSmallImage(
                        SmallImage.Builder(
                            dataSourceBurnInProtectionSmallImage,
                            SmallImageType.ICON
                        ).build()
                    )
                }

                complication.renderer.loadData(shortTextComplicationDataBuilder.build(), false)
            }
        } else if (complicationWireData.type == ComplicationData.Companion.TYPE_LARGE_IMAGE) {
            Log.d(tag, "Complication is ComplicationType.LARGE_IMAGE")
            var photoImageComplicationDataBuilder: PhotoImageComplicationData.Builder? = null
            if (dataSourceLargeImage != null && dataSourceContentDescription != null) {
                Log.d(tag, "Setting largeImage")
                photoImageComplicationDataBuilder = PhotoImageComplicationData.Builder(
                    dataSourceLargeImage,
                    PlainComplicationText.Builder(dataSourceContentDescription).build()
                )
            }
            if (photoImageComplicationDataBuilder != null) {
                complication.renderer.loadData(photoImageComplicationDataBuilder.build(), false)
            }
        } else {
            Log.d(tag, "Unknown complication type, rendering default.")
        }

        complication.render(canvas, zonedDateTime, renderParameters)
        if (renderParameters.drawMode == DrawMode.AMBIENT) {
            Log.d(tag, "Ambient")
            paint.setARGB(255, 255, 255, 255)
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 11f
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
