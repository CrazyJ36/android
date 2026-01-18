package com.crazyj36.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.databinding.ObservableArrayList
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : ComponentActivity() {

    data class UrlData(
        val urlLinks: String,
        val urlName: String,
        val renamable: Boolean
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val gson = Gson()

        val preferenceUrlData = getSharedPreferences(
            "preference", MODE_PRIVATE
        ).getString("urlData", "[]")

        val listType = object: TypeToken<ObservableArrayList<UrlData>>() {}.type
        val urlData: ObservableArrayList<UrlData> = gson.fromJson(preferenceUrlData, listType)

        for (entry in urlData) {
            Log.d("APP", "entry link: ${entry.urlLinks}")
        }

        getSharedPreferences("preference", MODE_PRIVATE).edit {
            putString("urlData", gson.toJson(urlData))
            apply()
        }

        setContent {

        }
    }
}