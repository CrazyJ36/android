package com.crazyj36.wearphonesynctile

import android.widget.Toast
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TimelineBuilders.TimelineEntry
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileRequestData
import androidx.wear.tiles.TileService
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.ExperimentalHorologistTilesApi
import com.google.android.horologist.tiles.SuspendingTileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import org.checkerframework.checker.nullness.qual.NonNull

private const val RESOURCES_VERSION = "0"
private val messagePath = "/messagepath"
private var message: String = ""

@OptIn(ExperimentalHorologistApi::class)
class MyTileService : TileService(), OnDataChangedListener {
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) =
        Futures.immediateFuture(Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder().addTimelineEntry(
                TimelineEntry.Builder().setLayout(
                    LayoutElementBuilders.Layout.Builder().setRoot(
                        LayoutElementBuilders.Text.Builder().setText(message)
                            .build()
                        ).build()
                    ).build()
                ).build()
            ).build()
        )

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest) =
        Futures.immediateFuture(
            androidx.wear.tiles.ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
        )

    override fun onCreate() {
        super.onCreate()
        Wearable.getDataClient(this).addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        dataEventBuffer.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { dataItem ->
                    if (dataItem.uri.path?.compareTo(messagePath) == 0) {
                        val dataMap: DataMap = DataMapItem.fromDataItem(dataItem).dataMap
                        message = dataMap.getInt("message").toString()
                        getUpdater(this).requestUpdate(MyTileService::class.java)
                    }
                }
            }
        }
    }
}