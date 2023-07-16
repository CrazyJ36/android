package com.crazyj36.updatetile

import android.util.Log
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

private const val RESOURCES_VERSION = "0"
private var counter: Int = 1

class MyTileService : TileService() {
    override fun onTileRequest(requestParams: TileRequest): ListenableFuture<Tile> {
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
        super.onTileEnterEvent(requestParams)

        requestParams.run {
            for (i in 0..10) {
                Log.d("UPDATETILE", "running: $i")
                counter++
                LoadAction.Builder().build()
                Thread.sleep(1000)
            }
        }

    }
}