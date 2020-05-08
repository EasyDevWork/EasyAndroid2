package com.easy.ndk;

import android.content.Context;
import android.widget.Toast;

public class NDKTools {
    public static Context context;

    static {
        System.loadLibrary("native-lib");
    }

    public static void logMessage(String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static native void callStaticMethod();

    public static native int addNative(int a,int b);
}
