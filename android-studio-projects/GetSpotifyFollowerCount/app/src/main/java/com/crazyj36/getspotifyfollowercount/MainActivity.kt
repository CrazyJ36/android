package com.crazyj36.getspotifyfollowercount

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException


class MainActivity : ComponentActivity() {
    private var artistInfoString = mutableStateOf("waiting")
    private var okHttpCall: Call? = null
    private val requestTokenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val response =
            AuthorizationClient.getResponse(it.resultCode, it.data)
        val okHttpClient = OkHttpClient()
        try {
            val request = Request.Builder()
                .url("https://api.spotify.com/v1/artists/02UTIVsX3sxUEjvIONrzFe")
                .addHeader(
                    "Authorization",
                    "Bearer ${response.accessToken}"
                ).build()
            CoroutineScope(Dispatchers.IO).launch {
                artistInfoString.value =
                    okHttpCall!!.execute().body.toString()
                okHttpCall = okHttpClient.newCall(request)
                runOnUiThread() {
                    Toast.makeText(
                        applicationContext,
                        artistInfoString.value,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (exception: IOException) {
            Toast.makeText(
                applicationContext,
                exception.localizedMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = artistInfoString.value
                )
            }
        }
        val builder =
            AuthorizationRequest.Builder(
                "19260f6162744ecc8719814edceec27e",
                AuthorizationResponse.Type.TOKEN,
                Uri.parse("http://www.crazyj36.rocks/development/web/index.html").toString()
            ).setScopes(arrayOf("streaming")).build()
        requestTokenLauncher.launch(
            AuthorizationClient.createLoginActivityIntent(
                this,
                builder
            )
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        if (okHttpCall != null) okHttpCall!!.cancel()
    }
}