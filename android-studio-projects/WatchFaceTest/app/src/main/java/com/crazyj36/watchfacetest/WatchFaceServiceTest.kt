package com.crazyj36.watchfacetest

import android.content.Intent
import android.content.pm.PackageManager
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
    override fun onCreate() {
        super.onCreate()
        if (checkSelfPermission(
                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
            ) != PackageManager.PERMISSION_GRANTED) {
            startActivity(
                Intent(applicationContext, GetComplicationPermission::class.java)
                    .setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    )
            )
        }
    }
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
        val supportedTypesList = listOf(
            ComplicationType.SHORT_TEXT,
            ComplicationType.SMALL_IMAGE,
            ComplicationType.RANGED_VALUE,
            ComplicationType.NO_DATA,
            ComplicationType.LONG_TEXT,
            ComplicationType.MONOCHROMATIC_IMAGE,
            ComplicationType.NOT_CONFIGURED,
            ComplicationType.EMPTY,
            ComplicationType.GOAL_PROGRESS,
            ComplicationType.NO_PERMISSION,
            ComplicationType.PHOTO_IMAGE,
            ComplicationType.WEIGHTED_ELEMENTS
        )
        val leftTopComplication = ComplicationSlot
            .createRoundRectComplicationSlotBuilder(
                id = 0,
                canvasComplicationFactory = defaultCanvasComplicationFactory,
                supportedTypes = supportedTypesList,
                defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_DATE,
                    ComplicationType.SHORT_TEXT
                ),
                bounds = ComplicationSlotBounds( // 16 width/height
                    RectF(0.28f, 0.20f, 0.44f, 0.36f),
                )
            ).build()
        val leftBottomComplication = ComplicationSlot
            .createRoundRectComplicationSlotBuilder(
                id = 1,
                canvasComplicationFactory =
                    defaultCanvasComplicationFactory,
                supportedTypes = supportedTypesList,
                defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_WEATHER,
                    ComplicationType.SHORT_TEXT
                ),
                bounds = ComplicationSlotBounds(
                    RectF(0.28f, 0.40f, 0.44f, 0.56f)
                )
            ).build()
        val rightTopComplication = ComplicationSlot
            .createRoundRectComplicationSlotBuilder(
                id = 2,
                canvasComplicationFactory =
                    defaultCanvasComplicationFactory,
                supportedTypes = supportedTypesList,
                defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_STEP_COUNT,
                    ComplicationType.SHORT_TEXT
                ),
                bounds = ComplicationSlotBounds(
                    RectF(0.57f, 0.20f, 0.73f, 0.36f)
                )
            ).build()
        val rightBottomComplication = ComplicationSlot
            .createRoundRectComplicationSlotBuilder(
                id = 3,
                canvasComplicationFactory = defaultCanvasComplicationFactory,
                supportedTypes = supportedTypesList,
                defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_WATCH_BATTERY,
                    ComplicationType.SHORT_TEXT
                ),
                bounds = ComplicationSlotBounds(
                    RectF(0.57f, 0.40f, 0.73f, 0.56f)
                )
            ).build()
        return ComplicationSlotsManager(
            listOf(leftTopComplication, leftBottomComplication,
                rightTopComplication, rightBottomComplication),
            currentUserStyleRepository
        )
    }
}