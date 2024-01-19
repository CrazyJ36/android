package com.crazyj36.complicationtest

import android.graphics.Color
import android.graphics.RectF
import android.view.SurfaceHolder
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
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository

class MyWatchFaceService : WatchFaceService() {
    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        val complicationId = 0
        val supportedTypes = listOf(
            ComplicationType.NOT_CONFIGURED,
            ComplicationType.EMPTY,
            ComplicationType.SHORT_TEXT,
            ComplicationType.SMALL_IMAGE
        )
        val bounds = ComplicationSlotBounds(
            RectF(0.40f, 0.40f, 0.60f, 0.60f)
        )
        val defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
            SystemDataSources.DATA_SOURCE_STEP_COUNT,
            ComplicationType.SHORT_TEXT
        )
        val complicationDrawable = ComplicationDrawable(this@MyWatchFaceService)
        val canvasComplicationFactory = CanvasComplicationFactory { watchState, listener ->
            CanvasComplicationDrawable(
                complicationDrawable,
                watchState,
                listener
            )
        }
        val complicationSlotBuilder = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            id = complicationId,
            canvasComplicationFactory = canvasComplicationFactory,
            supportedTypes = supportedTypes,
            defaultDataSourcePolicy = defaultDataSourcePolicy,
            bounds = bounds
        )

        return ComplicationSlotsManager(
            listOf(
                complicationSlotBuilder.build()
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