package com.crazyj36.watchfacetest

import android.content.Context
import android.graphics.RectF
import android.os.Looper
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
import kotlinx.coroutines.MainScope
import java.util.concurrent.Executor

class WatchFaceServiceTest: WatchFaceService() {
    override fun onCreate() {
        super.onCreate()
        val showWeatherComplicationWarning: Boolean =
            getSharedPreferences("file_show_weather_complication_warning",
                Context.MODE_PRIVATE).getBoolean("showWeatherComplicationWarning", true)
        if (showWeatherComplicationWarning) {
            mainLooper.apply {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.watchFaceConfigurationToastText),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        getSharedPreferences("file_show_weather_complication_warning",
            Context.MODE_PRIVATE).edit().putBoolean("showWeatherComplicationWarning", false)
            .apply()
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
        val leftMiddleComplication = ComplicationSlot
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
                    SystemDataSources.NO_DATA_SOURCE,
                    ComplicationType.SHORT_TEXT
                ),
                bounds = ComplicationSlotBounds(
                    RectF(0.3f, 0.34f, 0.43f, 0.47f)
                )
            ).build()
        return ComplicationSlotsManager(
            listOf(leftTopComplication, leftMiddleComplication),
            currentUserStyleRepository
        )
    }
}