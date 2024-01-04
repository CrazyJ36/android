package com.crazyj36.getspotifyfollowercount

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationHandler
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
    private var artistInfoString = ""

    private val requestTokenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.data!!.type) {
            AuthorizationResponse.Type.TOKEN.toString() -> {
                Toast.makeText(
                    applicationContext,
                    "Got auth token.",
                    Toast.LENGTH_SHORT
                ).show()
                val okHttpClient = OkHttpClient()
                val data = it.data
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

            AuthorizationResponse.Type.ERROR.toString() -> {
                Toast.makeText(
                    applicationContext,
                    "Error in getting token.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            AuthorizationResponse.Type.CODE.toString() -> {
                Toast.makeText(
                    applicationContext,
                    "Type CODE",
                    Toast.LENGTH_SHORT
                ).show()
            }

            AuthorizationResponse.Type.EMPTY.toString() -> {
                Toast.makeText(
                    applicationContext,
                    "Type EMPTY",
                    Toast.LENGTH_SHORT
                ).show()

            }

            AuthorizationResponse.Type.UNKNOWN.toString() -> {
                Toast.makeText(
                    applicationContext,
                    "Type UNKNOWN",
                    Toast.LENGTH_SHORT
                ).show()
            }
            null -> {
                Toast.makeText(
                    applicationContext,
                    "null data type",
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
            ).setShowDialog(true)
                .setScopes(arrayOf("streaming")).build()
        requestTokenLauncher.launch(
            AuthorizationClient.createLoginActivityIntent(
            this,
            builder
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}