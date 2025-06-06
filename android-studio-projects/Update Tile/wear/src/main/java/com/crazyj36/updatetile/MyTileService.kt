package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.util.Timer
import java.util.TimerTask

const val RESOURCES_VERSION = "1"

class MyTileService : TileService() {
    /* Key:
    namespace = "protolayout"
    key = "Daily Distance"
    */
    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<Tile> {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Futures.immediateFuture(
                Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setFreshnessIntervalMillis(1000)
                    .setTileTimeline(
                            Timeline.fromLayoutElement(
                                LayoutElementBuilders.Text.Builder()
                                    .setText(
                                        TypeBuilders.StringProp
                                            .Builder("Daily Distance")
                                            .setDynamicValue(
                                                PlatformHealthSources
                                                    .dailyDistanceMeters()
                                                    .format()
                                            ).build()
                                    )
                                    .setLayoutConstraintsForDynamicText(
                                        StringLayoutConstraint
                                            .Builder("00000")
                                            .build()
                                    )
                                    .build()
                            )
                    ).build()
            )
        } else {
            Futures.immediateFuture(
                Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setTileTimeline(
                        Timeline
                            .fromLayoutElement(
                                LayoutElementBuilders.Text.Builder()
                                    .setText("Need permission")
                                    .build()
                            )
                    ).build()
            )
        }
    }

    override fun onTileResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ListenableFuture<ResourceBuilders.Resources> =
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
}

