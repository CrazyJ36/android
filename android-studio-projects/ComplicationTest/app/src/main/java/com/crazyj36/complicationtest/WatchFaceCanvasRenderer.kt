package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
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
    private var newShortTextComplicationData: ShortTextComplicationData.Builder? = null
    private var dataSourceTapAction: PendingIntent? = null

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

        if (complicationWireData.hasShortText()) {
            val dataSourceText = complicationWireData.shortText!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
            newShortTextComplicationData = ShortTextComplicationData.Builder(
                PlainComplicationText.Builder(dataSourceText).build(),
                PlainComplicationText.Builder("Draw custom complication icon on watchface").build()
            )
            if (complication.complicationData.value.dataSource != null) {
                Log.d("COMPLICATION_TEST", "hasDataSource")
                newShortTextComplicationData!!
                    .setDataSource(complication.complicationData.value.dataSource)
            }
            if (complication.complicationData.value.tapAction != null) {
                Log.d("COMPLICATION_TEST", "hasTapAction")
                dataSourceTapAction = complication.complicationData.value.tapAction
                newShortTextComplicationData!!.setTapAction(dataSourceTapAction)
            }
            if (complicationWireData.hasIcon()) {
                Log.d("COMPLICATION_TEST", "hasIcon")
                val dataSourceIcon = complicationWireData.icon!!
                val monochromaticImage = MonochromaticImage.Builder(dataSourceIcon)
                val monochromaticImageBuilt = monochromaticImage.build().image
                val monochromaticImageColored = monochromaticImageBuilt.setTint(Color.BLUE)
                newShortTextComplicationData!!.setMonochromaticImage(
                    MonochromaticImage.Builder(monochromaticImageColored).build()
                )
            }
            if (complicationWireData.hasBurnInProtectionIcon()) {
                Log.d("COMPLICATION_TEST", "hasBurnInProtectionIcon")
                val dataSourceBurnInProtectionIcon = complicationWireData.icon!!
                val monochromaticImage = MonochromaticImage.Builder(dataSourceBurnInProtectionIcon)
                val monochromaticImageBuilt = monochromaticImage.build().image
                val monochromaticImageColored = monochromaticImageBuilt.setTint(Color.BLUE)
                newShortTextComplicationData!!.setMonochromaticImage(
                    MonochromaticImage.Builder(monochromaticImageColored).build()
                )
            }
            if (complicationWireData.hasSmallImage()) {
                Log.d("COMPLICATION_TEST", "hasSmallImage")
                val dataSourceSmallImage = complicationWireData.smallImage
                dataSourceSmallImage!!.setTint(Color.BLUE)
                newShortTextComplicationData!!.setSmallImage(SmallImage.Builder(
                    dataSourceSmallImage, SmallImageType.PHOTO).build())
            }

            complication.renderer.loadData(
                newShortTextComplicationData!!.build(),
                false
            )
            Log.d("COMPLICATION_TEST", "Rendering custom complication.")
        } else {
            Log.d("COMPLICATION_TEST", "Rendering default complication.")
        }
        complication.render(canvas, zonedDateTime, renderParameters)
    }

    class MySharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    override suspend fun createSharedAssets(): MySharedAssets {
        return MySharedAssets()
    }
}
