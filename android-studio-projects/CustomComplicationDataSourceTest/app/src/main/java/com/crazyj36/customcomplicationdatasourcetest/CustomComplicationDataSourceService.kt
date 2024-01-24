package com.crazyj36.customcomplicationdatasourcetest

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService

class CustomComplicationDataSourceService: SuspendingComplicationDataSourceService() {

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
    }

    override fun onComplicationDeactivated(complicationInstanceId: Int) {
        super.onComplicationDeactivated(complicationInstanceId)
        Log.d("CRAZYJ36", "complication deactivated")
        if (ComplicationTapBroadcastReceiver.isTimerTaskAlive) {
            ComplicationTapBroadcastReceiver.timerTask.cancel()
            ComplicationTapBroadcastReceiver.timer.cancel()
            ComplicationTapBroadcastReceiver.timer.purge()
        }
    }

    override suspend fun onComplicationRequest(
        request: ComplicationRequest
    ): ComplicationData {
        Log.d("CRAZYJ36", "updating complication")
        val intent = Intent(this, ComplicationTapBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent
            .getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE)
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
        ).setTapAction(pendingIntent)
            .build()
    }

  companion object {
      var counter: Int = 0
  }
}
