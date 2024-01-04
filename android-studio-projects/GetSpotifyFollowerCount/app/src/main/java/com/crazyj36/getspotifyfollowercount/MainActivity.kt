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
        Toast.makeText(applicationContext, it.data!!.type, Toast.LENGTH_SHORT).show()
        if (it.data!!.type == AuthorizationResponse.Type.TOKEN.toString()) {
            val response =
                AuthorizationClient.getResponse(it.resultCode, it.data)
            val okHttpClient = OkHttpClient()
            try {
                val request = Request.Builder()
                    .url("https://api.spotify.com/v1/artist/02UTIVsX3sxUEjvIONrzFe")
                    .addHeader(
                        "Authorization",
                        "Bearer ${response.accessToken}"
                    ).build()
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
            Toast.makeText(
                applicationContext,
                artistInfoString,
                Toast.LENGTH_LONG
            ).show()
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
                null
            ).setShowDialog(true)
                .setScopes(arrayOf("user-read-email")).build()
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