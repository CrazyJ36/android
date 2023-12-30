package com.crazyj36.watchfacerevokepermissiontest

import android.graphics.Color
import android.graphics.RectF
import android.view.SurfaceHolder
import androidx.appcompat.content.res.AppCompatResources
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
    private lateinit var complicationDrawable: ComplicationDrawable
    override fun onCreate() {
        super.onCreate()
        complicationDrawable = ComplicationDrawable.getDrawable(
            this@MyWatchFaceService, R.drawable.complication_drawable)!!
    }
    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        val defaultCanvasComplicationFactory =
            CanvasComplicationFactory { watchState, listener ->
                CanvasComplicationDrawable(
                    complicationDrawable,
                    watchState,
                    listener
                )
            }

        val supportedTypesList = listOf(
            ComplicationType.SHORT_TEXT,
            ComplicationType.EMPTY
        )
        val complication = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            id = 0,
            canvasComplicationFactory = defaultCanvasComplicationFactory,
            supportedTypes = supportedTypesList,
            defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                SystemDataSources.DATA_SOURCE_DATE,
                ComplicationType.SHORT_TEXT
            ),
            bounds = ComplicationSlotBounds(
                RectF(0.30f, 0.30f, 0.70f, 0.70f),
            )
        ).build()
        return ComplicationSlotsManager(
            listOf(
                complication
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