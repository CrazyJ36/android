package com.crazyj36.getspotifyfollowercount

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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONException
import org.json.JSONObject


class MainActivity : ComponentActivity(), Callback {
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
            okHttpCall = okHttpClient.newCall(request)
            okHttpCall!!.enqueue(this)
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
            ).setScopes(arrayOf("streaming"))
                .setShowDialog(true).build()
        requestTokenLauncher.launch(
            AuthorizationClient.createLoginActivityIntent(
                this,
                builder
            )
        )
    }

    override fun onFailure(call: Call, e: java.io.IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
        try {
            val jsonObject =
                JSONObject(response.body!!.string())
            Toast.makeText(applicationContext, jsonObject.toString(),
                Toast.LENGTH_SHORT).show()
            artistInfoString.value = jsonObject.toString()
        } catch (jsonException: JSONException) {
            Toast.makeText(applicationContext,
                "Json Exception in response",
                Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (okHttpCall != null) okHttpCall!!.cancel()
    }
}