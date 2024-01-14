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
import android.graphics.RectF
import android.graphics.drawable.Icon
import android.support.wearable.complications.ComplicationData
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable.Companion.getDrawable
import androidx.wear.watchface.complications.rendering.ComplicationStyle
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
    interactiveDrawModeUpdateDelayMillis = 16L,
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

    /*private val colorMatrix = floatArrayOf( // backup red
        1f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )*/
    private val colorMatrix = floatArrayOf( // cooler red
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 50f,
        0f, 0f, -1f, 0f, 50f,
        0f, 0f, 0f, 1f, 0f
    )
    private var dataSourceLargeImage: Icon? = null
    private var dataSourceDynamicValues: DynamicBuilders.DynamicFloat? = null
    private val paint = Paint()

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
        dataSourceLargeImage = null
        dataSourceDynamicValues = null

        getDataSourceInfo(zonedDateTime)
        when (complicationWireData!!.type) {
            ComplicationData.Companion.TYPE_SHORT_TEXT -> {
                Log.d(tag, "Loading custom ShortTextComplicationData")
                complication!!.renderer.loadData(setShortTextComplicationData(), false)
            }

            ComplicationData.Companion.TYPE_SMALL_IMAGE -> {
                Log.d(tag, "Loading custom SmallImageComplicationData")
                complication!!.renderer.loadData(setSmallImageComplicationData(), false)

                /*val complicationDrawable = ComplicationDrawable(context)
                complicationDrawable.apply {
                    bounds =
                        Rect(
                            (canvas.width / 2) - (canvas.width / 7),
                            (canvas.height / 2) - (canvas.height / 7),
                            (canvas.width / 2) + (canvas.width / 7),
                            (canvas.height / 2) + (canvas.height / 7)
                        )
                    activeStyle.apply {
                        iconColor = Color.RED
                        textColor = Color.WHITE
                        titleColor = Color.WHITE
                    }
                complicationDrawable.complicationData.asWireComplicationData().smallImage!!.apply {
                    loadDrawable(context)!!.apply {
                        colorFilter = ColorMatrixColorFilter(colorMatrix) // must come first.
                        setTintBlendMode(BlendMode.COLOR_BURN)
                        setTint(Color.RED)
                    }
                }
                complicationDrawable.setComplicationData(complication!!.complicationData.value, false)
                complicationDrawable.draw(canvas)*/

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
    private fun setShortTextComplicationData(): ShortTextComplicationData {
        Log.d(tag, "Complication is ComplicationType.SHORT_TEXT.")
        if (dataSourceText != null && dataSourceContentDescription != null) {
            Log.d(tag, "Setting dataSourceText.")
            Log.d(tag, "Setting dataSourceContentDescription.")
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
        if (dataSourceSmallImage != null && dataSourceContentDescription != null) {
            Log.d(tag, "Setting dataSourceSmallImage.")
            Log.d(tag, "Setting dataSourceContentDescription.")
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
                PlainComplicationText.Builder(
                    "Content description not provided by DataSource."
                ).build()
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
            if (complicationWireData!!.smallImage!!.type ==
                ComplicationData.Companion.IMAGE_STYLE_ICON) {
                Log.d(tag, "dataSourceSmallImage is type ICON, coloring...")

                smallImageComplicationData!!.smallImage.image.loadDrawable(context)!!.apply {
                    colorFilter = ColorMatrixColorFilter(colorMatrix) // must come first.
                    setTintBlendMode(BlendMode.COLOR_BURN)
                    setTint(Color.RED)
                }

            }
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
            ).build().apply {
                smallImage.image.loadDrawable(context)!!.apply {
                    colorFilter = ColorMatrixColorFilter(colorMatrix) // must come first.
                    setTintBlendMode(BlendMode.COLOR_BURN)
                    setTint(Color.RED)
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun getDataSourceInfo(zonedDateTime: ZonedDateTime) {
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
            dataSourceIcon = complicationWireData!!.icon
        }
        if (complicationWireData!!.hasSmallImage()) {
            Log.d(tag, "hasSmallImage")
            dataSourceSmallImage = complicationWireData!!.smallImage
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
