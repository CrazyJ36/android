package com.crazyj36.getspotifyfollowercount

import android.os.Bundle
import android.util.Log
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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject


class MainActivity : ComponentActivity(), Callback {
    private var artistInfoString = mutableStateOf("waiting")
    private var okHttpCall: Call? = null
    private val requestTokenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        Log.d("GETSPOTIFYFOLLOWERCOUNT", "started login")
        val accessToken = AuthorizationClient.getResponse(
            it.resultCode, it.data).accessToken
        okHttpCall = OkHttpClient().newCall(
            Request.Builder()
                .url("https://api.spotify.com/v1/artists/02UTIVsX3sxUEjvIONrzFe")
                .addHeader(
                    "Authorization",
                    "Bearer $accessToken"
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
                "crazyj36://callback"
            ).setScopes(arrayOf("streaming"))
        requestTokenLauncher.launch(
            AuthorizationClient.createLoginActivityIntent(
                this,
                builder.build()
            )
        )
    }

    override fun onFailure(call: Call, e: java.io.IOException) {
        Log.d("GETSPOTIFYFOLLOWERCOUNT", "failed to get response")
    }

    override fun onResponse(call: Call, response: Response) {
        Log.d("GETSPOTIFYFOLLOWERCOUNT", "got response")
        artistInfoString.value = JSONObject(
            response.body!!.string())
            .getString("name").toString() +
                " has " +
                JSONObject(response.body!!.string())
                    .getJSONObject("followers")
                    .getInt("total").toString() +
                " followers."
    }
    override fun onDestroy() {
        super.onDestroy()
        if (okHttpCall != null) okHttpCall!!.cancel()
    }

}