package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.Measure
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.MeasureClient
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicDataBuilders
import androidx.wear.protolayout.expression.DynamicDataKey
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
import kotlinx.coroutines.runBlocking
import java.util.Timer
import java.util.TimerTask

const val RESOURCES_VERSION = "1"

class MyTileService : TileService() {

    private lateinit  var timer: Timer
    private var state: StateBuilders.State? = null
    private lateinit var measureClient: MeasureClient
    var heartRate: String = "heartRate"

    companion object {
        val TEXT = AppDataKey<DynamicBuilders
            .DynamicString>("TEXT")
    }
    private val heartRateCallback = object: MeasureCallback {
        override fun onAvailabilityChanged(
            dataType: DeltaDataType<*, *>,
            availability: Availability
        ) {
            if (availability is DataTypeAvailability) {
                Toast.makeText(this@MyTileService,
                    "Heart rate available, please wait.",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MyTileService,
                    "Can't get heart rate on this device",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        override fun onDataReceived(data: DataPointContainer) {
            heartRate = data.getData(
                DataType.HEART_RATE_BPM
            ).last().value.toString()
            state = StateBuilders.State.Builder()
                .addKeyToValueMapping(TEXT, DynamicDataBuilders
                    .DynamicDataValue.fromString(heartRate))
                .build()
        }
    }

    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)
        measureClient = HealthServices
            .getClient(this@MyTileService)
            .measureClient
        measureClient.registerMeasureCallback(DataType
            .Companion.HEART_RATE_BPM, heartRateCallback)
        timer = Timer()
        timer.schedule(object: TimerTask() {
            override fun run() {
                Log.d("UPDATETILE", "HEART RATE: $heartRate")
            }
        }, 0, 1000)
    }

    override fun onTileLeaveEvent(requestParams: EventBuilders.TileLeaveEvent) {
        super.onTileLeaveEvent(requestParams)
        timer.cancel()
        timer.purge()
        runBlocking {
            measureClient.unregisterMeasureCallbackAsync(
                DataType.Companion.HEART_RATE_BPM,
                heartRateCallback
            )
        }
    }

    override fun onTileRemoveEvent(requestParams: EventBuilders.TileRemoveEvent) {
        super.onTileRemoveEvent(requestParams)
        timer.cancel()
        timer.purge()
        runBlocking {
            measureClient.unregisterMeasureCallbackAsync(
                DataType.Companion.HEART_RATE_BPM,
                heartRateCallback
            )
        }
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
                        DynamicBuilders.DynamicString.from(TEXT)
                    ).build(),
                StringLayoutConstraint
                    .Builder("00000")
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
                                    .setText(
                                        "Need body sensor permission to work.")
                                    .build()
                            )
                    ).build()
            )
        } else {
            Futures.immediateFuture(
                Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setFreshnessIntervalMillis(1000)
                    .setState(state!!)
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

