package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.graphics.RectF
import android.util.Log
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
    var complicationId = 0
    lateinit var canvasComplicationFactory: CanvasComplicationFactory
    lateinit var supportedTypes: List<ComplicationType>
    lateinit var defaultDataSourcePolicy: DefaultComplicationDataSourcePolicy
    lateinit var complicationDrawable: ComplicationDrawable
    lateinit var bounds: ComplicationSlotBounds
    lateinit var complicationSlotBuilder: ComplicationSlot.Builder
    lateinit var myCurrentUserStyleRepository: CurrentUserStyleRepository

    public override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        Log.d("MYLOG", "currentUserStyle: " + currentUserStyleRepository.userStyle.value.entries)
        complicationId = 0
        supportedTypes = listOf(
            ComplicationType.SHORT_TEXT
        )
        bounds = ComplicationSlotBounds(
            RectF(0.35f, 0.35f, 0.65f, 0.65f)
        )
        defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
            SystemDataSources.DATA_SOURCE_STEP_COUNT,
            ComplicationType.SHORT_TEXT
        )
        complicationDrawable = ComplicationDrawable.getDrawable(
            this@MyWatchFaceService,
            R.drawable.complication_drawable
        )!!
        canvasComplicationFactory = CanvasComplicationFactory { watchState, listener ->
            CanvasComplicationDrawable(
                complicationDrawable,
                watchState,
                listener
            )
        }
        complicationSlotBuilder = ComplicationSlot.createRoundRectComplicationSlotBuilder(
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
            applicationContext,
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