package com.crazyj36.updatetile

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.LocalGlanceId
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.wear.tiles.GlanceTileService
import androidx.glance.wear.tiles.state.updateWearTileState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private val prefsCountKey = intPreferencesKey("count")
class MyTileService : GlanceTileService() {
    override val stateDefinition = PreferencesGlanceStateDefinition
    @Composable
    override fun Content() {
        val currentCount = currentState<Preferences>()[prefsCountKey]
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                    text = currentCount.toString(),
                    style = TextStyle(
                            color = ColorProvider(Color.Gray),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                    )
            )
            Button(
                    text = "+1",
                    modifier = GlanceModifier.padding(5.dp).background(Color.Cyan),
                    onClick = {
                        Toast.makeText(applicationContext, "clicked", Toast.LENGTH_SHORT).show()
                    }
            )
            val glanceId = LocalGlanceId.current
            Thread {
                for (i in 1..5) {
                    Log.d("UPDATETILE", "running")
                    MainScope().launch {
                        updateWearTileState(applicationContext, PreferencesGlanceStateDefinition, glanceId) { preferences ->
                            preferences.toMutablePreferences().apply {
                                set(prefsCountKey, (this[prefsCountKey] ?: 0) + 1)
                            }
                        }
                    }
                    Thread.sleep(1000)
                }
            }.start()
        }
    }
}