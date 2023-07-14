package com.crazyj36.wearphonesynctile

import android.app.admin.DeviceAdminInfo
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ActionBuilders.Action
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.ColorBuilders
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Box
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.LayoutElementBuilders.VERTICAL_ALIGN_CENTER
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material.Button
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.protolayout.material.*
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.TileUpdateRequester
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.tools.buildDeviceParameters
import com.google.android.horologist.tiles.SuspendingTileService
import java.util.Timer
import java.util.TimerTask

private const val RESOURCES_VERSION = "0"
private val messagePath = "/messagepath"


@OptIn(ExperimentalHorologistApi::class)
class MyTileService : SuspendingTileService(), OnDataChangedListener {
    private var message: String = "tap here to refresh after sending messages from phone."
    private var count: Int = 0
    private val timer = Timer()
    private val timerTask = MyTimerTask()

    private val myClickable =  ModifiersBuilders.Modifiers.Builder()
        .setClickable(Clickable.Builder()
            .setId("clickId")
            .setOnClick(
                LoadAction.Builder().build()
            ).build()
        ).build().clickable.apply { Log.d("TILE", "log") }

    @ExperimentalHorologistApi
    override fun onCreate() {
        super.onCreate()
        timer.schedule(timerTask, 0, 3000)
    }



    @ExperimentalHorologistApi
    override fun onTileLeaveEvent(requestParams: EventBuilders.TileLeaveEvent) {
        super.onTileLeaveEvent(requestParams)
        Wearable.getDataClient(this).removeListener(this)
    }

    @ExperimentalHorologistApi
    override suspend fun resourcesRequest(requestParams: ResourcesRequest):
        ResourceBuilders.Resources = ResourceBuilders.Resources.Builder().setVersion(
            RESOURCES_VERSION).build()

    @ExperimentalHorologistApi
    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        return Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                Timeline.Builder().addTimelineEntry(
                    TimelineBuilders.TimelineEntry.Builder().setLayout(
                        Layout.Builder().setRoot(
                            Box.Builder()
                                .setVerticalAlignment(VERTICAL_ALIGN_CENTER)
                                .setWidth(DimensionBuilders.expand())
                                .setHeight(DimensionBuilders.expand())
                                .addContent(
                                    LayoutElementBuilders.Text.Builder().setText("tile").build()
                                )
                                .addContent(
                                    LayoutElementBuilders.Text.Builder().setText("new text")
                                        .setModifiers(
                                            ModifiersBuilders.Modifiers.Builder()
                                                .setPadding(
                                                    ModifiersBuilders.Padding.Builder().setTop(DimensionBuilders.dp(16f)).build()
                                            ).build()
                                    ).build()
                                )
                                .addContent(
                                    Button.Builder(
                                        applicationContext,
                                        Clickable.Builder()
                                            .setId("buttonId")
                                            .setOnClick(LoadAction.Builder().build())
                                            .build()
                                    ).build()
                                ).build()
                        ).build()
                    ).build()
                ).build()
            ).build()
    }

    @ExperimentalHorologistApi
    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)
        Wearable.getDataClient(this).addListener(this)
    }


    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        dataEventBuffer.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { dataItem ->
                    if (dataItem.uri.path?.compareTo(messagePath) == 0) {
                        val dataMap: DataMap = DataMapItem.fromDataItem(dataItem).dataMap
                        // setting message or count here doesn't seem to get back to onTileRequest
                        message = dataMap.getInt("message").toString()
                        //LoadAction.Builder()
                        //Toast.makeText(this@MyTileService, "message: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    class MyTimerTask : TimerTask() {
        override fun run() {
            timerMessage++
            LoadAction.Builder().build()
        }
    }

    companion object {
        var timerMessage = 0
    }
}