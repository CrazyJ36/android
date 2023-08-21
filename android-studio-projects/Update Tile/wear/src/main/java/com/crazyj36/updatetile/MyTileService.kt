package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.layout.Layout
import androidx.core.app.ActivityCompat
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.MeasureClient
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.DynamicDataBuilders.DynamicDataValue
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.material.Chip
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.layouts.PrimaryLayout
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
    val timer = Timer()
    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (ActivityCompat.checkSelfPermission(
                        this@MyTileService,
                        Manifest.permission.BODY_SENSORS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("UPDATETILE",
                        "need body sensor permission")
                } else {
                    val data = TypeBuilders.FloatProp.Builder(
                        0F)
                        .setDynamicValue(
                            PlatformHealthSources
                                .heartRateBpm()
                        ).build()
                    val heartRate = data.dynamicValue

                    Log.d("UPDATETILE", "HEART RATE: $heartRate")

                }
            }

        }, 0, 1000)
    }

    override fun onTileLeaveEvent(requestParams: EventBuilders.TileLeaveEvent) {
        super.onTileLeaveEvent(requestParams)
        timer.cancel()
        timer.purge()
    }

    public override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<Tile> {
        val primaryChip = CompactChip.Builder(
            this,
            "Load",
            ModifiersBuilders.Clickable.Builder()
                .setOnClick(LoadAction.Builder().build())
                .build(),
            requestParams.deviceConfiguration
        ).build()
        val primaryText =
            Text.Builder(
                this,
                StringProp.Builder("--")
                    .setDynamicValue(
                        PlatformHealthSources
                            .heartRateBpm().format()
                    ).build(),
                StringLayoutConstraint
                    .Builder("000")
                    .build()
            ).build()
        val primaryLayout = PrimaryLayout.Builder(requestParams
            .deviceConfiguration)
            .setPrimaryChipContent(primaryChip)
            .setPrimaryLabelTextContent(primaryText)
            .build()
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
                                    .setText("permission")
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
                           primaryLayout
                        )
                    ).build()
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

