
package com.crazyj36.updatetile

import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.Resources
import androidx.wear.protolayout.ResourceBuilders.ImageResource
import androidx.wear.protolayout.TimelineBuilders.TimeInterval
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.TimelineBuilders.TimelineEntry
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService

private const val RESOURCES_VERSION = "0"

@OptIn(ExperimentalHorologistApi::class)
class MyTileService : SuspendingTileService() {
    val text1: LayoutElementBuilders.Text = LayoutElementBuilders.Text.Builder()
            .setText("timeline entry 1")
            .build()
    val text2: LayoutElementBuilders.Text = LayoutElementBuilders.Text.Builder()
            .setText("timeline entry 2")
            .build()
    val box1: LayoutElementBuilders.Box = LayoutElementBuilders.Box.Builder()
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(text1)
            .build()
    val box2: LayoutElementBuilders.Box = LayoutElementBuilders.Box.Builder()
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(text2)
            .build()

    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        return Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setTileTimeline(Timeline.Builder()
                    .addTimelineEntry(
                            TimelineEntry.Builder()
                                    .setLayout(Layout.Builder()
                                            .setRoot(box1)
                                            .build()
                                    )
                                    .setValidity(TimeInterval.Builder()
                                            .setStartMillis(0)
                                            .setEndMillis(3000)
                                            .build()
                                    ).build()
                    ).addTimelineEntry(
                            TimelineEntry.Builder()
                                    .setLayout(Layout.Builder()
                                            .setRoot(box2)
                                            .build())
                                    .setValidity(TimeInterval.Builder()
                                            .setStartMillis(3000)
                                            .setEndMillis(6000)
                                            .build()
                                    ).build()
                    ).build()
                ).build()
    }
    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): Resources =
            Resources.Builder()
                    .setVersion(RESOURCES_VERSION)
                    // sends "button_icon" to onTileRequest().button.setIconContent
                    .addIdToImageMapping("button_icon", ImageResource.Builder()
                            .setAndroidResourceByResId(
                                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                                            .setResourceId(R.drawable.button_icon)
                                            .build())
                            .build())
                    .build()
}