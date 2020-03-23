package com.easy.utils;

import android.content.Context;
import android.os.StrictMode;

import androidx.annotation.Nullable;

import com.easy.utils.blockcanary.BlockCanaryConfig;
import com.github.moduth.blockcanary.BlockCanary;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;


public class EasyUtils {

    public static void init(Context context) {
        ToastUtils.initToast(context);//toast初始化
        CrashReport.initCrashReport(context);//bugly异常上报
        CrashUtils.init(context);//未捕获异常处理

        //打印日志配置
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
                // ( .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("MyLog")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });

        debugInit(context);
    }

    private static void debugInit(Context context) {
        BlockCanary.install(context, new BlockCanaryConfig(context)).start();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }
}
