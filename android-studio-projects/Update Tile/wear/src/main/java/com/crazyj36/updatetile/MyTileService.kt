package com.crazyj36.updatetile

import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicInstant
import androidx.wear.protolayout.expression.DynamicDataBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.util.Timer
import java.util.TimerTask

const val RESOURCES_VERSION = "1"

class MyTileService : TileService() {

    companion object {
        var count = 0
        val state = StateBuilders.State.Builder()
        val TEXT = AppDataKey<DynamicBuilders.DynamicString>("text")
    }

    override fun onCreate() {
        super.onCreate()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                count++
                state.addKeyToValueMapping(
                    TEXT,
                    DynamicDataBuilders.DynamicDataValue.fromString(
                        count.toString()
                    )
                ).build()
            }
        }, 0, 1000)
    }

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<Tile> {
        return Futures.immediateFuture(
            Tile.Builder()
                .setResourcesVersion(RESOURCES_VERSION)
                .setTileTimeline(
                    TimelineBuilders.Timeline.fromLayoutElement(
                        LayoutElementBuilders
                            .Text.Builder()
                            .setText(
                                StringProp.Builder("--")
                                    .setDynamicValue(
                                        DynamicBuilders
                                            .DynamicString
                                            .from(TEXT)
                                    ).build()
                            ).setLayoutConstraintsForDynamicText(
                                StringLayoutConstraint
                                    .Builder("000")
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

