package com.crazyj36.updatetile

import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicDataBuilders
import androidx.wear.protolayout.material.Text
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
        val TEXT = AppDataKey<DynamicBuilders.DynamicString>("textKey")
    }

    override fun onCreate() {
        super.onCreate()
        val systemTime: DynamicBuilders.DynamicInstant =
            DynamicBuilders.DynamicInstant.
            platformTimeWithSecondsPrecision()
        Timer().schedule(object: TimerTask() {
            override fun run() {
                count++
                LoadAction.Builder().build()
            }
        }, 0, 1000)
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        val state = StateBuilders.State.Builder()
            .addKeyToValueMapping(TEXT,
            DynamicDataBuilders.DynamicDataValue
                .fromString(count.toString()))
        return Futures.immediateFuture(
            TileBuilders.Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setTileTimeline(
                    TimelineBuilders.Timeline.fromLayoutElement(
                        Text.Builder(this,
                            TypeBuilders.StringProp
                                .Builder("--")
                                .setDynamicValue(
                                    DynamicBuilders.DynamicString.from(TEXT)
                                ).build(),
                            StringLayoutConstraint.Builder("00000")
                                .build()
                        ).build()
                    )
                )
                .setState(state.build())
                .build()
        )
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

