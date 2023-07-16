package com.crazyj36.updatetile

import android.util.Log
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.ResourceBuilders.ImageResource
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.TimelineBuilders.TimelineEntry
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val RESOURCES_VERSION = "0"
private var counter: Int = 1

class MyTileService : TileService() {
    private var tileRequestParams: TileRequest? = null

    override fun onTileRequest(requestParams: TileRequest): ListenableFuture<Tile> {
        tileRequestParams = requestParams
        Log.d("UPDATETILE", "tile reloaded")
        LoadAction.Builder().build()
        val text1: LayoutElementBuilders.Text = LayoutElementBuilders.Text.Builder().setText(counter.toString()).build()
        val button = CompactChip.Builder(
                this@MyTileService, "",
                Clickable.Builder().setId("buttonId").setOnClick(LoadAction.Builder().build()).build(),
                requestParams.deviceConfiguration
        ).setIconContent("button_icon").build()
        val primaryLayout = PrimaryLayout.Builder(requestParams.deviceConfiguration)
                .setPrimaryLabelTextContent(text1)
                .setPrimaryChipContent(button)
                .build()
        val box: LayoutElementBuilders.Box = LayoutElementBuilders.Box.Builder()
                .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
                .setWidth(DimensionBuilders.expand())
                .setHeight(DimensionBuilders.expand())
                .addContent(primaryLayout)
                .build()

        val timeline = Timeline.Builder()
        timeline.addTimelineEntry(
                TimelineEntry.Builder()
                        .setLayout(Layout.Builder()
                                .setRoot(box)
                                .build())
                        .build()
        )
        val tile = Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setTileTimeline(timeline.build())
        return Futures.immediateFuture(tile.build())
    }
    override fun onTileResourcesRequest(requestParams: ResourcesRequest): ListenableFuture<Resources> =
            Futures.immediateFuture(Resources.Builder()
                    .setVersion(RESOURCES_VERSION)
                    .addIdToImageMapping("button_icon", ImageResource.Builder()
                            .setAndroidResourceByResId(
                                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                                            .setResourceId(R.drawable.button_icon)
                                            .build())
                            .build())
                    .build()
            )
    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)  // doesn't call onTileRequest
        Thread() {
            if (tileRequestParams != null) {
                for (i in 1..5) {
                    counter++
                    Log.d("UPDATETILE", "trying to reload")
                    onTileRequest(tileRequestParams!!)
                    LoadAction.Builder().build()
                    Thread.sleep(1000)
                }
            }
        }.start()
    }
}