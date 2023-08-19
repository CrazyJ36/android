package com.crazyj36.updatetile

import android.annotation.SuppressLint
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders.Int32Prop
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicInt32
import androidx.wear.protolayout.expression.DynamicDataBuilders
import androidx.wear.protolayout.expression.DynamicDataBuilders.DynamicDataValue
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
    var state = StateBuilders.State.Builder()
    companion object {
        var count = 0
        val KEY_COUNT_NUMBER =
            AppDataKey<DynamicString>(
                "count")
    }
    override fun onCreate() {
        super.onCreate()
        Timer().schedule(object: TimerTask() {
            override fun run() {
                count++
                state = StateBuilders.State.Builder()
                state.addKeyToValueMapping(KEY_COUNT_NUMBER,
                    DynamicDataValue
                        .fromString(count.toString())).build()
                ActionBuilders.LoadAction.Builder().build()
            }

        }, 0, 1000)

    }
    @SuppressLint("MissingPermission")
    public override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest):
            ListenableFuture<Tile> {
        return Futures.immediateFuture(
            Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setFreshnessIntervalMillis(1000)
                .setTileTimeline(
                    TimelineBuilders.Timeline.fromLayoutElement(
                        Text.Builder(
                            this,
                            StringProp.Builder("--")
                                .setDynamicValue(
                                    PlatformHealthSources
                                        .dailyCalories()
                                        .format()
                                ).build(),
                            StringLayoutConstraint
                                .Builder("000000")
                                .build()
                        ).build()
                    )
                ).setState(state.build())
                .build()
        )
    }
    override fun onTileResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest):
            ListenableFuture<ResourceBuilders.Resources> =
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
}

