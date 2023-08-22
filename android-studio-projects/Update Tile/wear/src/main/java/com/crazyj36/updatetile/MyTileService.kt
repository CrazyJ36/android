package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicFloat
import androidx.wear.protolayout.expression.PlatformHealthSources
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

    private lateinit  var timer: Timer
    private lateinit var heartRate: DynamicBuilders.DynamicInt32

    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(applicationContext,
                "Need body sensor permissions",
                Toast.LENGTH_SHORT).show()
        } else {
            @NonNull
            val prop = TypeBuilders.FloatProp.Builder(
                0F)
                .setDynamicValue(
                    PlatformHealthSources
                        .heartRateBpm()
                ).build()
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    Log.d("UPDATETILE",
                        "HEART RATE: ${
                            prop.dynamicValue to Int
                        }"
                    )
                }
            }, 0, 1000)
        }
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

