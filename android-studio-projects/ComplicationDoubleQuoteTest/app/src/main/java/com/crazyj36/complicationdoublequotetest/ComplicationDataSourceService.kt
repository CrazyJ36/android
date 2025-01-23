package com.crazyj36.complicationdoublequotetest

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService

class ComplicationDataSourceService: SuspendingComplicationDataSourceService() {
    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        return LongTextComplicationData.Builder(
            text = PlainComplicationText.Builder(
                "This complication text will only show \"this\"when rendering."
            ).build(),
            contentDescription = PlainComplicationText.Builder(
                "Example text."
            ).build()
        ).setTitle(PlainComplicationText.Builder(
            text = "\"This\" is the title."
        ).build()
        ).build()
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return LongTextComplicationData.Builder(
            text = PlainComplicationText.Builder(
                "\"Preview\" text."
            ).build(),
            contentDescription = PlainComplicationText.Builder(
                "Example text."
            ).build()
        ).build()
    }
}