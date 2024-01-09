package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

class WatchFaceCanvasRenderer(
    context: Context,
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

    private val passedContext = context

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
        val complicationWireData = complication!!
            .complicationData.value.asWireComplicationData()
        if (complicationWireData.hasShortText()) {
            val dataSourceText = complicationWireData.shortText!!.getTextAt(
                Resources.getSystem(), zonedDateTime.nano.toLong()
            )
            val dataSourceTapAction = complication.complicationData.value.tapAction
            val customIcon = Icon.createWithResource(passedContext, R.drawable.ic_action_name)
            val newComplicationData = ShortTextComplicationData.Builder(
                PlainComplicationText.Builder(dataSourceText).build(),
                PlainComplicationText.Builder("Draw custom complication icon on watchface").build()
            )
                .setDataSource(complicationWireData.dataSource)
                .setTapAction(dataSourceTapAction)
                .setMonochromaticImage(
                MonochromaticImage.Builder(customIcon).build()
                ).build()
            complication.renderer.loadData(newComplicationData, false)
            complication.render(canvas, zonedDateTime, renderParameters)
            Log.d("COMPLICATION_TEST", "Drew custom complication.")
        } else {
            Log.d("COMPLICATION_TEST", "Couldn't get dataSource text, is complicationType SHORT_TEXT?")
            complication.render(canvas, zonedDateTime, renderParameters)
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
