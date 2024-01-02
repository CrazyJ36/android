package com.crazyj36.spotifyapitest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.crazyj36.spotifyapitest.ui.theme.SpotifyApiTestTheme
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val clientId = "9730141f6f79463282864c10a0bb008d"
    private val redirectUri =
        "http://www.crazyj36.rocks/development/web/crazyj36-daily-spotify-streams/access-sucess-or-failure.html"
    private lateinit var spotifyAppRemote: SpotifyAppRemote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyApiTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(
                        text = "Spotify"
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(
            this@MainActivity,
            "onStart()", Toast.LENGTH_SHORT
        ).show()
        lifecycleScope.launch {
            SpotifyAppRemote.connect(
                application,
                ConnectionParams.Builder(clientId)
                    .setRedirectUri(redirectUri)
                    .showAuthView(true)
                    .build(),
                object : Connector.ConnectionListener {
                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        Toast.makeText(
                            this@MainActivity,
                            "Connected to spotify",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(error: Throwable) {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed: " + error.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::spotifyAppRemote.isInitialized) {
            spotifyAppRemote.let {
                SpotifyAppRemote.disconnect(it)
            }
        } else {
            Toast.makeText(
                this@MainActivity,
                "spotifyAppRemote never got initialized.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
