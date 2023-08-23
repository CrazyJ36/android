package com.crazyj36.updatetile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.MeasureClient
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType
import androidx.health.services.client.unregisterMeasureCallback
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.StateBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.DynamicDataBuilders
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

const val RESOURCES_VERSION = "1"

class MyTileService : TileService() {

    companion object {
        private lateinit var measureClient: MeasureClient
        private lateinit var timer: Timer
        private var heartRate: String = "heartRate"
        private var systemTime: String = "systemTime"
        private var KEY_HEART_RATE = AppDataKey<DynamicString>(
            "KEY_HEART_RATE")
        private var KEY_SYSTEM_TIME = AppDataKey<DynamicString>(
            "KEY_SYSTEM_TIME"
        )
        private var state = StateBuilders.State.Builder()
        private val date = Date()
        private val simpleDateFormat = SimpleDateFormat("h:mm",
            Locale.US)
        private lateinit var stringProp: StringProp
    }

    override fun onCreate() {
        super.onCreate()
        measureClient = HealthServices
            .getClient(this@MyTileService)
            .measureClient
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            stringProp = StringProp.Builder("Heart Rate")
                .setDynamicValue(
                    PlatformHealthSources.heartRateBpm()
                        .format()
                ).build()
        }
    }

    private val heartRateCallback = object : MeasureCallback {
        override fun onAvailabilityChanged(
            dataType: DeltaDataType<*, *>,
            availability: Availability
        ) {
            if (availability is DataTypeAvailability) {
                Toast.makeText(
                    this@MyTileService,
                    "Heart rate sensor available, please wait...",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@MyTileService,
                    "Heart rate sensor unavailable.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onDataReceived(data: DataPointContainer) {
            /*heartRate = data.getData(
                DataType.HEART_RATE_BPM
            ).last().value.toString()
            Log.d("UPDATETILE", "HEART RATE: $heartRate")
            state.addKeyToValueMapping(
                KEY_HEART_RATE,
                DynamicDataBuilders.DynamicDataValue
                    .fromString(heartRate)
            )*/
        }
    }

    override fun onTileEnterEvent(
        requestParams:
        EventBuilders.TileEnterEvent
    ) {
        super.onTileEnterEvent(requestParams)
        measureClient.registerMeasureCallback(
            DataType
                .Companion.HEART_RATE_BPM, heartRateCallback
        )

        timer = Timer()
        timer.schedule(object: TimerTask() {
            @SuppressLint("RestrictedApi")
            override fun run(){
                Log.d("UPDATETILE", stringProp
                    .dynamicValue.toString()
                )
                state.addKeyToValueMapping(KEY_HEART_RATE,
                    DynamicDataBuilders.DynamicDataValue
                        .fromString(stringProp
                            .dynamicValue.toString())
                )

                systemTime = simpleDateFormat.format(date)
                Log.d("UPDATETILE", "Time: $systemTime")
                state.addKeyToValueMapping(KEY_SYSTEM_TIME,
                    DynamicDataBuilders.DynamicDataValue
                        .fromString(systemTime)
                )
            }
        }, 0, 1000)
    }

    override fun onTileLeaveEvent(
        requestParams: EventBuilders.TileLeaveEvent) {
        super.onTileLeaveEvent(requestParams)
        runBlocking {
            measureClient.unregisterMeasureCallback(
                DataType.Companion
                    .HEART_RATE_BPM, heartRateCallback
            )
        }
        timer.cancel()
        timer.purge()
    }

    override fun onTileRemoveEvent(
        requestParams: EventBuilders.TileRemoveEvent) {
        super.onTileRemoveEvent(requestParams)
        runBlocking {
            measureClient.unregisterMeasureCallback(
                DataType.Companion.HEART_RATE_BPM,
                heartRateCallback
            )
        }
        timer.cancel()
        timer.purge()
    }

    public override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<Tile> {
        Log.d("UPDATETILE", "onTileRequest()")
        val primaryChip = CompactChip.Builder(
            this,
            "Load",
            ModifiersBuilders.Clickable.Builder()
                .setOnClick(LoadAction.Builder().build())
                .build(),
            requestParams.deviceConfiguration
        ).build()

        val heartRateText =
            Text.Builder(
                this,
                stringProp,
                StringLayoutConstraint
                    .Builder("000")
                    .build()
            )
        val systemTimeText =
            Text.Builder(
                this,
                StringProp.Builder("Time")
                    .setDynamicValue(DynamicString.from(
                            KEY_SYSTEM_TIME
                        )
                    ).build(),
                StringLayoutConstraint
                    .Builder("000000000000000000000000")
                    .build()
            )
        val primaryLayout = PrimaryLayout.Builder(
            requestParams
                .deviceConfiguration
        )
            .setPrimaryChipContent(primaryChip)
            .setPrimaryLabelTextContent(systemTimeText.build())
            .setSecondaryLabelTextContent(heartRateText.build())
        state.addKeyToValueMapping(KEY_HEART_RATE,
            DynamicDataBuilders.DynamicDataValue
                .fromString(stringProp
                    .dynamicValue.toString())
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
                                        "Need body sensor permission to work."
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
                    .setState(state.build())
                    .setTileTimeline(
                        TimelineBuilders.Timeline.fromLayoutElement(
                            primaryLayout.build()
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

