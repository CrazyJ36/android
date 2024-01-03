package com.crazyj36.getspotifyfollowercount

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException


class MainActivity : ComponentActivity() {
    private val getTokenRequestCode = 0
    private lateinit var token: String

    @Deprecated("Using old method onActivityResult()")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == getTokenRequestCode) {
            val response =
                AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Toast.makeText(
                        applicationContext,
                        "Got auth token. ",
                        Toast.LENGTH_SHORT
                    ).show()
                    token = data.toString()
                }

                AuthorizationResponse.Type.ERROR -> {
                    Toast.makeText(
                        applicationContext,
                        "Error in getting token.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        applicationContext,
                        "Cancelled?",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private lateinit var artistInfoString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder =
            AuthorizationRequest.Builder(
                "19260f6162744ecc8719814edceec27e",
                AuthorizationResponse.Type.TOKEN,
                "http://localhost:8080"
            )
        builder.setScopes(arrayOf("streaming"))

        AuthorizationClient.openLoginActivity(
            this,
            getTokenRequestCode,
            builder.build()
        )

        while (!this::token.isInitialized) {
            CoroutineScope(Dispatchers.IO).launch { delay(100) }
        }

        val okHttpClient = OkHttpClient()
        try {
            val request = Request.Builder()
                .url("https://api.spotify.com/v1/artist/02UTIVsX3sxUEjvIONrzFe")
                .addHeader(
                    "myHeader",
                    "Authorization: Bearer $token"
                )
                .build()
            CoroutineScope(Dispatchers.IO).launch {
                artistInfoString = okHttpClient
                    .newCall(request).execute().body!!.string()
                while (!this@MainActivity::artistInfoString.isInitialized) {
                    delay(100)
                }
            }
        } catch (exception: IOException) {
            Toast.makeText(
                applicationContext,
                exception.localizedMessage,
                Toast.LENGTH_LONG
            ).show()
        }
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = artistInfoString
                )
            }
        }
    }
}