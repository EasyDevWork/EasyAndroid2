package com.easy.utils;

import android.content.Context;

public class EasyUtils {

    public static void init(Context context){
        ToastUtils.initToast(context);
        CrashUtils.init(context);
    }
}
