package com.crazyj36.spotifyapitest

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import java.util.Timer
import java.util.TimerTask

class MainActivity : Activity() {

    private var globalSpotifyAppRemote: SpotifyAppRemote? = null
    private var isPaused: Boolean? = null
    private var timer: Timer? = null
    private lateinit var pauseButton: Button
    private lateinit var resumeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pauseButton: Button = findViewById(R.id.pauseButton)
        val resumeButton: Button = findViewById(R.id.resumeButton)
        pauseButton.isEnabled = false
        resumeButton.isEnabled = false

        pauseButton.setOnClickListener {
            if (globalSpotifyAppRemote != null) {
                if (isPaused != null) {
                    if (!isPaused!!) {
                        globalSpotifyAppRemote!!.playerApi.pause()
                    }
                }
            }
        }
        resumeButton.setOnClickListener {
            if (globalSpotifyAppRemote != null) {
                if (isPaused != null) {
                    if (isPaused!!) {
                        globalSpotifyAppRemote!!.playerApi.resume()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
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
                        "Connected Spotify App Remote",
                        Toast.LENGTH_SHORT
                    ).show()
                    connectedSpotifyAppRemote.playerApi.play(
                        "spotify:playlist:29Q1fd9uetB3q306eWWsK0?si=8c5024b00d4b4ed2"
                    )
                    globalSpotifyAppRemote!!.playerApi
                        .subscribeToPlayerState()
                        .setEventCallback {
                            isPaused = it.isPaused
                        }
                }

                override fun onFailure(error: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed To Connect Spotify App Remote:\n" + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                Log.d("SPOTIFYAPITEST", "timer running")
                if (this@MainActivity::pauseButton.isInitialized &&
                    this@MainActivity::resumeButton.isInitialized
                ) {
                    if (isPaused!!) {
                        runOnUiThread {
                            pauseButton.isEnabled = false
                            resumeButton.isEnabled = true
                        }
                    } else {
                        runOnUiThread {
                            pauseButton.isEnabled = true
                            resumeButton.isEnabled = false
                        }
                    }
                }
            }
        }, 0, 10)
    }

    override fun onPause() {
        super.onPause()
        if (globalSpotifyAppRemote != null) {
            Toast.makeText(
                this@MainActivity,
                "Disconnecting Spotify Remote.",
                Toast.LENGTH_SHORT
            ).show()
            SpotifyAppRemote.disconnect(globalSpotifyAppRemote)
            globalSpotifyAppRemote = null
        }
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
            timer = null
        }
        if (isPaused != null) isPaused = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (globalSpotifyAppRemote != null) {
            Toast.makeText(
                this@MainActivity,
                "Disconnecting Spotify Remote.",
                Toast.LENGTH_SHORT
            ).show()
            SpotifyAppRemote.disconnect(globalSpotifyAppRemote)
            globalSpotifyAppRemote = null
        }
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
            timer = null
        }
        if (isPaused != null) isPaused = null
    }
}
