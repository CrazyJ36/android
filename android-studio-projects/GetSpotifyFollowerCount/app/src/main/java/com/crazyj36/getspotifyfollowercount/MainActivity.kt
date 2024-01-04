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
    private val requestTokenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        Log.d("MYLOG", "started activity")
        val accessToken = AuthorizationClient.getResponse(
            it.resultCode, it.data).accessToken
        okHttpCall = OkHttpClient().newCall(
            Request.Builder()
                .url("https://api.spotify.com/v1/artists/02UTIVsX3sxUEjvIONrzFe")
                .addHeader(
                    "Authorization",
                    "Bearer ${accessToken}"
                ).build()
        )
        okHttpCall!!.enqueue(this)
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
                "http://www.crazyj36.rocks/development/web/index.html"
            ).setScopes(arrayOf("streaming"))
        requestTokenLauncher.launch(
            AuthorizationClient.createLoginActivityIntent(
                this,
                builder.build()
            )
        )
    }

    override fun onFailure(call: Call, e: java.io.IOException) {
        Log.d("MYLOG", "failed response")
    }

    override fun onResponse(call: Call, response: Response) {
        Log.d("MYLOG", "got response")
        artistInfoString.value = JSONObject(response.body!!.string()).toString()
    }
    override fun onDestroy() {
        super.onDestroy()
        if (okHttpCall != null) okHttpCall!!.cancel()
    }

}