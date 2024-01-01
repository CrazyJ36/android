package com.crazyj36.customcomplicationdatasourcetest

import android.content.ComponentName
import android.util.Log
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import java.util.Timer
import java.util.TimerTask


class CustomComplicationDataSourceService: SuspendingComplicationDataSourceService() {
    private var counter: Int = 0
    /*private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask
    val updater = ComplicationDataSourceUpdateRequester.create(
    this, ComponentName(
            this, javaClass)
    )*/


    override fun getPreviewData(type: ComplicationType): ComplicationData {
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(
                counter.toString()
            ).build(),
            contentDescription = PlainComplicationText.Builder(
                getString(R.string.shortTextComplicationContentDescription)
            ).build()
        ).setTitle(
            PlainComplicationText.Builder(
                getString(R.string.complicationTitleText)
            ).build()
        ).build()
    }

    override fun onComplicationActivated(complicationInstanceId: Int, type: ComplicationType) {
        super.onComplicationActivated(complicationInstanceId, type)
        Log.d("CRAZYJ36", "complication activated")
        counter = 0
        /*timer = Timer()
        timerTask = MyTimerTask()
        timer.schedule(timerTask, 0, 1000)*/
    }

    override fun onComplicationDeactivated(complicationInstanceId: Int) {
        super.onComplicationDeactivated(complicationInstanceId)
        Log.d("CRAZYJ36", "complication deactivated")
        /*if (this::timer.isInitialized) {
            timerTask.cancel()
            timer.cancel()
            timer.purge()
        }*/
    }

    override suspend fun onComplicationRequest(
        request: ComplicationRequest
    ): ComplicationData {
        Log.d("CRAZYJ36", "updating complication")
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(
                counter.toString()
            ).build(),
            contentDescription = PlainComplicationText.Builder(
                getString(R.string.shortTextComplicationContentDescription)
            ).build()
        ).setTitle(
            PlainComplicationText.Builder(
                getString(R.string.complicationTitleText)
            ).build()
        ).build()
    }

    inner class MyTimerTask : TimerTask() {
        override fun run() {
            /*if (counter < 10) {
                Log.d("CRAZYJ36", "timer running")
                counter++
                updater.requestUpdateAll()
            } else {
                timerTask.cancel()
                timer.cancel()
                timer.purge()
            }*/
        }
    }
}
