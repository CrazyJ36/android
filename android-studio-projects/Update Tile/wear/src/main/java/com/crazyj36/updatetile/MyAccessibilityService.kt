package com.crazyj36.updatetile

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class MyAccessibilityService: AccessibilityService() {
    override fun onCreate() {
        //serviceInfo.flags = AccessibilityServiceInfo.CAPABILITY_CAN_PERFORM_GESTURES
    }
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        p0!!.source?.apply{
            Log.d("UPDATETILE", "onAccessibilityEvent")
            performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }
    override fun onInterrupt() {

    }
    fun pressScreen() {
        val gestureDescriptionBuilder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(500.toFloat(), 1000.toFloat())
        val gestureDescription = gestureDescriptionBuilder.addStroke(GestureDescription.StrokeDescription(path, 10, 10)).build()
        dispatchGesture(gestureDescription, null, null)
        Log.d("UPDATETILE", "pressScreen()")
    }
}