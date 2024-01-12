package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.support.wearable.complications.ComplicationData
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.data.toApiComplicationData
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.LocalDateTime
import java.time.ZonedDateTime

class WatchFaceCanvasRenderer(
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
    private val tag = "COMPLICATION_TEST"
    private var zonedDateTime: ZonedDateTime? = null
    private var complication: ComplicationSlot? = null
    private var complicationWireData: ComplicationData? = null
    private var shortTextComplicationDataBuilder: ShortTextComplicationData.Builder? = null
    private var shortTextComplicationData: ShortTextComplicationData? = null
    private var smallImageComplicationDataBuilder: SmallImageComplicationData.Builder? = null
    private var smallImageComplicationData: SmallImageComplicationData? = null
    private var dataSourceDataSource: ComponentName? = null
    private var dataSourceTapAction: PendingIntent? = null
    private var dataSourceText: CharSequence? = null
    private var dataSourceContentDescription: CharSequence? = null
    private var dataSourceTitle: CharSequence? = null
    private var dataSourceIcon: Icon? = null
    private var dataSourceBurnInProtectionIcon: Icon? = null
    private var dataSourceSmallImage: Icon? = null
    private var dataSourceBurnInProtectionSmallImage: Icon? = null
    private var dataSourceLargeImage: Icon? = null
    private var dataSourceDynamicValues: DynamicBuilders.DynamicFloat? = null
    private val paint = Paint()

    @SuppressLint("RestrictedApi")
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
        /*val complication = complicationSlotsManager.complicationSlots[0]
        val complicationWireData = complication!!.complicationData.value.asWireComplicationData()
        if (complicationWireData.type == ComplicationData.TYPE_SMALL_IMAGE && complicationWireData.hasSmallImage()) {
            Log.d(tag,"ComplicationType is SmallImage")
            when (complication.complicationData.value.asWireComplicationData().smallImageStyle) {
                ComplicationData.IMAGE_STYLE_ICON -> {
                    Log.d(tag, "smallImage type icon")
                    complicationWireData.smallImage!!.setTint(Color.BLUE)
                    val smallImageComplicationDataBuilder: SmallImageComplicationData.Builder?
                    if (complicationWireData.hasContentDescription()) {
                        smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                            SmallImage.Builder(complicationWireData.smallImage!!, SmallImageType.ICON).build(),
                            PlainComplicationText.Builder(complicationWireData.contentDescription!!
                                .getTextAt(
                                    Resources.getSystem(),
                                    LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
                                )
                            ).build()
                        )
                    } else {
                        smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                            SmallImage.Builder(complicationWireData.smallImage!!, SmallImageType.ICON).build(),
                            PlainComplicationText.Builder("Content description not provided by DataSource").build()
                        )
                    }

                    complication.renderer.loadData(
                        smallImageComplicationDataBuilder.build(),
                        false
                    )
                }

                ComplicationData.IMAGE_STYLE_PHOTO -> {
                    Log.d(tag, "smallImage type photo")
                }
            }
        } else {
            Log.d(tag, "Complication type is not SMALL_IMAGE, changing nothing")
        }
        complication.renderer.loadData(complicationWireData.toApiComplicationData(), false)
        complication.render(canvas, zonedDateTime, renderParameters)*/
        renderer(canvas, zonedDateTime, renderParameters)
    }
    @SuppressLint("RestrictedApi")
    private fun renderer(canvas: Canvas, zonedDateTime: ZonedDateTime, renderParameters: RenderParameters) {
        this.zonedDateTime = zonedDateTime
        complication = null
        complicationWireData = null
        complication = complicationSlotsManager.complicationSlots[0]
        complicationWireData = complication!!.complicationData.value.asWireComplicationData()
        shortTextComplicationDataBuilder = null
        shortTextComplicationData = null
        smallImageComplicationDataBuilder = null
        smallImageComplicationData = null
        dataSourceDataSource = null
        dataSourceTapAction = null
        dataSourceText = null
        dataSourceContentDescription = null
        dataSourceTitle = null
        dataSourceIcon = null
        dataSourceBurnInProtectionIcon = null
        dataSourceSmallImage = null
        dataSourceBurnInProtectionSmallImage = null
        dataSourceLargeImage = null
        dataSourceDynamicValues = null

        getDataSourceInfo()

        when (complicationWireData!!.type) {
            ComplicationData.Companion.TYPE_SHORT_TEXT -> {
                setShortTextComplicationData()
            }
            ComplicationData.Companion.TYPE_SMALL_IMAGE -> {
                setSmallImageComplicationData()
            }
            else -> {
                Log.d(tag, "Unknown complication type, rendering default.")
            }
        }

        complication!!.render(canvas, zonedDateTime, renderParameters)
        if (renderParameters.drawMode == DrawMode.AMBIENT) {
            Log.d(tag, "Ambient")
            paint.setARGB(255, 255, 255, 255)
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 14f
            canvas.drawText(
                "Ambient",
                canvas.width / 2f,
                canvas.height - (canvas.height / 7).toFloat(),
                paint
            )
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setShortTextComplicationData() {
        Log.d(tag, "Complication is ComplicationType.SHORT_TEXT")
        if (dataSourceText != null && dataSourceContentDescription != null) {
            Log.d(tag, "Setting text")
            shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                PlainComplicationText.Builder(dataSourceText!!).build(),
                PlainComplicationText.Builder(dataSourceContentDescription!!).build()
            )
        } else if (dataSourceText != null) {
            Log.d(tag, "Setting text")
            shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                PlainComplicationText.Builder(dataSourceText!!).build(),
                PlainComplicationText.Builder("Content description not supplied by DataSource.").build()
            )
        }
        if (shortTextComplicationDataBuilder != null) {
            if (dataSourceDataSource != null) {
                Log.d(tag, "Setting dataSource")
                shortTextComplicationDataBuilder!!.setDataSource(dataSourceDataSource)
            }
            if (dataSourceTapAction != null) {
                Log.d(tag, "Setting tapAction")
                shortTextComplicationDataBuilder!!.setTapAction(dataSourceTapAction)
            }
            if (dataSourceTitle != null) {
                Log.d(tag, "Setting title")
                shortTextComplicationDataBuilder!!.setTitle(
                    PlainComplicationText.Builder(dataSourceTitle!!).build()
                )
            }
            if (dataSourceIcon != null) { // setting color and using smallImage.builder here works properly on emulator
                Log.d(tag, "Setting icon")
                shortTextComplicationDataBuilder!!.setMonochromaticImage(
                    MonochromaticImage.Builder(dataSourceIcon!!).build()
                )
            }
            shortTextComplicationData = shortTextComplicationDataBuilder!!.build()
            complication!!.renderer.loadData(shortTextComplicationData!!, false)
        }
    }
    @SuppressLint("RestrictedApi")
    private fun setSmallImageComplicationData() {
        Log.d(tag, "Complication is ComplicationType.SMALL_IMAGE")
        if (dataSourceBurnInProtectionSmallImage != null && dataSourceContentDescription != null) {
            Log.d(tag, "Setting burnInProtectionSmallImage")
            smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                SmallImage.Builder(dataSourceBurnInProtectionSmallImage!!, SmallImageType.ICON).build(),
                PlainComplicationText.Builder(dataSourceContentDescription!!).build()
            )
            // if complicationWireData.IMAGE_STYLE_ICON/PHOTO
        } else if (dataSourceBurnInProtectionSmallImage != null) {
            Log.d(tag, "Setting burnInProtectionSmallImage")
            smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                SmallImage.Builder(dataSourceBurnInProtectionSmallImage!!, SmallImageType.ICON).build(),
                PlainComplicationText.Builder("Content description not provided by DataSource").build()
            )
        } else if (dataSourceSmallImage != null && dataSourceContentDescription != null) {
            Log.d(tag, "Setting smallImage")
            smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                SmallImage.Builder(dataSourceSmallImage!!, SmallImageType.ICON).build(),
                PlainComplicationText.Builder(dataSourceContentDescription!!).build()
            )
        } else if (dataSourceSmallImage != null) {
            Log.d(tag, "Setting smallImage")
            smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                SmallImage.Builder(dataSourceSmallImage!!, SmallImageType.ICON).build(),
                PlainComplicationText.Builder("Content description not provided by DataSource")
                    .build()
            )
        }
        if (smallImageComplicationDataBuilder != null) {
            if (dataSourceDataSource != null) {
                Log.d(tag, "Setting dataSource")
                smallImageComplicationDataBuilder!!. setDataSource(dataSourceDataSource)
            }
            if (dataSourceTapAction != null) {
                Log.d(tag, "Setting tapAction")
                smallImageComplicationDataBuilder!!.setTapAction(dataSourceTapAction)
            }
            smallImageComplicationData = smallImageComplicationDataBuilder!!.build()
            complication!!.renderer.loadData(smallImageComplicationData!!, false)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun getDataSourceInfo() {
        if (complicationWireData!!.dataSource != null) {
            Log.d(tag, "hasDataSource")
            dataSourceDataSource = complication!!.complicationData.value.dataSource
        }
        if (complicationWireData!!.hasTapAction()) {
            Log.d(tag, "hasTapAction")
            dataSourceTapAction = complication!!.complicationData.value.tapAction
        }
        if (complicationWireData!!.hasShortText()) {
            Log.d(tag, "hasShortText")
            dataSourceText = complicationWireData!!.shortText!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime!!.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData!!.hasContentDescription()) {
            Log.d(tag, "hasContentDescription")
            dataSourceContentDescription = complicationWireData!!.contentDescription!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime!!.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData!!.hasShortTitle()) {
            Log.d(tag, "hasShortTitle")
            dataSourceTitle = complicationWireData!!.shortTitle!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime!!.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData!!.hasIcon()) {
            Log.d(tag, "hasIcon")
            dataSourceIcon = complicationWireData!!.icon
        }
        if (complicationWireData!!.hasBurnInProtectionIcon()) {
            Log.d(tag, "hasBurnInProtectionIcon")
            dataSourceBurnInProtectionIcon = complicationWireData!!.burnInProtectionIcon
        }
        if (complicationWireData!!.hasSmallImage()) {
            Log.d(tag, "hasSmallImage")
            dataSourceSmallImage = complicationWireData!!.smallImage
        }
        if (complicationWireData!!.hasBurnInProtectionSmallImage()) {
            Log.d(tag, "hasBurnInProtectionSmallImage")
            dataSourceBurnInProtectionSmallImage = complicationWireData!!.burnInProtectionSmallImage
        }
        if (complicationWireData!!.hasLargeImage()) {
            Log.d(tag, "hasLargeImage")
            dataSourceLargeImage = complicationWireData!!.largeImage
        }
        if (complicationWireData!!.hasRangedDynamicValue()) {
            Log.d(tag, "hasRangedDynamicValues")
            dataSourceDynamicValues = complicationWireData!!.rangedDynamicValue
        }
    }
    class MySharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    override suspend fun createSharedAssets(): MySharedAssets {
        return MySharedAssets()
    }
}
