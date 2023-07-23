package com.crazyj36.updatetile

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("UPDATETILE", "onAccessibilityEvent()")
        Log.d("UPDATETILE", event.toString())
        /*MyTileService.count++
        val path = Path()
        val gestureBuilder: GestureDescription.Builder = GestureDescription.Builder()
        path.moveTo(220f, 200f)
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))
        val gesture = gestureBuilder.build()
        Log.d("UPDATETILE", "dispatched: " + dispatchGesture(gesture, null, null).toString())*/
    }

    override fun onInterrupt() {
        Log.d("UPDATETILE", "onInterrupt()")
    }
}