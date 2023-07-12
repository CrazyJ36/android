package com.crazyj36.wearphonesynctile

import android.widget.Toast
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.LayoutElementBuilders.Text
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val RESOURCES_VERSION = "0"
private val messagePath = "/messagepath"


class MyTileService : TileService(), OnDataChangedListener {
    private var message: String = "tap here to refresh after sending messages from phone."
    private var count: Int = 0

    override fun onTileRequest(requestParams: TileRequest): ListenableFuture<Tile> =
        Futures.immediateFuture(Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder().addTimelineEntry(
                TimelineBuilders.TimelineEntry.Builder().setLayout(
                    Layout.Builder().setRoot(
                        Text.Builder()
                            .setText(count++.toString() + ", " + message)
                            .setModifiers(
                                ModifiersBuilders.Modifiers.Builder()
                                .setClickable(Clickable.Builder()
                                    .setId("clickableId")
                                    .setOnClick(LoadAction.Builder().build())
                                    .build()
                                ).build()
                            ).build()
                        ).build()
                    ).build()
                ).build()
            ).build()
        )

    override fun onTileLeaveEvent(requestParams: EventBuilders.TileLeaveEvent) {
        super.onTileLeaveEvent(requestParams)
        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)
        Wearable.getDataClient(this).addListener(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onResourcesRequest(requestParams: ResourcesRequest) =
        Futures.immediateFuture(Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
        )

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


}