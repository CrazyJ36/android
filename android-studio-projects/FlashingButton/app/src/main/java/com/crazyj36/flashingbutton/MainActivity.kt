package com.crazyj36.flashingbutton

import com.google.android.material.button.MaterialButton
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    var button: MaterialButton? = null
    var pulseDirection = true
    var buttonStrokeWidth = 1
    var buttonTextSize = 12f
    var count = 0
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button!!.setOnClickListener {
            Toast.makeText(
                applicationContext,
                getString(R.string.toastText),
                Toast.LENGTH_SHORT
            ).show()
        }
        timer.schedule(MyTimerTask(), 0, 16)
    }
    inner class MyTimerTask : TimerTask() {
        override fun run() {
            if (pulseDirection && count < 15) {
                buttonStrokeWidth += 1
                buttonTextSize += 0.3f
                count++
                if (count == 15) pulseDirection = false
            } else if (!pulseDirection && count > 0) {
                buttonStrokeWidth -= 1
                buttonTextSize -= 0.3f
                count--
                if (count == 0) pulseDirection = true
            }
            button!!.strokeWidth = buttonStrokeWidth
            runOnUiThread {
                button!!.textSize = buttonTextSize
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        timer.purge()
    }
}