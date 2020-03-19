package com.easy.framework.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easy.framework.BuildConfig;
import com.github.moduth.blockcanary.BlockCanaryContext;

public class BlockCanaryConfig extends BlockCanaryContext {

    Application application;

    public BlockCanaryConfig(Application application) {
        this.application = application;
    }

    @Override
    public String provideQualifier() {
        String qualifier = "";
        try {
            PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
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

