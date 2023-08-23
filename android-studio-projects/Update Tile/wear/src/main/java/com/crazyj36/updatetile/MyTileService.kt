package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.DynamicDataBuilders
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.material.Text
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

const val RESOURCES_VERSION = "1"

class MyTileService : TileService() {
    companion object {
        val KEY_HEART_RATE =
            AppDataKey<DynamicString>("key_heart_rate")
    }
    public override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<Tile> {
        Log.d("UPDATETILE", "onTileRequest()")
        val state = StateBuilders.State.Builder()
            .addKeyToValueMapping(KEY_HEART_RATE,
                DynamicDataBuilders.DynamicDataValue
                    .fromString("new")
            ).build()

        Log.d("UPDATETILE", DynamicString
            .from(KEY_HEART_RATE).toString()
        )
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Futures.immediateFuture(
                Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setTileTimeline(
                        TimelineBuilders.Timeline
                            .fromLayoutElement(
                                LayoutElementBuilders.Text.Builder()
                                    .setText(
                                        "Need body sensor permission."
                                    )
                                    .build()
                            )
                    ).build()
            )
        } else {
            Futures.immediateFuture(
                Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setFreshnessIntervalMillis(1000)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.fromLayoutElement(
                            Text.Builder(
                                this,
                                StringProp
                                    .Builder("Heart Rate")
                                    .setDynamicValue(
                                        PlatformHealthSources
                                            .heartRateBpm()
                                            .format()
                                    ).build(),
                                StringLayoutConstraint
                                    .Builder("000")
                                    .build()
                            ).build()
                        )
                    )
                    .setState(state)
                    .build()
            )
        }
    }

    override fun onTileResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ):
            ListenableFuture<ResourceBuilders.Resources> =
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
}

