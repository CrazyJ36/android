package com.crazyj36.watchfacetest

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

class WatchFaceServiceTest: WatchFaceService() {

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        val renderer = CustomCanvasRenderer(
            applicationContext,
            surfaceHolder = surfaceHolder,
            watchState = watchState,
            complicationSlotsManager = complicationSlotsManager,
            currentUserStyleRepository = currentUserStyleRepository,
            canvasType = CanvasType.HARDWARE,
        )
        return WatchFace(watchFaceType = WatchFaceType.ANALOG,
            renderer = renderer)
    }

    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        val defaultCanvasComplicationFactory =
            CanvasComplicationFactory { watchState, listener ->
                CanvasComplicationDrawable(
                    ComplicationDrawable.getDrawable(
                        applicationContext,
                        R.drawable.complication_style
                    )!!,
                    watchState,
                    listener
                )
            }
        val leftTopComplication = ComplicationSlot
            .createRoundRectComplicationSlotBuilder(
                id = 0,
                canvasComplicationFactory = defaultCanvasComplicationFactory,
                supportedTypes = listOf(
                    ComplicationType.RANGED_VALUE,
                    ComplicationType.MONOCHROMATIC_IMAGE,
                    ComplicationType.SHORT_TEXT,
                    ComplicationType.SMALL_IMAGE,
                    ComplicationType.NO_DATA,
                    ComplicationType.LONG_TEXT
                ),
                defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_DATE,
                    ComplicationType.SHORT_TEXT
                ),
                bounds = ComplicationSlotBounds(
                    RectF(0.3f, 0.2f, 0.43f, 0.33f),
                )
            ).build()
        val leftBottomComplication = ComplicationSlot
            .createRoundRectComplicationSlotBuilder(
                id = 1,
                canvasComplicationFactory =
                    defaultCanvasComplicationFactory,
                supportedTypes = listOf(
                    ComplicationType.RANGED_VALUE,
                    ComplicationType.MONOCHROMATIC_IMAGE,
                    ComplicationType.SHORT_TEXT,
                    ComplicationType.SMALL_IMAGE,
                    ComplicationType.NO_DATA,
                    ComplicationType.LONG_TEXT
                ),
                defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_WEATHER,
                    ComplicationType.SHORT_TEXT
                ),
                bounds = ComplicationSlotBounds(
                    RectF(0.3f, 0.34f, 0.43f, 0.47f)
                )
            ).build()
        return ComplicationSlotsManager(
            listOf(leftTopComplication, leftBottomComplication),
            currentUserStyleRepository
        )
    }
}