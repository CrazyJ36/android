package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.ui.layout.Layout
import androidx.core.app.ActivityCompat
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TimelineBuilders.TimeInterval
import androidx.wear.protolayout.TimelineBuilders.TimelineEntry
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

const val RESOURCES_VERSION = "1"

class MyTileService : TileService() {

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<Tile> {
        val timeline = TimelineBuilders.Timeline.Builder()
        val text = LayoutElementBuilders.Text.Builder()
            .setText("test")
            .build()
        val layout = LayoutElementBuilders.Layout.Builder()
            .setRoot(text)

        timeline.addTimelineEntry(
            TimelineEntry.Builder()
                .setLayout(layout.build())
                .setValidity(TimeInterval.Builder()
                    .setEndMillis(5000).build()
                ).build()
        )
        val tile = Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(timeline.build())
        return Futures.immediateFuture(tile.build())
    }

    override fun onTileResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> =
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .addIdToImageMapping(
                    "button_icon", ResourceBuilders.ImageResource.Builder()
                        .setAndroidResourceByResId(
                            ResourceBuilders.AndroidImageResourceByResId.Builder()
                                .setResourceId(R.drawable.button_icon)
                                .build()
                        )
                        .build()
                )
                .build()
        )
}

