package com.crazyj36.watchfacetest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.wear.activity.ConfirmationActivity

class GetComplicationPermission : ComponentActivity() {
    val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) Log.d("WATCHFACETEST", "granted in registerForActivityResult.")
        else Log.d("WATCHFACETEST", "not granted in registerForActivityResult")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission.launch("com.google.android.wearable.permission.RECEIVE_COMPLICATION_DATA")
                /*if (getSharedPreferences("file_show_complication_warning",
                        Context.MODE_PRIVATE).getBoolean("showComplicationWarning", true)) {*/
                   /* val intent = Intent(
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
                //}*/
        }

}