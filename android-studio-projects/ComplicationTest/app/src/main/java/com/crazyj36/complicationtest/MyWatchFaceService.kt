package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.Icon
import android.os.Build
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
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.toApiComplicationData
import androidx.wear.watchface.complications.data.toTypedApiComplicationData
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository

class MyWatchFaceService : WatchFaceService() {
    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        val complicationId = 0
        val supportedTypes = listOf(
            ComplicationType.SHORT_TEXT
        )
        val bounds = ComplicationSlotBounds(
            RectF(0.35f, 0.35f, 0.65f, 0.65f)
        )
        val defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
            SystemDataSources.DATA_SOURCE_DATE,
            ComplicationType.SHORT_TEXT
        )
        val complicationDrawable = ComplicationDrawable.getDrawable(
            applicationContext,
            R.drawable.complication_drawable
        )!!

        val myIcon = Icon.createWithResource(
            applicationContext,
            R.drawable.ic_action_name
        )
        complicationDrawable.setComplicationData(getWireComplicationData(complicationDrawable, myIcon), true)
        val canvasComplicationFactory = CanvasComplicationFactory { watchState, listener ->
            CanvasComplicationDrawable(
                complicationDrawable.apply {
                    activeStyle.iconColor = Color.WHITE
                                           },
                watchState,
                listener
            )
        }
        complicationDrawable.setComplicationData(getWireComplicationData(complicationDrawable, myIcon), true)
        val complicationSlotBuilder = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            id = complicationId,
            canvasComplicationFactory = canvasComplicationFactory,
            supportedTypes = supportedTypes,
            defaultDataSourcePolicy = defaultDataSourcePolicy,
            bounds = bounds
        )
        complicationDrawable.setComplicationData(getWireComplicationData(complicationDrawable, myIcon), true)

        return ComplicationSlotsManager(
            listOf(
                complicationSlotBuilder.build()
            ),
            currentUserStyleRepository
        )
    }

    @SuppressLint("RestrictedApi")
    fun getWireComplicationData(complicationDrawable: ComplicationDrawable, myIcon: Icon): ComplicationData {
        val wireComplicationData = complicationDrawable.complicationData
            .asWireComplicationData()
        wireComplicationData.icon.apply {
            this!!.setTint(Color.WHITE)
            if (Build.VERSION.SDK_INT >= 29) this.setTintBlendMode(BlendMode.MULTIPLY)
        }
        return wireComplicationData.toApiComplicationData()
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