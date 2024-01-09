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
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageType
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
    private val myContext = context
    private val complication = complicationSlotsManager.complicationSlots[0]
    private var isShortTextComplicationType = complication!!.complicationData.value.type == ComplicationType.SHORT_TEXT
    private var isRangedValueComplicationType = complication!!.complicationData.value.type == ComplicationType.RANGED_VALUE
    @SuppressLint("RestrictedApi")
    val complicationWireData = complication!!
        .complicationData.value.asWireComplicationData()
    private var shortTextComplicationData: ShortTextComplicationData.Builder? = null
    private var dataSourceDataSource: ComponentName? = null
    private var dataSourceTapAction: PendingIntent? = null
    private var dataSourceText: CharSequence? = null
    private var dataSourceTitle: CharSequence? = null
    private var dataSourceIcon: Icon? = null
    private var dataSourceSmallImage: SmallImage? = null
    private var dataSourceBurnInProtectionIcon: Icon? = null
    private var dataSourceLargeImage: Icon? = null
    private var waitToRender: Int? = null

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
        if (waitToRender != null ) {
            when (complication!!.complicationData.value.type) {
                ComplicationType.SHORT_TEXT -> {
                    getComplicationData(complication, complicationWireData, zonedDateTime)
                    shortTextComplicationData = ShortTextComplicationData.Builder(
                        text = PlainComplicationText.Builder(dataSourceText!!).build(),
                        contentDescription = PlainComplicationText.Builder("Customizable complication items in A WatchFace.")
                            .build()
                    )
                    setComplicationData()
                    complication.renderer.loadData(shortTextComplicationData!!.build(), false)
                }

                else -> {
                    Log.d(
                        "COMPLICATION_TEST",
                        "Complication is not SHORT_TEXT type, showing provider default view."
                    )
                }
            }
        }

        complication!!.render(canvas, zonedDateTime, renderParameters)
    }
    @SuppressLint("RestrictedApi")
    fun setComplicationData() {
        if (isShortTextComplicationType) {
            if (complication!!.complicationData.value.dataSource != null)
                shortTextComplicationData!!.setDataSource(dataSourceDataSource)
            if (complicationWireData.hasTapAction())
                shortTextComplicationData!!.setTapAction(dataSourceTapAction)
            if (complicationWireData.hasShortTitle())
                shortTextComplicationData!!.setTitle(PlainComplicationText.Builder(dataSourceTitle!!).build())
            if (complicationWireData.hasIcon()) shortTextComplicationData!!.setMonochromaticImage(MonochromaticImage.Builder(dataSourceIcon!!).build())
            if (complicationWireData.hasSmallImage())
                shortTextComplicationData!!.setSmallImage(SmallImage.Builder(Icon.createWithResource(myContext, R.drawable.ic_action_name), SmallImageType.ICON).build())
        } else if (isRangedValueComplicationType) {

        }
    }
    @SuppressLint("RestrictedApi")
    fun getComplicationData(complication: ComplicationSlot, complicationWireData: android.support.wearable.complications.ComplicationData, zonedDateTime: ZonedDateTime) {
        if (complication.complicationData.value.dataSource != null)
            dataSourceDataSource = complication.complicationData.value.dataSource
        if (complicationWireData.hasTapAction())
            dataSourceTapAction = complication.complicationData.value.tapAction
        if (complicationWireData.hasShortTitle())
            dataSourceTitle = complicationWireData.shortTitle!!.getTextAt(
                Resources.getSystem(), LocalDateTime.now()
                    .atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        if (complicationWireData.hasShortText())
            dataSourceText = complicationWireData.shortText!!.getTextAt(
                Resources.getSystem(), LocalDateTime.now()
                    .atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        if (complicationWireData.hasIcon()) {
            dataSourceIcon = complicationWireData.icon
            dataSourceIcon.apply {
                this!!.setTint(Color.WHITE)
            }
        }
        if (complicationWireData.hasSmallImage())
            dataSourceSmallImage = SmallImage.Builder(
                complicationWireData.smallImage!!, SmallImageType.ICON
            ).build()
        if (complicationWireData.hasLargeImage())
            dataSourceLargeImage = complicationWireData.largeImage
        if (complicationWireData.hasBurnInProtectionIcon())
            dataSourceBurnInProtectionIcon = complicationWireData.burnInProtectionIcon
        waitToRender = 0
    }
    class MySharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    override suspend fun createSharedAssets(): MySharedAssets {
        return MySharedAssets()
    }
}
