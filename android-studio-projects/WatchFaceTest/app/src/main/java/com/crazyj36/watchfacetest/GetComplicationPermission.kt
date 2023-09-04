package com.crazyj36.watchfacetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Text

class GetComplicationPermission : ComponentActivity() {
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            if (getSharedPreferences(
                    "file_show_complication_warning",
                    Context.MODE_PRIVATE
                ).getBoolean("showComplicationWarning", true)
            ) {
                val intent = Intent(
                    this,
                    ConfirmationActivity::class.java
                ).apply {
                    putExtra(
                        ConfirmationActivity
                            .EXTRA_ANIMATION_TYPE, ConfirmationActivity
                            .SUCCESS_ANIMATION
                    )
                    putExtra(
                        ConfirmationActivity
                            .EXTRA_MESSAGE,
                        resources.getString(R.string.complicationWarningText)
                    )
                    putExtra(
                        ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS,
                        3500
                    )
                }
                startActivity(intent)
                getSharedPreferences("file_show_complication_warning", Context.MODE_PRIVATE)
                    .edit().putBoolean(
                        "showComplicationWarning", false
                    ).apply()
                finish()
            }
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = resources.getString(
                            R.string.permissionNotYetGrantedLayoutText
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                item {
                    CompactChip(
                        onClick = {
                            requestPermission.launch(
                                "com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA"
                            )
                        },
                        label = {
                            Text(resources.getString(R.string.allowButtonText))
                        }
                    )
                }
            }
        }
    }
}