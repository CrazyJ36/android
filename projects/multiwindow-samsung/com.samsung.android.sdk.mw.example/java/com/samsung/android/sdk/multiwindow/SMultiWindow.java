package com.samsung.android.sdk.multiwindow;

import android.app.ActivityThread;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;
import com.samsung.android.sdk.SsdkInterface;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.SsdkVendorCheck;
import com.samsung.android.sdk.multiwindow.SMultiWindowReflator.PackageManager;

public final class SMultiWindow implements SsdkInterface {
    public static final int FREE_STYLE = 2;
    public static final int MULTIWINDOW = 1;
    private static final String TAG = "SMultiWindow";
    private static boolean enableQueried = false;
    private static boolean isFreeStyleEnabled = false;
    private static boolean isMultiWindowEnabled = false;
    private static int mVersionCode = 5;
    private static String mVersionName = "1.2.6";
    private boolean mInsertLog = false;

    public SMultiWindow() {
        initMultiWindowFeature();
    }

    public void initialize(Context arg0) throws SsdkUnsupportedException {
        if (!SsdkVendorCheck.isSamsungDevice()) {
            throw new SsdkUnsupportedException(Build.BRAND + " is not supported.", 0);
        } else if (isMultiWindowEnabled) {
            try {
                if (!this.mInsertLog) {
                    insertLog(arg0);
                }
            } catch (SecurityException e) {
                throw new SecurityException("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission is required.");
            }
        } else {
            throw new SsdkUnsupportedException("The device is not supported.", MULTIWINDOW);
        }
    }

    public boolean isFeatureEnabled(int type) {
        switch (type) {
            case MULTIWINDOW /*1*/:
                return isMultiWindowEnabled;
            case FREE_STYLE /*2*/:
                return isFreeStyleEnabled;
            default:
                return false;
        }
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    public String getVersionName() {
        return mVersionName;
    }

    private void initMultiWindowFeature() {
        try {
            if (!enableQueried) {
                enableQueried = true;
                IPackageManager pm = ActivityThread.getPackageManager();
                if (pm != null) {
                    isMultiWindowEnabled = pm.hasSystemFeature(PackageManager.FEATURE_MULTIWINDOW);
                    isFreeStyleEnabled = pm.hasSystemFeature(PackageManager.FEATURE_MULTIWINDOW_FREESTYLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertLog(Context context) {
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo("com.samsung.android.providers.context", 128).versionCode;
        } catch (NameNotFoundException e) {
            Log.d(TAG, "Could not find ContextProvider");
        }
        Log.d(TAG, "versionCode: " + version);
        if (version <= MULTIWINDOW) {
            Log.d(TAG, "Add com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission");
        } else if (context.checkCallingOrSelfPermission("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY") != 0) {
            throw new SecurityException();
        } else {
            ContentValues cv = new ContentValues();
            String appId = getClass().getPackage().getName();
            String feature = context.getPackageName() + "#" + getVersionCode();
            cv.put("app_id", appId);
            cv.put("feature", feature);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
            broadcastIntent.putExtra("data", cv);
            broadcastIntent.setPackage("com.samsung.android.providers.context");
            context.sendBroadcast(broadcastIntent);
            this.mInsertLog = true;
        }
    }
}
