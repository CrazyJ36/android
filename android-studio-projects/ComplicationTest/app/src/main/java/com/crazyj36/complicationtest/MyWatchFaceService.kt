package com.crazyj36.complicationtest

import android.graphics.RectF
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
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
import java.lang.Exception

class MyWatchFaceService : WatchFaceService() {

    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {

        val defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
            SystemDataSources.DATA_SOURCE_DATE,
            ComplicationType.SHORT_TEXT
        )

        try {
            val test = defaultDataSourcePolicy.primaryDataSourceDefaultType.toString()
            Log.d("MYLOG", test)
        } catch (exception: Exception) {
            Log.d("MYLOG", "Didn\'t work: ${exception.localizedMessage}")
        }

        return ComplicationSlotsManager(
            listOf(
                ComplicationSlot.createRoundRectComplicationSlotBuilder(
                    id = 0,
                    canvasComplicationFactory = { watchState, listener ->
                        CanvasComplicationDrawable(
                            ComplicationDrawable.getDrawable(
                                this@MyWatchFaceService,
                                R.drawable.complication_drawable
                            )!!,
                            watchState,
                            listener
                        )
                    },
                    supportedTypes = listOf(
                        ComplicationType.SHORT_TEXT,
                        ComplicationType.EMPTY),
                    defaultDataSourcePolicy = defaultDataSourcePolicy,
                    bounds = ComplicationSlotBounds(
                        RectF(0.30f, 0.30f, 0.70f, 0.70f),
                    )
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