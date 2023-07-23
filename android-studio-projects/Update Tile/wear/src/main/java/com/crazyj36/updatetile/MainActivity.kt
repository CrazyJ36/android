package com.crazyj36.updatetile

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val myTileService = MyTileService

        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AndroidView(
                    factory = { context ->
                        TextView(context).apply {
                            text = "Android View"
                        }
                    }
                )
                AndroidView(
                    factory = { context ->
                        Button(context).apply {
                            text = "Button"
                            setOnClickListener {
                                //Toast.makeText(context, "view toast", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }
        }
    }
}