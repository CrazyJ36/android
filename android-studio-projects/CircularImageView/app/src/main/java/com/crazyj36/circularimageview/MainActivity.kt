package com.crazyj36.circularimageview

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity
import androidx.core.view.setMargins
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import java.util.Timer
import java.util.TimerTask

class MainActivity : ComponentActivity() {
    private val tag = "CIRCULAR_IMAGEVIEW"
    private val timer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val shapeableImageView = findViewById<ShapeableImageView>(R.id.shapeableImageView)

        shapeableImageView.setStrokeColorResource(
            android.R.color.holo_blue_dark
        )
        shapeableImageView.shapeAppearanceModel =
            ShapeAppearanceModel.builder(
                applicationContext,
                R.style.CircularShapeModel,
                R.style.CircularShapeModel
            ).build()

        var strokeWidthFloat = 2f
        var shapeableImageViewMargin = 2f
        var upOrDown = true
        var count = 0
        class MyTimerTask : TimerTask() {
            override fun run() {
                if (upOrDown && count < 20) {
                    strokeWidthFloat += 0.3f
                    shapeableImageViewMargin += 0.3f
                    count++
                    if (count == 20) upOrDown = false
                } else if (!upOrDown && count > 0) {
                    strokeWidthFloat -= 0.3f
                    shapeableImageViewMargin -= 0.3f
                    count--
                    if (count == 0) upOrDown = true
                }
                Log.d(tag, "count: $count")

                shapeableImageView.strokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, strokeWidthFloat, resources.displayMetrics
                )
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, shapeableImageViewMargin, resources.displayMetrics
                ).toInt()

                val layoutParamsSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 128f, resources.displayMetrics
                ).toInt()
                val shapeableImageViewLayoutParams = RelativeLayout.LayoutParams(
                    layoutParamsSize,
                    layoutParamsSize
                )
                shapeableImageViewLayoutParams
                    .addRule(RelativeLayout.CENTER_IN_PARENT,
                        RelativeLayout.TRUE
                    )

                val pixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    shapeableImageViewMargin,
                    resources.displayMetrics
                )
                runOnUiThread() {
                    shapeableImageViewLayoutParams.setMargins(
                        pixels.toInt()
                    )
                    shapeableImageView.layoutParams = shapeableImageViewLayoutParams
                }

                shapeableImageView.invalidate()
            }

        }
        timer.schedule(MyTimerTask(), 0, 16)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        timer.purge()
    }
}