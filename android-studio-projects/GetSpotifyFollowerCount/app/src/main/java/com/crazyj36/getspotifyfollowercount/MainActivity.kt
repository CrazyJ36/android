package com.crazyj36.getspotifyfollowercount

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject


class MainActivity : ComponentActivity(), Callback {
    private var artistInfoString = mutableStateOf("waiting")
    private var okHttpCall: Call? = null
    private var accessToken: String? = null
    private val requestTokenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        accessToken = AuthorizationClient.getResponse(
            it.resultCode, it.data).accessToken
        Log.d("TEST", "got token")
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
                "http://localhost:8888/callback"
            ).setScopes(arrayOf("streaming")).build()
        requestTokenLauncher.launch(
            AuthorizationClient.createLoginActivityIntent(
                this,
                builder
            )
        )

        CoroutineScope(Dispatchers.Main).launch {
            while (accessToken == null) {
                Log.d("TEST", "getting token")
                delay(1000)
            }
        }
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/artists/02UTIVsX3sxUEjvIONrzFe")
            .addHeader(
                "Authorization",
                "Bearer ${accessToken}"
            ).build()
        okHttpCall = okHttpClient.newCall(request)
        okHttpCall!!.enqueue(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (okHttpCall != null) okHttpCall!!.cancel()
    }

    override fun onFailure(call: Call, e: java.io.IOException) {
    }

    override fun onResponse(call: Call, response: Response) {
        artistInfoString.value = JSONObject(response.body!!.string()).toString()
    }
}