package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
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

    companion object {
        //var count = 0
        val heartRate =
            PlatformHealthSources.Keys.HEART_RATE_BPM
    }

    override fun onCreate() {
        super.onCreate()
        /*Timer().schedule(object : TimerTask() {
            override fun run() {
                count++
            }
        }, 0, 1000)*/
        Toast.makeText(this,
            heartRate.toString(),
            Toast.LENGTH_SHORT).show()

    }

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<Tile> {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Futures.immediateFuture(
                Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.fromLayoutElement(
                            Text.Builder(this,
                                StringProp.Builder(
                                    "permission")
                                    .build(),
                            StringLayoutConstraint.Builder("000")
                                .build()
                        ).build()
                        )
                    ).build()
            )
        } else {
            Futures.immediateFuture(
                Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setFreshnessIntervalMillis(20000)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.fromLayoutElement(
                            Text.Builder(
                                this,
                                StringProp.Builder("--")
                                    .setDynamicValue(
                                        PlatformHealthSources
                                            .heartRateBpm()
                                            .format()
                                            .concat(
                                                DynamicBuilders
                                                    .DynamicString
                                                    .constant(
                                                        " bpm"
                                                    )
                                            )
                                    ).build(),
                                StringLayoutConstraint
                                    .Builder("000")
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

