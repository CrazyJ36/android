package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.toApiComplicationData
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.LocalDateTime
import java.time.ZonedDateTime

class WatchFaceCanvasRenderer(
    context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    var complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int
) : Renderer.CanvasRenderer2<WatchFaceCanvasRenderer.MySharedAssets>(
    surfaceHolder = surfaceHolder,
    currentUserStyleRepository = currentUserStyleRepository,
    watchState = watchState,
    canvasType = canvasType,
    interactiveDrawModeUpdateDelayMillis = 16L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    private val passedContext = context
    private var newComplicationData: ComplicationData? = null
    private var dataSourceDataSource: ComponentName? = null
    private var dataSourceTapAction: PendingIntent? = null
    private var dataSourceTitle: CharSequence? = null
    private var dataSourceText: CharSequence? = null
    private var dataSourceIcon: Icon? = null
    private var dataSourceSmallImage: Icon? = null
    private var dataSourceBurnInProtectionIcon: Icon? = null
    private var dataSourceLargeImage: Icon? = null

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {

    }

    @SuppressLint("RestrictedApi")
    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
        val complication = complicationSlotsManager.complicationSlots[0]
        val complicationWireData = complication!!
            .complicationData.value.asWireComplicationData()

        if (complication.complicationData.value.dataSource != null) {
            dataSourceDataSource = complication.complicationData.value.dataSource
        }
        if (complicationWireData.hasShortText()) {
            dataSourceText = complicationWireData.shortText!!.getTextAt(
                Resources.getSystem(), LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData.hasTapAction()) {
            dataSourceTapAction = complication.complicationData.value.tapAction
        }
        if (complicationWireData.hasShortTitle()) {
            dataSourceTitle = complicationWireData.shortTitle.toString()
        }
        if (complicationWireData.hasIcon()) {
            dataSourceIcon = complicationWireData.icon
            dataSourceIcon.apply {
                this!!.setTint(Color.WHITE)
            }
        }
        if (complicationWireData.hasSmallImage()) {
            dataSourceSmallImage = complicationWireData.smallImage
        }
        if (complicationWireData.hasLargeImage()) {
            dataSourceLargeImage = complicationWireData.largeImage
        }
        if (complicationWireData.hasBurnInProtectionIcon()) {
            dataSourceBurnInProtectionIcon = complicationWireData.burnInProtectionIcon
        }
        /*val text = complicationWireData.shortText!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )*/

        Log.d("COMPLICATION_TEST", "ComplicationType: " + complication.complicationData.value.type)
        when (complication.complicationData.value.type) {
            ComplicationType.SHORT_TEXT -> {
                newComplicationData = ShortTextComplicationData.Builder(
                    text = PlainComplicationText.Builder(dataSourceText!!).build(),
                    contentDescription = PlainComplicationText.Builder("Customizable complication items in A WatchFace.")
                        .build()
                )
                    .setDataSource(dataSourceDataSource)
                    .setTapAction(dataSourceTapAction)
                    .setMonochromaticImage(MonochromaticImage.Builder(dataSourceIcon!!).build())
                    .build()
                complication.renderer.loadData(newComplicationData!!, false)
            }
            else -> {
                Toast.makeText(
                    passedContext,
                    "Complication is not SHORT_TEXT type, showing provider default view.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        complication.render(canvas, zonedDateTime, renderParameters)
    }

    class MySharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    override suspend fun createSharedAssets(): MySharedAssets {
        return MySharedAssets()
    }
}
