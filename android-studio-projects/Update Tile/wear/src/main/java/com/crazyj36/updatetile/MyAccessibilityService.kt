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
        if (event?.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {

        }
    }
    override fun onInterrupt() {
        Log.d("UPDATETILE", "onInterrupt()")
    }
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("UPDATETILE", "onServiceConnected()")
    }
    companion object {
        val instance = MyAccessibilityService()
    }
}