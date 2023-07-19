package com.crazyj36.updatetile;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import org.jetbrains.annotations.Nullable;

public class MyAccessibilityService extends AccessibilityService {

   public void onAccessibilityEvent(@Nullable AccessibilityEvent p0) {
      if (p0 != null && p0.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {

      }

   }

   public void onInterrupt() {
      Log.d("UPDATETILE", "onInterrupt()");
   }
   public void pressScreen() {

   }
   protected void onServiceConnected() {
      super.onServiceConnected();
      Log.d("UPDATETILE", "onServiceConnected()");
   }
}
