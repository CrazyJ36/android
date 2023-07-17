package com.crazyj36.updatetile

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.semantics.contentDescription
import androidx.glance.semantics.semantics
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.wear.tiles.GlanceTileService
import androidx.glance.wear.tiles.action.ActionCallback
import androidx.glance.wear.tiles.action.actionRunCallback
import androidx.glance.wear.tiles.state.updateWearTileState
import androidx.wear.tiles.ActionBuilders.LoadAction
import kotlinx.coroutines.delay
import java.util.concurrent.Executor

private val prefsCountKey = intPreferencesKey("count")

class MyTileService : GlanceTileService() {
    override val stateDefinition = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val currentCount = currentState<Preferences>()[prefsCountKey] ?: 0
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                GlanceModifier.semantics({ contentDescription = "demo of actionRunCallback" })
        ) {

            Text(
                    text = currentCount.toString(),
                    style = TextStyle(
                            color = ColorProvider(Color.Gray),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                    )
            )

            Row(
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                        text = "+",
                        modifier = GlanceModifier.padding(5.dp).background(Color.Blue),
                        onClick = actionRunCallback<ClickAddAction>()
                )
            }
        }
    }
}
class ClickAddAction : ActionCallback {
    override suspend fun onAction(
            context: Context,
            glanceId: GlanceId
    ) {
        for (i in 1..5) {
            updateWearTileState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                prefs.toMutablePreferences().apply {
                    set(prefsCountKey, (this[prefsCountKey] ?: 0) + 1)
                }
            }
            delay(1000)
        }
    }
}

