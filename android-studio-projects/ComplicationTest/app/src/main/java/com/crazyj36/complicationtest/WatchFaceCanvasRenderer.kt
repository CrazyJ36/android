package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.toApiComplicationData
import androidx.wear.watchface.style.CurrentUserStyleRepository
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
        Log.d(
            "MYLOG", "complication data source (WatchFaceCanvasRenderer): " +
                    complicationSlotsManager.complicationSlots[0]!!.complicationData.value
        )

        // render complications
        for ((_, complication) in complicationSlotsManager.complicationSlots) {
            val complicationData = complicationSlotsManager.complicationSlots[0]!!
                .complicationData.value.asWireComplicationData()
            complicationData.smallImage.let {
                it.apply {
                    Icon.createWithResource(context, R.drawable.ic_action_name)
                }
            }
            complicationSlotsManager.complicationSlots[0]!!.renderer.loadData(
                complicationData.toApiComplicationData(), true
            )

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