package com.crazyj36.getspotifyfollowercount

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class MainActivity : ComponentActivity() {

    private var token = ""
    private val loginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        token = result.data.toString()
        Toast.makeText(
            this@MainActivity,
            "got result: $token",
            Toast.LENGTH_LONG
        ).show()

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
        val activity = AuthorizationClient.openLoginActivity(
            this,
            0,
            builder.build()
        )
        val intent = Intent(
            this@MainActivity,
            activity::class.java
        )
        loginLauncher.launch(intent)

        /*        val okHttpClient = OkHttpClient()
                try {
                    val request = Request.Builder()
                        .url("https://api.spotify.com/v1/artist/02UTIVsX3sxUEjvIONrzFe")
                        .addHeader("myHeader","Authorization: Bearer BQCbnwqEc7zFZ9mFfQ7v8PB3GJi79lfpzNYB8hRkZ3Q2GOqms8pN4Xa5ZADkKXSSMfHvZ4ipJLxcZklNypnQ_WIVRHfoO0ACFUbIUmnlE0ZjR0toGgU")
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
        */
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                artistInfoString = "TEMPORARY!!!!"
                Text(
                    text = artistInfoString
                )
            }
        }
    }
}