package com.crazyj36.updatetile

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {
    private var gesture: GestureDescription? = null
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (MyTileService.isTileInView && event?.packageName == "com.crazyj36.updatetile") {
            MyTileService.count++
            Log.d("UPDATETILE", "dispatched: " + dispatchGesture(gesture!!, null, null).toString())
        }
    }
    override fun onInterrupt() {
        Log.d("UPDATETILE", "onInterrupt()")
    }
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("UPDATETILE", "onServiceConnected()")
        buildClickGesture()
    }

    private fun buildClickGesture() {
        val path = Path()
        val gestureBuilder: GestureDescription.Builder = GestureDescription.Builder()
        path.moveTo(300f, 50f)
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))
        gesture = gestureBuilder.build()
    }
}