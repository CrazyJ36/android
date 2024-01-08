package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.toApiComplicationData
import androidx.wear.watchface.complications.data.toApiComplicationText
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import kotlinx.coroutines.flow.transform
import java.time.ZonedDateTime

class WatchFaceCanvasRenderer(
    val context: Context,
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
        // render complications
        for ((_, complication) in complicationSlotsManager.complicationSlots) {
            ComplicationDrawable.getDrawable(
                context,
                R.drawable.complication_drawable
            )?.let {
                (complication.renderer as CanvasComplicationDrawable).drawable = it
            }
            complication.render(canvas, zonedDateTime, renderParameters)


            /*var complicationText = ShortTextComplicationData.Builder(
                PlainComplicationText.Builder(
                    SystemDataSources::class.java.methods()
                ).build(),
                PlainComplicationText.Builder("contentDesc").build()
            )

            val wireComplicationData = complicationSlotsManager.complicationSlots[0]!!
                .complicationData.value.asWireComplicationData()

            // Works for SHORT_TEXT dataSources only
            if (wireComplicationData.hasShortText()) {
                Log.d("MYLOG", "DEFAULT TEXT: " + wireComplicationData.shortText!!
                    .getTextAt(Resources.getSystem(), zonedDateTime.toEpochSecond()))
            } else Log.d("MYLOG", "Add A SHORT_TEXT type complication to get its text.")

            complication.renderer.loadData(complicationText.build(), true)
            //complication.renderer.loadData(wireComplicationData.toApiComplicationData(), true)
            complication.render(canvas, zonedDateTime, renderParameters)
            Log.d(
                "MYLOG", "complication data source (WatchFaceCanvasRenderer): " +
                        complicationSlotsManager.complicationSlots[0]!!.complicationData.value
            )*/
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