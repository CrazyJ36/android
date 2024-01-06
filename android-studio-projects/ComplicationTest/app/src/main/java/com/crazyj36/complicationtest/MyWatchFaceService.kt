package com.crazyj36.complicationtest

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.RectF
import android.os.Build
import android.view.SurfaceHolder
import androidx.compose.ui.graphics.BlendMode
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.lang.Exception

class MyWatchFaceService : WatchFaceService() {

    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        val complicationId = 0
        val supportedTypes = listOf(
            ComplicationType.SHORT_TEXT)
        val bounds = ComplicationSlotBounds(
            RectF(0.30f, 0.30f, 0.70f, 0.70f),
        )
        val defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
            SystemDataSources.DATA_SOURCE_DATE,
            ComplicationType.SHORT_TEXT
        )
        val complicationDrawable = ComplicationDrawable.getDrawable(
            this@MyWatchFaceService,
            R.drawable.complication_drawable
        )!!
        complicationDrawable.apply {
            this.setTint(Color.WHITE)
            if (Build.VERSION.SDK_INT >= 29) this.setTintBlendMode(android.graphics.BlendMode.SRC_ATOP)
        }
        val canvasComplicationFactory = CanvasComplicationFactory { watchState, listener ->
            CanvasComplicationDrawable(
                complicationDrawable,
                watchState,
                listener
            )
        }
        return ComplicationSlotsManager(
            listOf(
                ComplicationSlot.createRoundRectComplicationSlotBuilder(
                    id = complicationId,
                    canvasComplicationFactory = canvasComplicationFactory,
                    supportedTypes = supportedTypes,
                    defaultDataSourcePolicy = defaultDataSourcePolicy,
                    bounds = bounds
                ).build()
            ),
            currentUserStyleRepository
        )
    }

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        val renderer = WatchFaceCanvasRenderer(
            context = applicationContext,
            surfaceHolder = surfaceHolder,
            watchState = watchState,
            complicationSlotsManager = complicationSlotsManager,
            currentUserStyleRepository = currentUserStyleRepository,
            canvasType = CanvasType.HARDWARE
        )
        return WatchFace(
            watchFaceType = WatchFaceType.ANALOG,
            renderer = renderer
        )
    }
}