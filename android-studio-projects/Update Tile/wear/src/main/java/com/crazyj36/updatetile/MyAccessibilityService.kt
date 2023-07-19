package com.crazyj36.updatetile

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.d("UPDATETILE", "onAccessibilityEvent()")
        if (MyTileService.runTouchEvent) {
            MyTileService.count++
            val path = Path()
            path.moveTo(200f, 200f)
            val gestureBuilder: GestureDescription.Builder = GestureDescription.Builder()
            gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            val gesture = gestureBuilder.build()
            Log.d("UPDATETILE", dispatchGesture(gesture, null, null).toString())
        }

    }
    override fun onInterrupt() {
        Log.d("UPDATETILE", "onInterrupt()")
    }
    protected override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("UPDATETILE", "onServiceConnected()")
    }
}