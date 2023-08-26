package com.crazyj36.watchfacetest

import android.content.Intent
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema
import java.util.Timer
import java.util.TimerTask

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
    companion object {
        var count = 0
        val timer = Timer()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer.schedule(object: TimerTask() {
            override fun run() {
                if (count > 10) count = 0
                count++
            }
        }, 0 , 1000)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        timer.purge()
    }
    override fun createUserStyleSchema(): UserStyleSchema {
        return super.createUserStyleSchema()
    }

    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository):
            ComplicationSlotsManager {
        return super.createComplicationSlotsManager(
            currentUserStyleRepository)
    }
}