package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.layouts.PrimaryLayout
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
                getUpdater(this@MyTileService).requestUpdate(MyTileService::class.java)
                count++
            }
        }, 0, 2000)
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        val systemTime = DynamicBuilders.DynamicInstant
            .platformTimeWithSecondsPrecision()
            .toDynamicInstantByteArray()
        val text = LayoutElementBuilders.Text.Builder()
            .setText(systemTime.decodeToString())
            .build()
        val button = CompactChip.Builder(
            this,
            "",
            ModifiersBuilders.Clickable.Builder()
                .setOnClick(LoadAction.Builder().build())
                .build(),
            requestParams.deviceConfiguration)
            .setIconContent("button_icon")
        val primaryLayout = PrimaryLayout
            .Builder(requestParams.deviceConfiguration)
            .setPrimaryLabelTextContent(text)
            .setPrimaryChipContent(
                button.build())
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Futures.immediateFuture(TileBuilders.
            Tile.Builder()
                .setTileTimeline(TimelineBuilders.
                Timeline.fromLayoutElement(
                    LayoutElementBuilders.Text.Builder()
                        .setText("permission needed.")
                        .build()
                    )
                ).build())
        } else {
            Futures.immediateFuture(
                TileBuilders.Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setFreshnessIntervalMillis(2000)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.fromLayoutElement(
                            primaryLayout.build()
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

