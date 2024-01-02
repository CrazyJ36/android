package com.crazyj36.spotifyapitest

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class MainActivity : Activity() {

    private lateinit var globalSpotifyAppRemote: SpotifyAppRemote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SpotifyAppRemote.connect(
            this,
            ConnectionParams.Builder(
                "9730141f6f79463282864c10a0bb008d"
            )
                .setRedirectUri("http://localhost:8080")
                .showAuthView(true)
                .build(),
            object : Connector.ConnectionListener {
                override fun onConnected(connectedSpotifyAppRemote: SpotifyAppRemote) {
                    globalSpotifyAppRemote = connectedSpotifyAppRemote
                    Toast.makeText(
                        this@MainActivity,
                        "Connected to spotify",
                        Toast.LENGTH_SHORT
                    ).show()
                    connectedSpotifyAppRemote.playerApi.play(
                        "spotify:playlist:29Q1fd9uetB3q306eWWsK0?si=8c5024b00d4b4ed2"
                    )
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

        val pauseButton: Button = findViewById(R.id.pauseButton)
        pauseButton.setOnClickListener {
            if (this::globalSpotifyAppRemote.isInitialized) {
                globalSpotifyAppRemote.playerApi.pause()
            }
        }
        val resumeButton: Button = findViewById(R.id.resumeButton)
        resumeButton.setOnClickListener {
            if (this::globalSpotifyAppRemote.isInitialized) {
                globalSpotifyAppRemote.playerApi.resume()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::globalSpotifyAppRemote.isInitialized) {
            Toast.makeText(
                this@MainActivity,
                "Disconnecting Spotify remote.",
                Toast.LENGTH_SHORT
            ).show()
            SpotifyAppRemote.disconnect(globalSpotifyAppRemote)
        }
    }
}
