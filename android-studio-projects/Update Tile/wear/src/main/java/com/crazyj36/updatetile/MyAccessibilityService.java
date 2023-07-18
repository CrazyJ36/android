package com.crazyj36.updatetile;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;

public final class MyAccessibilityService extends AccessibilityService {
   public void onCreate() {
      Log.d("UPDATETILE", "onCreate");
   }

   public void onAccessibilityEvent(@Nullable AccessibilityEvent p0) {
      Log.d("UPDATETILE", "onAccessibilityEvent()");
   }

   public void onInterrupt() {
   }

   public void pressScreen() {
      GestureDescription.Builder gestureDescriptionBuilder = new GestureDescription.Builder();
      Path path = new Path();
      path.moveTo((float)200, (float)400);
      GestureDescription gestureDescription = gestureDescriptionBuilder.addStroke(new GestureDescription.StrokeDescription(path, 1L, 2L)).build();
      this.dispatchGesture(gestureDescription, new GestureResultCallback() {
         @Override
         public void onCompleted(GestureDescription gestureDescription) {
            super.onCompleted(gestureDescription);
            Log.d("UPDATETILE", "completed");
         }

         @Override
         public void onCancelled(GestureDescription gestureDescription) {
            super.onCancelled(gestureDescription);
            Log.d("UPDATETILE", "cancelled");
         }
      }, (Handler) null);
      Log.d("UPDATETILE", "pressScreen()");
   }

   protected void onServiceConnected() {
      super.onServiceConnected();
      Log.d("UPDATETILE", "onServiceConnected()");
   }
}
