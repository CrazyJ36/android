package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.Icon
import android.os.Build
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
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.TimeRange
import androidx.wear.watchface.complications.data.toApiComplicationData
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository

class MyWatchFaceService : WatchFaceService() {
    var complicationDrawable: ComplicationDrawable? = null
    var myIcon: Icon? = null

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
        complicationDrawable = ComplicationDrawable.getDrawable(
            applicationContext,
            R.drawable.complication_drawable
        )!!

        myIcon = Icon.createWithResource(
            applicationContext,
            R.drawable.ic_action_name
        )


        getIcon()
        //getWireComplicationData(complicationDrawable!!, myIcon!!)
        val canvasComplicationFactory = CanvasComplicationFactory { watchState, listener ->
            CanvasComplicationDrawable(
                complicationDrawable!!,
                watchState,
                listener
            )
        }
        //getWireComplicationData(complicationDrawable!!, myIcon!!)
        val complicationSlotBuilder = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            id = complicationId,
            canvasComplicationFactory = canvasComplicationFactory,
            supportedTypes = supportedTypes,
            defaultDataSourcePolicy = defaultDataSourcePolicy,
            bounds = bounds
        )
        getWireComplicationData(complicationDrawable!!, myIcon!!)
        return ComplicationSlotsManager(
            listOf(
                complicationSlotBuilder.build()
            ),
            currentUserStyleRepository
        )
    }

    fun getIcon() {
        val complicationDataClass = ShortTextComplicationData::class.java
        Log.d("MYLOG", "methods of ShortTextComplicationData: " + complicationDataClass.methods.toString())
        val setMonochromaticImageMethod =
            complicationDataClass.getMethod(
            "setMonochromaticImage"
        )
        setMonochromaticImageMethod.invoke(complicationDrawable,
            MonochromaticImage.Builder(myIcon!!).build())

    }

    @Suppress("RestrictedApi")
    fun getWireComplicationData(complicationDrawable: ComplicationDrawable, myIcon: Icon) {
        Log.d("MYLOG", "complicationDrawable ComplicationData: " + complicationDrawable.complicationData.toString())
        val wireComplicationData = complicationDrawable.complicationData.asWireComplicationData()
        wireComplicationData.icon?.apply {
            myIcon
            setTint(Color.WHITE)
            if (Build.VERSION.SDK_INT >= 29) setTintBlendMode(BlendMode.MULTIPLY)
        }
        complicationDrawable.setComplicationData(wireComplicationData.toApiComplicationData(), true)
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