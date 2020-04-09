package com.easy.utils;

import android.os.Looper;

public class ThreadUtils {

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }
}
