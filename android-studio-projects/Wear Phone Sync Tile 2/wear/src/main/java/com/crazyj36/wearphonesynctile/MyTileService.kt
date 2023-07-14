package com.crazyj36.wearphonesynctile

import android.widget.Toast
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ActionBuilders.LaunchAction
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Box
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.LayoutElementBuilders.VERTICAL_ALIGN_CENTER
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.Text
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import java.util.Timer
import java.util.TimerTask

private const val RESOURCES_VERSION = "0"
private val messagePath = "/messagepath"


@OptIn(ExperimentalHorologistApi::class)
class MyTileService : SuspendingTileService(), OnDataChangedListener {

    override suspend fun resourcesRequest(requestParams: ResourcesRequest):
        ResourceBuilders.Resources = ResourceBuilders.Resources.Builder().setVersion(
            RESOURCES_VERSION).build()

    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        val text1: Text = Text.Builder(this@MyTileService, message).build()

        val button = Button.Builder(this@MyTileService, Clickable.Builder()
            .setOnClick(LaunchAction.Builder()
                .setAndroidActivity(
                    ActionBuilders.AndroidActivity.Builder()
                        .setClassName(MainWearActivity::class.java.name)
                        .setPackageName(this.packageName).build()
                ).build()
            ).build())
            .setTextContent("button")
            .build()

        val box: Box = Box.Builder()
            .setVerticalAlignment(VERTICAL_ALIGN_CENTER)
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(text1).addContent(button).build()

        //val newButton = Button.fromLayoutElement(box.contents[2])
        //newButton.let { Toast.makeText(applicationContext, "toast", Toast.LENGTH_SHORT).show() }

        return Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                Timeline.Builder().addTimelineEntry(
                    TimelineBuilders.TimelineEntry.Builder().setLayout(
                        Layout.Builder().setRoot(
                            box
                        ).build()
                    ).build()
                ).build()
            ).build()
    }


    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)
        Wearable.getDataClient(this).addListener(this)
    }
    override fun onTileLeaveEvent(requestParams: EventBuilders.TileLeaveEvent) {
        super.onTileLeaveEvent(requestParams)
        Wearable.getDataClient(this).removeListener(this)
    }


    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        dataEventBuffer.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { dataItem ->
                    if (dataItem.uri.path?.compareTo(messagePath) == 0) {
                        val dataMap: DataMap = DataMapItem.fromDataItem(dataItem).dataMap
                        // setting message or count here doesn't seem to get back to onTileRequest
                        message = dataMap.getInt("message").toString()
                        LoadAction.Builder()
                        Toast.makeText(this@MyTileService, "message: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        private var message: String = "message"
    }
}