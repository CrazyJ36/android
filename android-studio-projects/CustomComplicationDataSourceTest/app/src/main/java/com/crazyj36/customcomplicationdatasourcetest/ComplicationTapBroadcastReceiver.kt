package com.crazyj36.customcomplicationdatasourcetest

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import java.util.Timer
import java.util.TimerTask

class ComplicationTapBroadcastReceiver : BroadcastReceiver() {
    lateinit var complicationDataSourceUpdateRequester: ComplicationDataSourceUpdateRequester

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "received", Toast.LENGTH_SHORT).show()
        if (isTimerTaskAlive) {
            timerTask.cancel()
            timer.cancel()
            timer.purge()
            isTimerTaskAlive = false
        }
        timer = Timer()
        timerTask = MyTimerTask()
        CustomComplicationDataSourceService.counter = 0
        complicationDataSourceUpdateRequester = ComplicationDataSourceUpdateRequester.create(
            context = context,
            complicationDataSourceComponent = ComponentName(
                context, CustomComplicationDataSourceService::class.java
            )
        )
        if (!isTimerTaskAlive) timer.schedule(timerTask, 0, 1000)
    }

    inner class MyTimerTask : TimerTask() {
        override fun run() {
            isTimerTaskAlive = true
            if (CustomComplicationDataSourceService.counter < 10) {
                Log.d("CRAZYJ36", "timer running")
                CustomComplicationDataSourceService.counter++
                complicationDataSourceUpdateRequester.requestUpdateAll()
            } else {
                timerTask.cancel()
                timer.cancel()
                timer.purge()
                isTimerTaskAlive = false
            }
        }
    }

    companion object {
        lateinit var timer: Timer
        lateinit var timerTask: TimerTask
        var isTimerTaskAlive = false
    }
}