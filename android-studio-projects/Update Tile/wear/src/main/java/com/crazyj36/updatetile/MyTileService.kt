package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.material.Text
import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.util.Timer
import java.util.TimerTask

const val RESOURCES_VERSION = "1"

class MyTileService: TileService() {
    companion object {
        var count = 0
    }

    override fun onCreate() {
        super.onCreate()
        Timer().schedule(object: TimerTask() {
            override fun run() {
                androidx.wear.protolayout.
                ActionBuilders.LoadAction.Builder().build()
            }
        }, 0, 2000)
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Futures.immediateFuture(TileBuilders.
            Tile.Builder()
                .setTileTimeline(TimelineBuilders.
                Timeline.fromLayoutElement(
                    Text.Builder(
                        this,
                        "permission"
                    ).build()
                ))
                .build())
        } else {
            Futures.immediateFuture(
                TileBuilders.Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setFreshnessIntervalMillis(2000)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.fromLayoutElement(
                            Text.Builder(
                                this,
                                TypeBuilders.StringProp
                                    .Builder("--")
                                    .setDynamicValue(
                                        PlatformHealthSources
                                            .heartRateBpm()
                                            .format()
                                    ).build(),
                                StringLayoutConstraint.Builder(
                                    "000")
                                    .build()
                            ).build()
                        )
                    ).build()
            )
        }
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

