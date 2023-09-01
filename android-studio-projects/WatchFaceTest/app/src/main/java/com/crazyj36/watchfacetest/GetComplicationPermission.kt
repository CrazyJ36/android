package com.crazyj36.watchfacetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.wear.activity.ConfirmationActivity

class GetComplicationPermission : ComponentActivity() {
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            /*if (getSharedPreferences(
                    "file_show_complication_warning",
                    Context.MODE_PRIVATE
                ).getBoolean("showComplicationWarning", true)
            ) {*/
            Toast.makeText(this@GetComplicationPermission,
                "permission granted",
                Toast.LENGTH_SHORT).show()
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
                    /*getSharedPreferences("file_show_complication_warning", Context.MODE_PRIVATE)
                    .edit().putBoolean(
                        "showComplicationWarning", false
                    ).apply()*/
            //}
        } else {
            Toast.makeText(this@GetComplicationPermission,
                "permission not granted",
                Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission.launch("com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA")
    }
}