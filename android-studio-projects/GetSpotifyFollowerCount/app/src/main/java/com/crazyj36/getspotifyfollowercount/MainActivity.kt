package com.crazyj36.getspotifyfollowercount

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException


class MainActivity : ComponentActivity() {
    private val getTokenRequestCode = 0
    private var artistInfoString = ""

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
                    val okHttpClient = OkHttpClient()
                    try {
                        val request = Request.Builder()
                            .url("https://api.spotify.com/v1/artist/02UTIVsX3sxUEjvIONrzFe")
                            .addHeader(
                                "myHeader",
                                "Authorization: Bearer $data"
                            )
                            .build()
                        CoroutineScope(Dispatchers.IO).launch {
                            artistInfoString = okHttpClient
                                .newCall(request).execute().body!!.string()
                        }
                    } catch (exception: IOException) {
                        Toast.makeText(
                            applicationContext,
                            exception.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                AuthorizationResponse.Type.ERROR -> {
                    Toast.makeText(
                        applicationContext,
                        "Error in getting token.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                AuthorizationResponse.Type.CODE -> {
                    Toast.makeText(applicationContext,
                        "Type CODE",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                AuthorizationResponse.Type.EMPTY -> {
                    Toast.makeText(applicationContext,
                        "Type EMPTY",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                AuthorizationResponse.Type.UNKNOWN -> {
                    Toast.makeText(applicationContext,
                        "Type UNKNOWN",
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            remember { mutableStateOf(artistInfoString) }
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = artistInfoString
                )
            }
        }
        val builder =
            AuthorizationRequest.Builder(
                "19260f6162744ecc8719814edceec27e",
                AuthorizationResponse.Type.TOKEN,
                "http://localhost:8080"
            )
        builder.setScopes(arrayOf("streaming"))

        AuthorizationClient.openLoginActivity(
            this@MainActivity,
            getTokenRequestCode,
            builder.build()
        )
    }
}