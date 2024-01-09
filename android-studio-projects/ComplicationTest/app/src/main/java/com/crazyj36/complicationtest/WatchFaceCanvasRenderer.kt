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
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

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
        // render complications
        val complication = complicationSlotsManager.complicationSlots[0]
        complication.apply {
        //for ((_, complication) in complicationSlotsManager.complicationSlots) {
            //Log.d("MYLOG", complication.complicationData.value.toString())
            val complicationWireFormat = complication!!.complicationData.value.asWireComplicationData()


            var dataSourceTitle: CharSequence? = null
            var dataSourceText: CharSequence? = null
            if (complication.complicationData.value.type == ComplicationType.SHORT_TEXT &&
                complicationWireFormat.hasShortTitle()) {
                dataSourceTitle = complicationWireFormat.shortTitle!!.getTextAt(
                    Resources.getSystem(), zonedDateTime.toEpochSecond()
                )
            }
            if (complication.complicationData.value.type == ComplicationType.SHORT_TEXT &&
                complicationWireFormat.hasShortText()) {
                dataSourceText = complicationWireFormat.shortText!!.getTextAt(
                    Resources.getSystem(), zonedDateTime.toEpochSecond()
                )
            }
            if (dataSourceTitle != null && dataSourceText != null) {
                Log.d("COMPLICATION_TEST", "$dataSourceTitle $dataSourceText")
                val myComplicationData = ShortTextComplicationData.Builder(
                    PlainComplicationText.Builder(dataSourceText).build(),
                    PlainComplicationText.Builder("desc").build()
                ).setTitle(
                    PlainComplicationText.Builder(dataSourceTitle).build()
                ).setMonochromaticImage(MonochromaticImage.Builder(
                    Icon.createWithResource(passedContext, R.drawable.ic_action_name)).build()
                ).build()

                complication.renderer.loadData(
                    myComplicationData,
                    true
                )
            }
            else {
                Log.d("COMPLICATION_TEST", "Complication title and/or text are null, possibly due to the assigned complication not being Type.SHORT_TEXT")
            }

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
