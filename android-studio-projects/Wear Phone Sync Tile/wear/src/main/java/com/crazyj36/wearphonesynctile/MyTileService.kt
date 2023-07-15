package com.crazyj36.wearphonesynctile

import android.content.Context
import android.widget.Toast
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.ImageResource
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService

private const val RESOURCES_VERSION = "0"
private val messagePath = "/messagepath"
private var message: Int = 0
private val prefFile: String = "file_count_preference"
private val mode: Int = Context.MODE_PRIVATE

@OptIn(ExperimentalHorologistApi::class)
class MyTileService : SuspendingTileService(), OnDataChangedListener {
    override suspend fun resourcesRequest(requestParams: ResourcesRequest):
            ResourceBuilders.Resources = ResourceBuilders.Resources.Builder()
        .setVersion(RESOURCES_VERSION)
        .addIdToImageMapping(
            "button_icon",
            ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.button_icon)
                        .build()
                )
                .build()
        )
        .build()

    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        when (requestParams.currentState.lastClickableId) {
            "buttonId" -> {
                getSharedPreferences(prefFile, mode).edit().putInt("count", getSharedPreferences(prefFile, mode).getInt("count", 1) + 1).apply()
                sendData(getSharedPreferences(prefFile, mode).getInt("count", 1))
                Toast.makeText(this@MyTileService, "clicked", Toast.LENGTH_SHORT).show()
            }
        }
        val deviceParameters = requestParams.deviceConfiguration
        val text1: LayoutElementBuilders.Text = LayoutElementBuilders.Text.Builder().setText(message.toString()).build()
        val button = CompactChip.Builder(
            this@MyTileService, "",
            Clickable.Builder().setId("buttonId").setOnClick(LoadAction.Builder().build()).build(),
            deviceParameters
        ).setIconContent("button_icon").build()
        val primaryLayout = PrimaryLayout.Builder(deviceParameters)
            .setPrimaryLabelTextContent(text1)
            .setPrimaryChipContent(button)
            .build()
        val box: LayoutElementBuilders.Box = LayoutElementBuilders.Box.Builder()
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(primaryLayout)
            .build()
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

    private fun secondaryText(current: String) = LayoutElementBuilders.Text.Builder()
        .setText(current).build()

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
                        Toast.makeText(this@MyTileService, "got data", Toast.LENGTH_SHORT).show()
                        val tempMessage = dataMap.getInt("message")
                        if (tempMessage > getSharedPreferences(prefFile, mode).getInt("count", 1)) {
                            getSharedPreferences(prefFile, mode).edit().putInt("count", tempMessage).apply()
                            message = tempMessage
                            getUpdater(this@MyTileService).requestUpdate(MyTileService::class.java) // only updates if the last update was >= 20 seconds ago
                        } else if (getSharedPreferences(prefFile, mode).getInt("count", 1) > message) {
                            sendData(getSharedPreferences(prefFile, mode).getInt("count", 1))
                            message = getSharedPreferences(prefFile, mode).getInt("count", 1)
                            getUpdater(this@MyTileService).requestUpdate(MyTileService::class.java)
                        }
                    }
                }
            }
        }
    }
    private fun sendData(message: Int) {
        var dataMap = PutDataMapRequest.create(messagePath)
        dataMap.getDataMap().putInt("message", message)
        var request = dataMap.asPutDataRequest()
        request.setUrgent()
        var dataItemTask = Wearable.getDataClient(this@MyTileService).putDataItem(request)
        dataItemTask.addOnSuccessListener(
            object : OnSuccessListener<DataItem?> {
                override fun onSuccess(dataItem: DataItem?) {}
            })
        dataItemTask.addOnFailureListener(
            object : OnFailureListener {
                override fun onFailure(e: Exception) {}
            })
    }
}