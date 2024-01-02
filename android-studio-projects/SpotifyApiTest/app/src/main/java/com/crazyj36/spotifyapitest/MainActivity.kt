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
import com.crazyj36.spotifyapitest.ui.theme.SpotifyApiTestTheme
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

class MainActivity : ComponentActivity() {
    private val clientId = "9730141f6f79463282864c10a0bb008d"
    private val redirectUri = "http://www.crazyj36.rocks/development/web/crazyj36-daily-spotify-streams/access-sucess-or-failure.html"
    private lateinit var spotifyAppRemote: SpotifyAppRemote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyApiTestTheme {
                // A surface container using the 'background' color from the theme
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
        val connectionParams = ConnectionParams
            .Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        Toast.makeText(this@MainActivity,
            "connecting to Spotify",
            Toast.LENGTH_SHORT).show()
        SpotifyAppRemote.connect(this,
            connectionParams,
            object: Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {
                    spotifyAppRemote = appRemote
                    Toast.makeText(this@MainActivity,
                        "Connected to spotify",
                        Toast.LENGTH_SHORT).show()
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Toast.makeText(this@MainActivity,
                        "Failed: " + throwable.message,
                        Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun connected() {
        spotifyAppRemote.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:37i9dQZF1DX7K31D69s4M1"
            it.playerApi.play(playlistURI)
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}
