package com.crazyj36.updatetile

import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AndroidViewConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.viewinterop.AndroidView
import androidx.glance.wear.tiles.GlanceTileService

class MyTileService: GlanceTileService() {
    companion object {
        var count = 0
    }

    @Composable
    override fun Content() {
       CompositionLocalProvider(
           LocalDensity provides Density(1.0f),
           LocalLayoutDirection provides LayoutDirection.Ltr,
           LocalViewConfiguration provides AndroidViewConfiguration(android.view.ViewConfiguration.get(this@MyTileService)),
           LocalContext provides this@MyTileService
       ) {
           MyView()
       }
    }

    @Composable
    fun MyView() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = { context ->
                    TextView(context).apply {
                        text = context.getString(R.string.androidViewText)
                    }
                }
            )
            AndroidView(
                factory = { context ->
                    android.widget.Button(context).apply {
                        text = context.getString(R.string.buttonText)
                        setOnClickListener {
                            Toast.makeText(context, "view toast", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}
