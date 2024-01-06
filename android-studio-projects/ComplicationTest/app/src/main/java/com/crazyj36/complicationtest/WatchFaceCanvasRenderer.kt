package com.crazyj36.complicationtest

import android.content.Context
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotBoundsType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
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
    val myWatchState = watchState
    val myContext = context
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
        // render complications
        for ((_, complication) in complicationSlotsManager.complicationSlots) {
            MyWatchFaceService.complicationDrawable.setContext(myContext)
            MyWatchFaceService.complicationDrawable.setTint(Color.WHITE)
            if (Build.VERSION.SDK_INT >= 29) MyWatchFaceService.complicationDrawable.setTintBlendMode(BlendMode.SRC_ATOP)
            MyWatchFaceService.complicationDrawable.setBounds(100, 100, 200, 200)
            MyWatchFaceService.complicationDrawable.draw(canvas)
            /*complication.renderer.drawHighlight(canvas, Rect(100, 100, 200, 200),
                ComplicationSlotBoundsType.ROUND_RECT, zonedDateTime, Color.WHITE
                )*/
            //complication.render(canvas, zonedDateTime, renderParameters)
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