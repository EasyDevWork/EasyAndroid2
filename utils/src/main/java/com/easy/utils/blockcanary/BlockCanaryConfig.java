package com.easy.utils.blockcanary;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easy.utils.BuildConfig;
import com.github.moduth.blockcanary.BlockCanaryContext;

public class BlockCanaryConfig extends BlockCanaryContext {

    Context context;

    public BlockCanaryConfig(Context context) {
        this.context = context;
    }

    @Override
    public String provideQualifier() {
        String qualifier = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            qualifier += info.versionCode + "_" + info.versionName + "_YYB";
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("BlockCanaryConfig", "provideQualifier exception", e);
        }
        return qualifier;
    }

    @Override
    public int provideBlockThreshold() {
        return 500;
    }

    @Override
    public boolean displayNotification() {
        return BuildConfig.DEBUG;
    }

    @Override
    public boolean stopWhenDebugging() {
        return false;
    }
}

