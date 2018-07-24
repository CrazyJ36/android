package com.samsung.android.sdk.mw.example;

import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.ResolveInfo;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.samsung.android.sdk.multiwindow.SMultiWindow;
import com.samsung.android.sdk.multiwindow.SMultiWindowActivity;
import com.samsung.android.sdk.multiwindow.SMultiWindowActivity.StateChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiWindowDemoFunction extends ListActivity {
    private static final int MENU_GET_RECTINFO = 3;
    private static final int MENU_GET_SCALEINFO = 5;
    private static final int MENU_GET_ZONEINFO = 4;
    private static final int MENU_IS_MINIMIZED = 11;
    private static final int MENU_IS_MULTI_WINDOW = 2;
    private static final int MENU_IS_NORMAL_WINDOW = 1;
    private static final int MENU_IS_SCALE_WINDOW = 10;
    private static final int MENU_LAUNCH_MULTI_WINDOW_APP = 0;
    private static final int MENU_MAKE_MULTIWINDOW_INTENT = 9;
    private static final int MENU_MINIMIZE_WINDOW = 12;
    private static final int MENU_MULTI_WINDOW = 13;
    private static final int MENU_NORMAL_WINDOW = 6;
    private static final int MENU_RESET_STATE_CHANGE_LISTENER = 8;
    private static final int MENU_SET_STATE_CHANGE_LISTENER = 7;
    private SMultiWindow mMultiWindow = null;
    private SMultiWindowActivity mMultiWindowActivity = null;
    private List<ResolveInfo> mMultiWindowAppList = null;
    boolean msetting;
    boolean mtray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter(this, 17367043, new String[]{"1. Launch MultiWindow Application", "2. Is Normal Window", "3. Is Multi Window", "4. Get RectInfo", "5. Get ZoneInfo", "6. Get ScaleInfo", "7. Normal Window", "8. Set statechange Listener", "9. Reset statechange Listener", "10. Make MultiWindow Intent", "11. Is Scale Window", "12. Is Minimize Window", "13. Miminize Window", "14. Multi Window"}));
        getListView().setTextFilterEnabled(true);
        this.mMultiWindow = new SMultiWindow();
        this.mMultiWindowActivity = new SMultiWindowActivity(this);
        this.mMultiWindowAppList = new ArrayList();
        for (ResolveInfo r : getPackageManager().queryIntentActivities(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER"), 192)) {
            if (!(r.activityInfo == null || r.activityInfo.applicationInfo.metaData == null)) {
                if (r.activityInfo.applicationInfo.metaData.getBoolean("com.sec.android.support.multiwindow") || r.activityInfo.applicationInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindow.enable")) {
                    boolean bUnSupportedMultiWinodw = false;
                    if (r.activityInfo.metaData != null) {
                        String activityWindowStyle = r.activityInfo.metaData.getString("com.sec.android.multiwindow.activity.STYLE");
                        if (activityWindowStyle != null) {
                            ArrayList<String> activityWindowStyles = new ArrayList(Arrays.asList(activityWindowStyle.split("\\|")));
                            if (!activityWindowStyles.isEmpty() && activityWindowStyles.contains("fullscreenOnly")) {
                                bUnSupportedMultiWinodw = true;
                            }
                        }
                    }
                    if (!bUnSupportedMultiWinodw) {
                        this.mMultiWindowAppList.add(r);
                    }
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mMultiWindowActivity.isMinimized()) {
            Toast.makeText(this, "MultiWindow is changed to Minimize-Window", MENU_IS_NORMAL_WINDOW).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.multi_window_example, menu);
        return true;
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case MENU_LAUNCH_MULTI_WINDOW_APP /*0*/:
                displayAppList(position);
                return;
            case MENU_NORMAL_WINDOW /*6*/:
                this.mMultiWindowActivity.normalWindow();
                return;
            case MENU_SET_STATE_CHANGE_LISTENER /*7*/:
                try {
                    if (this.mMultiWindowActivity.setStateChangeListener(new StateChangeListener() {
                        public void onModeChanged(boolean arg0) {
                            if (arg0) {
                                Toast.makeText(MultiWindowDemoFunction.this, "MultiWindow Mode is changed to Multi-Window", MultiWindowDemoFunction.MENU_IS_NORMAL_WINDOW).show();
                            } else {
                                Toast.makeText(MultiWindowDemoFunction.this, "MultiWindow Mode is changed to Normal-Window", MultiWindowDemoFunction.MENU_IS_NORMAL_WINDOW).show();
                            }
                        }

                        public void onZoneChanged(int arg0) {
                            String zoneInfo = "Free zone";
                            if (arg0 == SMultiWindowActivity.ZONE_A) {
                                zoneInfo = "Zone A";
                            } else if (arg0 == SMultiWindowActivity.ZONE_B) {
                                zoneInfo = "Zone B";
                            }
                            Toast.makeText(MultiWindowDemoFunction.this, "Activity zone info is changed to " + zoneInfo, MultiWindowDemoFunction.MENU_IS_NORMAL_WINDOW).show();
                        }

                        public void onSizeChanged(Rect arg0) {
                            Toast.makeText(MultiWindowDemoFunction.this, "Activity size info is changed to " + arg0, MultiWindowDemoFunction.MENU_IS_NORMAL_WINDOW).show();
                        }
                    })) {
                        Toast.makeText(this, "Register state change listener", MENU_IS_NORMAL_WINDOW).show();
                        return;
                    } else {
                        Toast.makeText(this, "Not supported API ", MENU_IS_NORMAL_WINDOW).show();
                        return;
                    }
                } catch (UnsupportedOperationException e) {
                    displayPopup(position);
                    return;
                }
            case MENU_RESET_STATE_CHANGE_LISTENER /*8*/:
                if (this.mMultiWindowActivity.setStateChangeListener(null)) {
                    Toast.makeText(this, "Reset state change listener", MENU_IS_NORMAL_WINDOW).show();
                    return;
                } else {
                    Toast.makeText(this, "Not supported API ", MENU_IS_NORMAL_WINDOW).show();
                    return;
                }
            case MENU_MAKE_MULTIWINDOW_INTENT /*9*/:
                Intent intent = new Intent();
                intent.setClassName("com.android.contacts", "com.android.contacts.activities.PeopleActivity");
                SMultiWindowActivity.makeMultiWindowIntent(intent, 0.6f);
                startActivity(intent);
                return;
            case MENU_MINIMIZE_WINDOW /*12*/:
                this.mMultiWindowActivity.minimizeWindow();
                return;
            case MENU_MULTI_WINDOW /*13*/:
                this.mMultiWindowActivity.multiWindow(0.6f);
                return;
            default:
                displayPopup(position);
                return;
        }
    }

    private void displayPopup(int position) {
        Builder builder = new Builder(this);
        builder.setTitle(getTitle(position));
        builder.setMessage(getDemoResult(position));
        builder.setNeutralButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private String getTitle(int position) {
        switch (position) {
            case MENU_IS_NORMAL_WINDOW /*1*/:
                return "Is Normal Window";
            case MENU_IS_MULTI_WINDOW /*2*/:
                return "Is Multi Window";
            case MENU_GET_RECTINFO /*3*/:
                return "Get Rect Info";
            case MENU_GET_ZONEINFO /*4*/:
                return "Get Zone Info";
            case MENU_GET_SCALEINFO /*5*/:
                return "Get Scale Info";
            case MENU_IS_SCALE_WINDOW /*10*/:
                return "Is Scale Window";
            case MENU_IS_MINIMIZED /*11*/:
                return "Is Minimized";
            default:
                return "MultiWindowDemo";
        }
    }

    private String getDemoResult(int position) {
        if (this.mMultiWindowActivity == null) {
            return null;
        }
        switch (position) {
            case MENU_IS_NORMAL_WINDOW /*1*/:
                return Boolean.toString(this.mMultiWindowActivity.isNormalWindow());
            case MENU_IS_MULTI_WINDOW /*2*/:
                return Boolean.toString(this.mMultiWindowActivity.isMultiWindow());
            case MENU_GET_RECTINFO /*3*/:
                return this.mMultiWindowActivity.getRectInfo().toString();
            case MENU_GET_ZONEINFO /*4*/:
                int zoneInfo = this.mMultiWindowActivity.getZoneInfo();
                if (!this.mMultiWindow.isFeatureEnabled(MENU_IS_NORMAL_WINDOW)) {
                    return "Zone None";
                }
                if (zoneInfo == SMultiWindowActivity.ZONE_A) {
                    return "Zone A";
                }
                if (zoneInfo == SMultiWindowActivity.ZONE_B) {
                    return "Zone B";
                }
                return "Zone None";
            case MENU_GET_SCALEINFO /*5*/:
                if (this.mMultiWindowActivity.getScaleInfo() != null) {
                    return this.mMultiWindowActivity.getScaleInfo().toString();
                }
                return new PointF(1.0f, 1.0f).toString();
            case MENU_IS_SCALE_WINDOW /*10*/:
                return Boolean.toString(this.mMultiWindowActivity.isScaleWindow());
            case MENU_IS_MINIMIZED /*11*/:
                return Boolean.toString(this.mMultiWindowActivity.isMinimized());
            default:
                return "MultiWindowDemo";
        }
    }

    private void displayAppList(int position) {
        ArrayList<String> appListLabels = new ArrayList();
        if (this.mMultiWindowAppList != null) {
            int appListCount = this.mMultiWindowAppList.size();
            for (int i = MENU_LAUNCH_MULTI_WINDOW_APP; i < appListCount; i += MENU_IS_NORMAL_WINDOW) {
                appListLabels.add((String) ((ResolveInfo) this.mMultiWindowAppList.get(i)).loadLabel(getPackageManager()));
            }
        }
        new Builder(this).setTitle(getTitle(position)).setItems((String[]) appListLabels.toArray(new String[MENU_LAUNCH_MULTI_WINDOW_APP]), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MultiWindowDemoFunction.this.displayLaunchType(which);
            }
        }).show();
    }

    private void displayLaunchType(int appPosition) {
        final ArrayList<String> launchTypes = new ArrayList();
        final int selectedApp = appPosition;
        launchTypes.add("Zone A");
        launchTypes.add("Zone B");
        new Builder(this).setTitle("Select Launch Type").setItems((String[]) launchTypes.toArray(new String[MENU_LAUNCH_MULTI_WINDOW_APP]), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String launchType = (String) launchTypes.get(which);
                ResolveInfo selectApp = (ResolveInfo) MultiWindowDemoFunction.this.mMultiWindowAppList.get(selectedApp);
                ComponentInfo selectAppInfo = selectApp.activityInfo != null ? selectApp.activityInfo : selectApp.serviceInfo;
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setFlags(268435456);
                intent.setComponent(new ComponentName(selectAppInfo.packageName, selectAppInfo.name));
                if ("Zone A".equals(launchType)) {
                    SMultiWindowActivity.makeMultiWindowIntent(intent, SMultiWindowActivity.ZONE_A);
                } else if ("Zone B".equals(launchType)) {
                    SMultiWindowActivity.makeMultiWindowIntent(intent, SMultiWindowActivity.ZONE_B);
                } else {
                    return;
                }
                MultiWindowDemoFunction.this.startActivity(intent);
            }
        }).show();
    }
}
