package com.easy.ndk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.Toast;

public class NDKTools {
    public static Context context;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("native-dy-lib");
    }

    public static String logMessage(String msg) {
        return msg;
    }

    public static String getVersionName() {
        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return "app -v " + versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "app -v 1.0.0";
    }

    public static void callNullPointerException() throws NullPointerException {
        throw new NullPointerException("MainActivity NullPointerException");
    }

    public static native String callStaticMethod();

    public static native int addNative(int a, int b);

    public static native int getNativeVersion();

    public static native String getJavaVersion();

    public static native void handleExcept() throws IllegalArgumentException;

    public static native String stringFromJNIByDy();

    public static native int addByDy(int a, int b);

    public static native void showAllFiles(String path);

    public static native void parseBitmap(Bitmap bitmap);
}
