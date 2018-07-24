package com.samsung.android.sdk.multiwindow;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.samsung.android.sdk.multiwindow.SMultiWindowReflator.Intent;
import com.samsung.android.sdk.multiwindow.SMultiWindowReflator.MultiWindowStyle;
import com.samsung.android.sdk.multiwindow.SMultiWindowReflator.WindowManagerPolicy;

public class SMultiWindowActivity {
    private static final String TAG = "SMultiWindowActivity";
    public static final int ZONE_A = WindowManagerPolicy.WINDOW_MODE_OPTION_SPLIT_ZONE_A;
    public static final int ZONE_B = WindowManagerPolicy.WINDOW_MODE_OPTION_SPLIT_ZONE_B;
    public static final int ZONE_FULL = (ZONE_A | ZONE_B);
    private Context mContext;
    private Rect mDefaultSize;
    private float mDensity;
    private SMultiWindow mMultiWindow = new SMultiWindow();
    private SMultiWindowReflator mMultiWindowReflator = new SMultiWindowReflator();
    private StateChangeListener mStateChangeListener;
    private int mWindowMode;

    public interface StateChangeListener {
        void onModeChanged(boolean z);

        void onSizeChanged(Rect rect);

        void onZoneChanged(int i);
    }

    private boolean checkMode(int mode) {
        return (this.mWindowMode & mode) != 0;
    }

    private boolean checkOption(int options) {
        return (this.mWindowMode & options) != 0;
    }

    private void updateWindowMode() {
        if (this.mMultiWindow.isFeatureEnabled(1)) {
            Object windowMode = this.mMultiWindowReflator.invoke("getWindowMode", (Object[]) null);
            if (windowMode != null) {
                this.mWindowMode = ((Integer) windowMode).intValue();
            }
        }
    }

    private void setWindowMode() {
        if (this.mMultiWindow.isFeatureEnabled(1)) {
            this.mMultiWindowReflator.invoke("setWindowMode", Integer.valueOf(this.mWindowMode), Boolean.valueOf(true));
        }
    }

    private Bundle getWindowInfo() {
        return (Bundle) this.mMultiWindowReflator.invoke("getWindowInfo", (Object[]) null);
    }

    private Rect getLastSize() {
        Rect lastSize = null;
        Bundle info = getWindowInfo();
        if (info != null) {
            lastSize = (Rect) info.getParcelable(Intent.EXTRA_WINDOW_LAST_SIZE);
        }
        return lastSize != null ? lastSize : this.mDefaultSize;
    }

    private Object getMultiPhoneWindowEvent() {
        return this.mMultiWindowReflator.invoke("getMultiPhoneWindowEvent", (Object[]) null);
    }

    public SMultiWindowActivity(Activity activity) {
        Class<?> activityClass = activity.getClass();
        this.mMultiWindowReflator.putMethod(activityClass, activity, "getWindowMode", (Class[]) null);
        this.mMultiWindowReflator.putMethod(activityClass, activity, "setWindowMode", new Class[]{Integer.TYPE, Boolean.TYPE});
        this.mMultiWindowReflator.putMethod(activityClass, activity, "getWindowInfo", (Class[]) null);
        this.mMultiWindowReflator.putMethod(activityClass, activity, "getWindow", (Class[]) null);
        try {
            Class<?> cl = Class.forName("com.samsung.android.multiwindow.MultiWindowStyle");
            this.mMultiWindowReflator.putMethod(activityClass, activity, "getMultiWindowStyle", (Class[]) null);
            this.mMultiWindowReflator.putMethod(activityClass, activity, "setMultiWindowStyle", new Class[]{cl});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Class<?> windowClass = activity.getWindow().getClass();
        this.mMultiWindowReflator.putMethod(windowClass, activity.getWindow(), "getMultiPhoneWindowEvent", (Class[]) null);
        this.mMultiWindowReflator.putMethod(windowClass, activity.getWindow(), "getWindowManager", (Class[]) null);
        this.mMultiWindowReflator.putMethod(windowClass, activity.getWindow(), "getAttributes", (Class[]) null);
        this.mDensity = activity.getResources().getDisplayMetrics().density;
        try {
            Object multiPhoneWindowEvent = getMultiPhoneWindowEvent();
            if (multiPhoneWindowEvent != null) {
                Class<?> multiPhoneWindowEventClass = multiPhoneWindowEvent.getClass();
                this.mMultiWindowReflator.putMethod(multiPhoneWindowEventClass, multiPhoneWindowEvent, "setStateChangeListener", new Class[]{com.samsung.android.sdk.multiwindow.SMultiWindowListener.StateChangeListener.class});
                this.mMultiWindowReflator.putMethod(multiPhoneWindowEventClass, multiPhoneWindowEvent, "minimizeWindow", new Class[]{Integer.TYPE, Boolean.TYPE});
                this.mMultiWindowReflator.putMethod(multiPhoneWindowEventClass, multiPhoneWindowEvent, "multiWindow", new Class[]{Integer.TYPE, Boolean.TYPE});
                this.mMultiWindowReflator.putMethod(multiPhoneWindowEventClass, multiPhoneWindowEvent, "normalWindow", new Class[]{Integer.TYPE});
                this.mMultiWindowReflator.putMethod(multiPhoneWindowEventClass, multiPhoneWindowEvent, "getScaleInfo", (Class[]) null);
            }
        } catch (NoClassDefFoundError e2) {
            e2.printStackTrace();
        }
        Bundle winInfo = getWindowInfo();
        if (winInfo != null) {
            this.mDefaultSize = (Rect) winInfo.getParcelable(Intent.EXTRA_WINDOW_DEFAULT_SIZE);
        }
        try {
            this.mContext = activity;
            insertLogForAPI(TAG);
        } catch (SecurityException e3) {
            throw new SecurityException("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission is required.");
        }
    }

    public boolean isNormalWindow() {
        if (!this.mMultiWindow.isFeatureEnabled(1)) {
            return true;
        }
        updateWindowMode();
        return checkMode(WindowManagerPolicy.WINDOW_MODE_NORMAL);
    }

    public boolean isMultiWindow() {
        if (!this.mMultiWindow.isFeatureEnabled(1)) {
            return false;
        }
        updateWindowMode();
        return checkMode(WindowManagerPolicy.WINDOW_MODE_FREESTYLE);
    }

    public boolean isScaleWindow() {
        if (!this.mMultiWindow.isFeatureEnabled(2)) {
            return false;
        }
        updateWindowMode();
        return checkOption(WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_SCALE);
    }

    public boolean isMinimized() {
        if (!this.mMultiWindow.isFeatureEnabled(2)) {
            return false;
        }
        updateWindowMode();
        if (checkMode(WindowManagerPolicy.WINDOW_MODE_FREESTYLE) && checkOption(WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_MINIMIZED)) {
            return true;
        }
        return false;
    }

    public void normalWindow() {
        if (this.mMultiWindow.isFeatureEnabled(1)) {
            updateWindowMode();
            if (!checkMode(WindowManagerPolicy.WINDOW_MODE_FREESTYLE)) {
                return;
            }
            if (this.mMultiWindowReflator.checkMethod("normalWindow")) {
                this.mMultiWindowReflator.invoke("normalWindow", Integer.valueOf(this.mWindowMode));
                return;
            }
            this.mWindowMode &= WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_UNIQUEOP_MASK ^ -1;
            this.mWindowMode &= WindowManagerPolicy.WINDOW_MODE_OPTION_SPLIT_ZONE_MASK ^ -1;
            this.mWindowMode &= WindowManagerPolicy.WINDOW_MODE_MASK ^ -1;
            this.mWindowMode |= WindowManagerPolicy.WINDOW_MODE_NORMAL;
            setWindowMode();
        }
    }

    public void multiWindow(float scale) {
        if (this.mMultiWindow.isFeatureEnabled(2)) {
            updateWindowMode();
            if ((!checkMode(WindowManagerPolicy.WINDOW_MODE_FREESTYLE) || checkOption(WindowManagerPolicy.WINDOW_MODE_OPTION_SPLIT_ZONE_MASK)) && this.mMultiWindowReflator.checkMethod("setMultiWindowStyle")) {
                try {
                    Class<?> mwClass = Class.forName("com.samsung.android.multiwindow.MultiWindowStyle");
                    if (mwClass != null) {
                        Object obj = mwClass.newInstance();
                        SMultiWindowReflator.invoke(mwClass, obj, "setType", new Class[]{Integer.TYPE}, Integer.valueOf(MultiWindowStyle.TYPE_CASCADE));
                        SMultiWindowReflator.invoke(mwClass, obj, "setOption", new Class[]{Integer.TYPE, Boolean.TYPE}, Integer.valueOf(MultiWindowStyle.OPTION_SCALE), Boolean.valueOf(true));
                        SMultiWindowReflator.invoke(mwClass, obj, "setScale", new Class[]{Float.TYPE}, Float.valueOf(scale));
                        this.mMultiWindowReflator.invoke("setMultiWindowStyle", obj);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public void minimizeWindow() {
        if (this.mMultiWindow.isFeatureEnabled(2)) {
            updateWindowMode();
            if (checkMode(WindowManagerPolicy.WINDOW_MODE_FREESTYLE) && !checkOption(WindowManagerPolicy.WINDOW_MODE_OPTION_SPLIT_ZONE_MASK)) {
                if (this.mMultiWindowReflator.checkMethod("minimizeWindow")) {
                    this.mMultiWindowReflator.invoke("minimizeWindow", Integer.valueOf(this.mWindowMode), Boolean.valueOf(false));
                    return;
                }
                this.mWindowMode &= WindowManagerPolicy.WINDOW_MODE_MASK ^ -1;
                this.mWindowMode |= WindowManagerPolicy.WINDOW_MODE_FREESTYLE;
                this.mWindowMode |= WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_MINIMIZED;
                this.mWindowMode &= WindowManagerPolicy.WINDOW_MODE_OPTION_SPLIT_ZONE_MASK ^ -1;
                setWindowMode();
            }
        }
    }

    public Rect getRectInfo() {
        if (isMultiWindow()) {
            return getLastSize();
        }
        Point fullscreenPoint = new Point();
        ((WindowManager) this.mMultiWindowReflator.invoke("getWindowManager", (Object[]) null)).getDefaultDisplay().getSize(fullscreenPoint);
        if ((((LayoutParams) this.mMultiWindowReflator.invoke("getAttributes", (Object[]) null)).flags & 1024) == 0) {
            return new Rect(0, (int) (25.0f * this.mDensity), fullscreenPoint.x, fullscreenPoint.y);
        }
        return new Rect(0, 0, fullscreenPoint.x, fullscreenPoint.y);
    }

    public int getZoneInfo() {
        updateWindowMode();
        return this.mWindowMode & WindowManagerPolicy.WINDOW_MODE_OPTION_SPLIT_ZONE_MASK;
    }

    public PointF getScaleInfo() {
        if (this.mMultiWindow.isFeatureEnabled(2)) {
            return (PointF) this.mMultiWindowReflator.invoke("getScaleInfo", (Object[]) null);
        }
        return new PointF(1.0f, 1.0f);
    }

    public boolean setStateChangeListener(StateChangeListener listener) {
        if (!this.mMultiWindow.isFeatureEnabled(1) || !this.mMultiWindowReflator.checkMethod("setStateChangeListener")) {
            return false;
        }
        this.mStateChangeListener = listener;
        if (this.mStateChangeListener == null) {
            this.mMultiWindowReflator.invoke("setStateChangeListener", null);
            return true;
        }
        com.samsung.android.sdk.multiwindow.SMultiWindowListener.StateChangeListener stateChangeListener = new com.samsung.android.sdk.multiwindow.SMultiWindowListener.StateChangeListener() {
            public void onModeChanged(boolean isMultiWindow) {
                SMultiWindowActivity.this.mStateChangeListener.onModeChanged(isMultiWindow);
            }

            public void onZoneChanged(int zoneInfo) {
                SMultiWindowActivity.this.mStateChangeListener.onZoneChanged(zoneInfo);
            }

            public void onSizeChanged(Rect rectInfo) {
                SMultiWindowActivity.this.mStateChangeListener.onSizeChanged(rectInfo);
            }
        };
        this.mMultiWindowReflator.invoke("setStateChangeListener", stateChangeListener);
        return true;
    }

    public static android.content.Intent makeMultiWindowIntent(android.content.Intent intent, int zoneInfo) {
        if (intent == null) {
            intent = new android.content.Intent();
        }
        if (new SMultiWindow().isFeatureEnabled(1)) {
            int windowMode;
            intent.addFlags(268435456);
            if (zoneInfo == ZONE_FULL) {
                windowMode = 0 | WindowManagerPolicy.WINDOW_MODE_NORMAL;
            } else {
                windowMode = 0 | (WindowManagerPolicy.WINDOW_MODE_FREESTYLE | zoneInfo);
            }
            intent.putExtra(Intent.EXTRA_WINDOW_MODE, windowMode);
        }
        return intent;
    }

    public static android.content.Intent makeMultiWindowIntent(android.content.Intent intent, float scale) {
        if (intent == null) {
            intent = new android.content.Intent();
        }
        SMultiWindow multiWindow = new SMultiWindow();
        if (multiWindow.isFeatureEnabled(1) && multiWindow.isFeatureEnabled(2)) {
            intent.addFlags(268435456);
            intent.putExtra(Intent.EXTRA_WINDOW_MODE, 0 | ((WindowManagerPolicy.WINDOW_MODE_FREESTYLE | WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_PINUP) | WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_SCALE));
            intent.putExtra(Intent.EXTRA_WINDOW_SCALE, scale);
        }
        return intent;
    }

    private void insertLogForAPI(String apiName) {
        if (this.mContext != null) {
            int version = -1;
            SMultiWindow temp = new SMultiWindow();
            String appId = temp.getClass().getPackage().getName();
            String feature = this.mContext.getPackageName() + "#" + temp.getVersionCode();
            try {
                version = this.mContext.getPackageManager().getPackageInfo("com.samsung.android.providers.context", 128).versionCode;
            } catch (NameNotFoundException e) {
                Log.d("SM_SDK", "Could not find ContextProvider");
            }
            Log.d("SM_SDK", "context framework's  versionCode: " + version);
            if (version <= 1) {
                Log.d("SM_SDK", "Add com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission");
            } else if (this.mContext.checkCallingOrSelfPermission("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY") != 0) {
                throw new SecurityException();
            } else {
                ContentValues cv = new ContentValues();
                cv.put("app_id", appId);
                cv.put("feature", feature);
                cv.put("extra", apiName);
                Log.d(TAG, appId + ", " + feature + ", " + apiName);
                android.content.Intent broadcastIntent = new android.content.Intent();
                broadcastIntent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
                broadcastIntent.putExtra("data", cv);
                broadcastIntent.setPackage("com.samsung.android.providers.context");
                this.mContext.sendBroadcast(broadcastIntent);
            }
        }
    }
}
