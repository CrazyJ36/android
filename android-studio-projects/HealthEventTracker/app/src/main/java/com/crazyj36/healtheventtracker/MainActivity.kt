package com.crazyj36.healtheventtracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.MeasureClient
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    private lateinit var measureClient: MeasureClient
    private lateinit var heartRateCallback: MeasureCallback
    var showAvailability = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            measureClient.unregisterMeasureCallbackAsync(
                DataType
                    .Companion.HEART_RATE_BPM, heartRateCallback
            )
        }
    }
    @Composable
    fun WearApp() {
        var text by remember { mutableStateOf("") }
        val heartRateCallback = object: MeasureCallback {
            override fun onAvailabilityChanged(
                dataType: DeltaDataType<*, *>,
                availability: Availability
            ) {
                if (showAvailability) {
                    showAvailability = false
                    if (availability is DataTypeAvailability) {
                        Toast.makeText(
                            applicationContext,
                            "Heart rate available, loading...",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Can't get heart rate from device.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            override fun onDataReceived(data: DataPointContainer) {
                val heartRate = data.getData(DataType.HEART_RATE_BPM)
                    .last().value.toString()
                if (heartRate != "0.0") {
                    text = heartRate
                }
            }
        }
        if (applicationContext.checkSelfPermission(
                Manifest.permission.BODY_SENSORS) !=
            PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.BODY_SENSORS
                ), 0
            )
        }
        measureClient = HealthServices.getClient(
            applicationContext).measureClient
        measureClient.registerMeasureCallback(DataType.Companion
            .HEART_RATE_BPM, heartRateCallback)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "heart rate: $text"
            )
        }
    }
}