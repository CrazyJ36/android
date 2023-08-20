/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.heatheventtracker.presentation

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.MeasureClient
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType
import androidx.health.services.client.data.MeasureCapabilities
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    var healthClient: HealthServicesClient? = null
    var measureClient: MeasureClient? = null
    var text = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (applicationContext.checkSelfPermission(
                Manifest.permission.ACTIVITY_RECOGNITION) != 
            PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACTIVITY_RECOGNITION
                    ), 0)
        }

        healthClient = HealthServices.getClient(applicationContext)
        measureClient = healthClient!!.measureClient
        measureClient!!.registerMeasureCallback(DataType.Companion
            .STEPS, stepsCallback)

        setContent {
            WearApp()
        }
    }

    private val stepsCallback = object: MeasureCallback {
        override fun onAvailabilityChanged(
            dataType: DeltaDataType<*, *>,
            availability: Availability
        ) {
            if (availability is DataTypeAvailability) {
                Toast.makeText(this@MainActivity,
                    "steps available", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity,
                    "can't get steps at the moment", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onDataReceived(data: DataPointContainer) {
            text = data.toString()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        measureClient!!.unregisterMeasureCallbackAsync(DataType
            .Companion.STEPS, stepsCallback)

    }

    @Composable
    fun WearApp() {
        remember { text }
        var capabilities: MeasureCapabilities? = null
        var supportsSteps: Boolean? = null
        LaunchedEffect(null, false) {
            lifecycleScope.launch {
                capabilities = measureClient!!.getCapabilitiesAsync().await()
                supportsSteps = DataType.STEPS in capabilities!!.supportedDataTypesMeasure
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "text: $text, supportsSteps: $supportsSteps"
            )
        }
    }
}