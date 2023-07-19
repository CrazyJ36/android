package com.crazyj36.updatetile

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.crazyj36.updatetile.MyTileService.Companion.count

import com.crazyj36.updatetile.MyTileService.Companion.runTouchEvent

class MyAccessibilityService : AccessibilityService() {

    private val gesture = setupGesture()
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.d("UPDATETILE", "onAccessibilityEvent()")
        if (runTouchEvent) {
            runTouchEvent = false
            for (i in 1..5) {
                count++
                Log.d("UPDATETILE", dispatchGesture(gesture, null, null).toString())
                Thread.sleep(1000)
            }
        }
    }
    private fun setupGesture(): GestureDescription {
        val path = Path()
        path.moveTo(200f, 200f)
        val gestureBuilder: GestureDescription.Builder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))
        return gestureBuilder.build()
    }
    override fun onInterrupt() {
        Log.d("UPDATETILE", "onInterrupt()")
    }

    fun pressScreen() {}
    protected override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("UPDATETILE", "onServiceConnected()")
    }
}