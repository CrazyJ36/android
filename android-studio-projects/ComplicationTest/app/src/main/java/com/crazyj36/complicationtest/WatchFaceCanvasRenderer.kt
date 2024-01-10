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
    private var shortTextComplicationDataBuilder: ShortTextComplicationData.Builder? = null
    private var shortTextComplicationData: ShortTextComplicationData? = null
    private var dataSourceDataSource: ComponentName? = null
    private var dataSourceTapAction: PendingIntent? = null
    private var dataSourceText: CharSequence? = null
    private var dataSourceTitle: CharSequence? = null
    private var dataSourceIcon: Icon? = null
    private var dataSourceBurnInProtectionIcon: Icon? = null
    private var dataSourceSmallImage: Icon? = null
    private val paint = Paint()

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
        if (complicationWireData.hasShortTitle()) {
            Log.d(tag, "hasShortTitle")
            dataSourceTitle = complicationWireData.shortTitle!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData.hasIcon()) {
            Log.d(tag, "hasIcon")
            dataSourceIcon = complicationWireData.icon!!
        }
        if (complicationWireData.hasBurnInProtectionIcon()) {
            Log.d(tag, "hasBurnInProtectionIcon")
            dataSourceBurnInProtectionIcon = complicationWireData.burnInProtectionIcon!!
        }
        if (complicationWireData.hasSmallImage()) {
            Log.d(tag, "hasSmallImage")
            dataSourceSmallImage = complicationWireData.smallImage
        }

        if (complicationWireData.type == ComplicationData.Companion.TYPE_SHORT_TEXT) {
            Log.d(tag, "complication is ComplicationType.SHORT_TEXT")
            if (dataSourceText != null) {
                Log.d(tag, "Setting text")
                shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                    PlainComplicationText.Builder(dataSourceText!!).build(),
                    PlainComplicationText.Builder(
                        "Draw custom complication icon on watchface"
                    ).build()
                )
            }
            if (dataSourceDataSource != null) {
                Log.d(tag, "Settings dataSource")
                shortTextComplicationDataBuilder!!.setDataSource(dataSourceDataSource)
            }
            if (dataSourceTapAction != null) {
                Log.d(tag, "Setting tapAction")
                shortTextComplicationDataBuilder!!.setTapAction(dataSourceTapAction)
            }
            if (dataSourceTitle != null) {
                Log.d(tag, "Setting title")
                shortTextComplicationDataBuilder!!.setTitle(
                    PlainComplicationText.Builder(dataSourceTitle!!).build()
                )
            }
            if (dataSourceIcon != null || dataSourceBurnInProtectionIcon != null) {
                Log.d(tag, "Setting icon")
                try {
                    val monochromaticImage = MonochromaticImage.Builder(dataSourceIcon!!)
                    val monochromaticImageBuilt: MonochromaticImage = monochromaticImage.build()
                    val monochromaticImageIcon: Icon = monochromaticImageBuilt.image
                    val monochromaticImageColored: Icon = monochromaticImageIcon.run {
                        setTint(Color.BLUE)
                    }
                    shortTextComplicationDataBuilder!!.setMonochromaticImage(
                        MonochromaticImage.Builder(monochromaticImageColored)
                            .build()
                    )
                } catch (exception: Exception) {
                    Log.d(
                        "COMPLICATION_TEST",
                        "Setting dataSourceIcon didn't work:\n" +
                                exception.localizedMessage +
                                "\nUsing dataSourceBurnInProtectionIcon."
                    )
                    val monochromaticImage = MonochromaticImage.Builder(
                        dataSourceBurnInProtectionIcon!!
                    )
                    val monochromaticImageBuilt = monochromaticImage.build()
                    val monochromaticImageIcon = monochromaticImageBuilt.image
                    val monochromaticImageColored = monochromaticImageIcon.run {
                        setTint(Color.BLUE)
                    }
                    shortTextComplicationDataBuilder!!.setMonochromaticImage(
                        MonochromaticImage.Builder(monochromaticImageColored)
                            .build()
                    )
                }
            }
            if (dataSourceSmallImage != null) {
                Log.d(tag, "Setting smallImage")
                shortTextComplicationDataBuilder!!.setSmallImage(
                    SmallImage.Builder(dataSourceSmallImage!!, SmallImageType.PHOTO)
                        .build()
                )
            }
            shortTextComplicationData = shortTextComplicationDataBuilder!!.build()
            complication.renderer.loadData(shortTextComplicationData!!, true)
        } else {
            Log.d(tag, "Complication is not ComplicationType.SHORT_TEXT.\n" +
            "Rendering default complication")
        }
        complication.render(canvas, zonedDateTime, renderParameters)

        if (renderParameters.drawMode == DrawMode.AMBIENT) {
            Log.d(tag, "Ambient")
            paint.setARGB(255, 255, 255, 255)
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                "Ambient",
                canvas.width / 2f,
                canvas.height - (canvas.height / 4).toFloat(),
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
