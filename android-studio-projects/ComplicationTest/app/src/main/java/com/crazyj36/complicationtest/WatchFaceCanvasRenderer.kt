package com.crazyj36.complicationtest

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.support.wearable.complications.ComplicationData
import android.util.Log
import android.view.SurfaceHolder
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toIcon
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.LocalDateTime
import java.time.ZonedDateTime

class WatchFaceCanvasRenderer(
    private val context: Context,
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
    interactiveDrawModeUpdateDelayMillis = 1000L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {

    private val tag = "COMPLICATION_TEST"
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
    private var dataSourceSmallImage: Icon? = null
    private var dataSourceBurnInProtectionSmallImage: Icon? = null
    private var dataSourceLargeImage: Icon? = null
    private var dataSourceDynamicValues: DynamicBuilders.DynamicFloat? = null
    private val paint = Paint()
    private val colorMatrix = floatArrayOf(
        1f, 1f, 1f, 0f, 0f,
        0.50f, 0.50f, 0.50f, 0f, 0f,
        0.50f, 0.50f, 0.50f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )
    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
        renderer(canvas, zonedDateTime, renderParameters)
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MySharedAssets
    ) {
        renderer(canvas, zonedDateTime, renderParameters)
    }

    @SuppressLint("RestrictedApi")
    private fun renderer(
        canvas: Canvas,
        zonedDateTime: ZonedDateTime,
        renderParameters: RenderParameters
    ) {
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
        dataSourceSmallImage = null
        dataSourceBurnInProtectionSmallImage = null
        dataSourceLargeImage = null
        dataSourceDynamicValues = null

        getDataSourceInfo(zonedDateTime)
        when (complication!!.complicationData.value.type) {
            ComplicationType.SHORT_TEXT -> {
                Log.d(tag, "Loading custom ShortTextComplicationData")
                complication!!.renderer.loadData(
                    setShortTextComplicationData(), false
                )
            }

            ComplicationType.SMALL_IMAGE -> {
                Log.d(tag, "Loading custom SmallImageComplicationData")
                complication!!.renderer.loadData(
                    setSmallImageComplicationData(), false
                )
            }

            else -> {
                Log.d(tag, "Unknown complication type, not customizing.")
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
    private fun setShortTextComplicationData(): ShortTextComplicationData {
        Log.d(tag, "Complication is ComplicationType.SHORT_TEXT.")
        if (dataSourceText != null && dataSourceContentDescription != null) {
            Log.d(tag, "Setting dataSourceText and dataSourceContentDescription.")
            shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                PlainComplicationText.Builder(dataSourceText!!).build(),
                PlainComplicationText.Builder(dataSourceContentDescription!!).build()
            )
        } else if (dataSourceText != null) {
            Log.d(tag, "Setting dataSourceText, no dataSourceContentDescription.")
            shortTextComplicationDataBuilder = ShortTextComplicationData.Builder(
                PlainComplicationText.Builder(dataSourceText!!).build(),
                PlainComplicationText.Builder("Content description not supplied by DataSource.")
                    .build()
            )
        }
        if (shortTextComplicationDataBuilder != null) {
            if (dataSourceDataSource != null) {
                Log.d(tag, "Setting dataSourceDataSource.")
                shortTextComplicationDataBuilder!!.setDataSource(dataSourceDataSource)
            }
            if (dataSourceTapAction != null) {
                Log.d(tag, "Setting dataSourceTapAction.")
                shortTextComplicationDataBuilder!!.setTapAction(dataSourceTapAction)
            }
            if (dataSourceTitle != null) {
                Log.d(tag, "Setting dataSourceTitle.")
                shortTextComplicationDataBuilder!!.setTitle(
                    PlainComplicationText.Builder(dataSourceTitle!!).build()
                )
            }
            if (dataSourceIcon != null) {
                Log.d(tag, "Setting dataSourceIcon.")
                shortTextComplicationDataBuilder!!.setMonochromaticImage(
                    MonochromaticImage.Builder(dataSourceIcon!!).build()
                )
            }
            shortTextComplicationData = shortTextComplicationDataBuilder!!.build()
        }
        return if (shortTextComplicationData != null) {
            Log.d(tag, "Custom shortTextComplicationData creation complete.")
            shortTextComplicationData!!
        } else {
            Log.d(tag, "dataSourceText was null, loading placeholder...")
            ShortTextComplicationData.Builder(
                PlainComplicationText.Builder("No data").build(),
                PlainComplicationText.Builder("No data.").build()
            ).build()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setSmallImageComplicationData(): SmallImageComplicationData {
        Log.d(tag, "Complication is ComplicationType.SMALL_IMAGE.")
        if (dataSourceBurnInProtectionSmallImage != null && dataSourceContentDescription != null) {
            Log.d(
                tag,
                "Setting dataSourceBurnInProtectionSmallImage and dataSourceContentDescription."
            )
            smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                SmallImage.Builder(
                    dataSourceBurnInProtectionSmallImage!!, SmallImageType.ICON
                ).build(),
                PlainComplicationText.Builder(dataSourceContentDescription!!).build()
            )
        } else if (dataSourceBurnInProtectionSmallImage != null) {
            Log.d(
                tag,
                "Setting dataSourceBurnInProtectionSmallImage, no dataSourceContentDescription."
            )
            smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                SmallImage.Builder(
                    dataSourceBurnInProtectionSmallImage!!, SmallImageType.ICON
                ).build(),
                PlainComplicationText.Builder("Content description not provided by DataSource.")
                    .build()
            )
        } else if (dataSourceSmallImage != null && dataSourceContentDescription != null) {
            Log.d(tag, "Setting dataSourceSmallImage and dataSourceContentDescription.")
            smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                SmallImage.Builder(
                    dataSourceSmallImage!!, SmallImageType.ICON
                ).build(),
                PlainComplicationText.Builder(dataSourceContentDescription!!).build()
            )
        } else if (dataSourceSmallImage != null) {
            Log.d(tag, "Setting dataSourceSmallImage, no dataSourceContentDescription.")
            smallImageComplicationDataBuilder = SmallImageComplicationData.Builder(
                SmallImage.Builder(
                    dataSourceSmallImage!!, SmallImageType.ICON
                ).build(),
                PlainComplicationText.Builder("Content description not provided by DataSource")
                    .build()
            )
        }
        if (smallImageComplicationDataBuilder != null) {
            if (dataSourceDataSource != null) {
                Log.d(tag, "Setting dataSourceDataSource.")
                smallImageComplicationDataBuilder!!.setDataSource(dataSourceDataSource)
            }
            if (dataSourceTapAction != null) {
                Log.d(tag, "Setting dataSourceTapAction.")
                smallImageComplicationDataBuilder!!.setTapAction(dataSourceTapAction)
            }
            smallImageComplicationData = smallImageComplicationDataBuilder!!.build()
        }
        return if (smallImageComplicationData != null) {
            Log.d(tag, "Custom smallImageComplicationData creation complete.")
            smallImageComplicationData!!
        } else {
            Log.d(tag, "dataSourceSmallImage was null, loading placeholder...")
            SmallImageComplicationData.Builder(
                SmallImage.Builder(
                    Icon.createWithResource(context, R.drawable.small_image_placeholder),
                    SmallImageType.ICON
                ).build(),
                PlainComplicationText.Builder("No data.").build()
            ).build()
        }
    }

    @SuppressLint("RestrictedApi") // applying attributes here works.
    private fun getDataSourceInfo(zonedDateTime: ZonedDateTime) {
        if (complicationWireData!!.dataSource != null &&
            complicationWireData!!.dataSource != complication!!.complicationData.value.dataSource
        ) {
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
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData!!.hasContentDescription()) {
            Log.d(tag, "hasContentDescription")
            dataSourceContentDescription = complicationWireData!!.contentDescription!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData!!.hasShortTitle()) {
            Log.d(tag, "hasShortTitle")
            dataSourceTitle = complicationWireData!!.shortTitle!!.getTextAt(
                Resources.getSystem(),
                LocalDateTime.now().atZone(zonedDateTime.zone).toInstant().toEpochMilli()
            )
        }
        if (complicationWireData!!.hasIcon()) {
            Log.d(tag, "hasIcon")
            dataSourceIcon = complicationWireData!!.icon!!
        }
        if (complicationWireData!!.hasSmallImage()) {
            Log.d(tag, "hasSmallImage")
            dataSourceSmallImage = complicationWireData!!.smallImage!!
            if (dataSourceSmallImage!!.type == ComplicationData.IMAGE_STYLE_ICON) {
                Log.d(tag, "ComplicationType is IMAGE_STYLE_ICON")
                dataSourceSmallImage!!.apply {
                    loadDrawable(context)!!.apply {
                        colorFilter = ColorMatrixColorFilter(colorMatrix)
                    }.toBitmap().toIcon()
                }.setTint(Color.RED).setTintBlendMode(BlendMode.COLOR)
            }
        }
        if (complicationWireData!!.hasBurnInProtectionSmallImage()) {
            Log.d(tag, "hasBurnInProtectionSmallImage")
            dataSourceBurnInProtectionSmallImage =
                complicationWireData!!.burnInProtectionSmallImage!!
            if (dataSourceBurnInProtectionSmallImage!!.type == ComplicationData.IMAGE_STYLE_ICON) {
                dataSourceBurnInProtectionSmallImage!!.apply {
                    loadDrawable(context)!!.apply {
                        colorFilter = ColorMatrixColorFilter(colorMatrix)
                    }.toBitmap().toIcon()
                }.setTint(Color.RED).setTintBlendMode(BlendMode.COLOR)
            }
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