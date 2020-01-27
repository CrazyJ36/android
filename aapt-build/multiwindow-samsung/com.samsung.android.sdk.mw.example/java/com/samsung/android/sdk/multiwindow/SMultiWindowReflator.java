package com.samsung.android.sdk.multiwindow;

import android.util.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class SMultiWindowReflator {
    private HashMap<String, Pair<Object, Method>> mMethodMap = new HashMap();

    public static class Intent {
        public static String EXTRA_WINDOW_DEFAULT_SIZE;
        public static String EXTRA_WINDOW_LAST_SIZE;
        public static String EXTRA_WINDOW_MODE;
        public static String EXTRA_WINDOW_POSITION;
        public static String EXTRA_WINDOW_SCALE;
        static String[] FIELD_NAMES = new String[]{"EXTRA_WINDOW_MODE", "EXTRA_WINDOW_POSITION", "EXTRA_WINDOW_DEFAULT_SIZE", "EXTRA_WINDOW_LAST_SIZE", "EXTRA_WINDOW_SCALE"};

        static {
            int N = FIELD_NAMES.length;
            for (int i = 0; i < N; i++) {
                try {
                    Field src = android.content.Intent.class.getDeclaredField(FIELD_NAMES[i]);
                    Field dst = Intent.class.getField(FIELD_NAMES[i]);
                    dst.set(dst, src.get(src));
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e2) {
                } catch (IllegalAccessException e3) {
                }
            }
        }
    }

    public static class MultiWindowStyle {
        static String[] FIELD_NAMES = new String[]{"TYPE_NORMAL", "TYPE_SPLIT", "TYPE_CASCADE", "ZONE_UNKNOWN", "ZONE_A", "ZONE_B", "ZONE_C", "ZONE_D", "ZONE_E", "ZONE_F", "ZONE_FULL", "OPTION_SCALE"};
        public static int OPTION_SCALE;
        public static int TYPE_CASCADE;
        public static int TYPE_NORMAL;
        public static int TYPE_SPLIT;
        public static int ZONE_A;
        public static int ZONE_B;
        public static int ZONE_C;
        public static int ZONE_D;
        public static int ZONE_E;
        public static int ZONE_F;
        public static int ZONE_FULL;
        public static int ZONE_UNKNOWN;

        static {
            int N = FIELD_NAMES.length;
            for (int i = 0; i < N; i++) {
                try {
                    Field src = com.samsung.android.multiwindow.MultiWindowStyle.class.getDeclaredField(FIELD_NAMES[i]);
                    Field dst = MultiWindowStyle.class.getField(FIELD_NAMES[i]);
                    dst.setInt(dst, src.getInt(src));
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e2) {
                } catch (IllegalAccessException e3) {
                }
            }
        }
    }

    public static class PackageManager {
        public static String FEATURE_MULTIWINDOW;
        public static String FEATURE_MULTIWINDOW_FREESTYLE;
        public static String FEATURE_MULTIWINDOW_FREESTYLE_DOCKING;
        public static String FEATURE_MULTIWINDOW_FREESTYLE_LAUNCH;
        public static String FEATURE_MULTIWINDOW_MINIMIZE;
        public static String FEATURE_MULTIWINDOW_MULTIINSTANCE;
        public static String FEATURE_MULTIWINDOW_PHONE;
        public static String FEATURE_MULTIWINDOW_QUADVIEW;
        public static String FEATURE_MULTIWINDOW_TABLET;
        static String[] FIELD_NAMES = new String[]{"FEATURE_MULTIWINDOW", "FEATURE_MULTIWINDOW_FREESTYLE", "FEATURE_MULTIWINDOW_MINIMIZE", "FEATURE_MULTIWINDOW_QUADVIEW", "FEATURE_MULTIWINDOW_MULTIINSTANCE", "FEATURE_MULTIWINDOW_FREESTYLE_DOCKING", "FEATURE_MULTIWINDOW_FREESTYLE_LAUNCH", "FEATURE_MULTIWINDOW_PHONE", "FEATURE_MULTIWINDOW_TABLET"};

        static {
            int N = FIELD_NAMES.length;
            for (int i = 0; i < N; i++) {
                try {
                    Field src = android.content.pm.PackageManager.class.getDeclaredField(FIELD_NAMES[i]);
                    Field dst = PackageManager.class.getField(FIELD_NAMES[i]);
                    dst.set(dst, src.get(src));
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e2) {
                } catch (IllegalAccessException e3) {
                }
            }
        }
    }

    public static class WindowManagerPolicy {
        static String[] FIELD_NAMES = new String[]{"WINDOW_MODE_MASK", "WINDOW_MODE_NORMAL", "WINDOW_MODE_FREESTYLE", "WINDOW_MODE_OPTION_COMMON_SCALE", "WINDOW_MODE_OPTION_COMMON_PINUP", "WINDOW_MODE_OPTION_COMMON_MINIMIZED", "WINDOW_MODE_OPTION_SPLIT_ZONE_MASK", "WINDOW_MODE_OPTION_SPLIT_ZONE_A", "WINDOW_MODE_OPTION_SPLIT_ZONE_B", "WINDOW_MODE_OPTION_SPLIT_ZONE_C", "WINDOW_MODE_OPTION_SPLIT_ZONE_D", "WINDOW_MODE_OPTION_SPLIT_ZONE_E", "WINDOW_MODE_OPTION_SPLIT_ZONE_F", "WINDOW_MODE_OPTION_SPLIT_ZONE_UNKNOWN", "WINDOW_MODE_OPTION_COMMON_UNIQUEOP_MASK"};
        public static int WINDOW_MODE_FREESTYLE;
        public static int WINDOW_MODE_MASK;
        public static int WINDOW_MODE_NORMAL;
        public static int WINDOW_MODE_OPTION_COMMON_MINIMIZED;
        public static int WINDOW_MODE_OPTION_COMMON_PINUP;
        public static int WINDOW_MODE_OPTION_COMMON_SCALE;
        public static int WINDOW_MODE_OPTION_COMMON_UNIQUEOP_MASK;
        public static int WINDOW_MODE_OPTION_SPLIT_ZONE_A;
        public static int WINDOW_MODE_OPTION_SPLIT_ZONE_B;
        public static int WINDOW_MODE_OPTION_SPLIT_ZONE_C;
        public static int WINDOW_MODE_OPTION_SPLIT_ZONE_D;
        public static int WINDOW_MODE_OPTION_SPLIT_ZONE_E;
        public static int WINDOW_MODE_OPTION_SPLIT_ZONE_F;
        public static int WINDOW_MODE_OPTION_SPLIT_ZONE_MASK;
        public static int WINDOW_MODE_OPTION_SPLIT_ZONE_UNKNOWN;

        static {
            int N = FIELD_NAMES.length;
            for (int i = 0; i < N; i++) {
                try {
                    Field src = android.view.WindowManagerPolicy.class.getDeclaredField(FIELD_NAMES[i]);
                    Field dst = WindowManagerPolicy.class.getField(FIELD_NAMES[i]);
                    dst.setInt(dst, src.getInt(src));
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e2) {
                } catch (IllegalAccessException e3) {
                }
            }
        }
    }

    public void putMethod(Class<?> cls, Object obj, String methodName, Class<?>[] params) {
        try {
            this.mMethodMap.put(methodName, new Pair(obj, cls.getMethod(methodName, params)));
        } catch (NoSuchMethodException e) {
        }
    }

    public boolean checkMethod(String name) {
        if (((Pair) this.mMethodMap.get(name)) != null) {
            return true;
        }
        return false;
    }

    public Object invoke(String name, Object... args) {
        try {
            Pair<Object, Method> pair = (Pair) this.mMethodMap.get(name);
            if (pair != null) {
                return ((Method) pair.second).invoke(pair.first, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invoke(Class<?> cls, Object obj, String methodName, Class<?>[] params, Object... args) {
        try {
            return cls.getMethod(methodName, params).invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
