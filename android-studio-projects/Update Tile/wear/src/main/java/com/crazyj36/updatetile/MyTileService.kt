package com.crazyj36.updatetile

import android.annotation.SuppressLint
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.material.Text
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.util.Timer
import java.util.TimerTask

const val RESOURCES_VERSION = "1"

class MyTileService : TileService() {

    override fun onCreate() {
        super.onCreate()
        Timer().schedule(object: TimerTask() {
            override fun run() {
                getUpdater(this@MyTileService)
                    .requestUpdate(MyTileService::class.java)
            }

        }, 0, 21000)
    }
    @SuppressLint("MissingPermission")
    public override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<Tile> =
        Futures.immediateFuture(
            Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setFreshnessIntervalMillis(21000)
                .setTileTimeline(
                    TimelineBuilders.Timeline.fromLayoutElement(
                        Text.Builder(
                            this,
                            StringProp.Builder("--")
                                .setDynamicValue(
                                    PlatformHealthSources
                                        .dailySteps().format()
                                        .concat(DynamicBuilders
                                            .DynamicString
                                            .constant(" bpm"))
                                )
                                .build(),
                            StringLayoutConstraint
                                .Builder("000")
                                .build()
                        ).build()
                    )
                ).build()
        )

    override fun onTileResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> =
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
}

