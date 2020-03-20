package com.easy.framework.base;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.framework.BuildConfig;
import com.easy.net.RetrofitConfig;
import com.easy.net.retrofit.RetrofitUtils;
import com.easy.framework.component.Appcomponent;
import com.easy.framework.component.DaggerAppcomponent;
import com.easy.framework.manager.activity.ActivityManager;
import com.easy.framework.module.AppModule;
import com.easy.store.IProvider.IFrameWork2StoreProvider;
import com.easy.store.base.EasyStore;
import com.easy.utils.CrashUtils;
import com.easy.utils.EasyUtils;
import com.easy.utils.ToastUtils;
import com.github.moduth.blockcanary.BlockCanary;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseApplication extends Application {

    public static BaseApplication application;
    private Appcomponent appcomponent;
    Disposable lazyInit;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initImportant();
        initOnMainThread();
        lazyInit();
    }

    /**
     * 初始化重要
     */
    private void initImportant() {
        initARouter();
        appcomponent = DaggerAppcomponent.builder().appModule(new AppModule(this)).build();
        appcomponent.inject(this);

        RetrofitConfig.Builder builder = new RetrofitConfig.Builder(this);
        initBaseConfig(builder);
        initRetrofit(builder);
        EasyStore.getInstance().init(this);
        //注册生命周期监听
        registerActivityLifecycleCallbacks(ActivityManager.getInstance().getCallbacks());
        initLogger();

        initBugly();
        EasyUtils.init(this);
    }

    private void initBugly() {
        CrashReport.initCrashReport(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initLogger() {
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
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.printStackTrace(); // 打印日志的时候打印线程堆栈
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    public void initRetrofit(RetrofitConfig.Builder builder) {
        RetrofitUtils.get().initRetrofit(builder.build());
    }

    protected abstract void initBaseConfig(RetrofitConfig.Builder builder);

    public static BaseApplication getInst() {
        return application;
    }

    public Appcomponent getAppComponent() {
        return appcomponent;
    }

    private void lazyInit() {
        Single.create((SingleOnSubscribe<String>) emitter -> {
            initOnThread();
            initDebug();
            emitter.onSuccess("success");
        }).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        lazyInit = disposable;
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (lazyInit != null) {
                            lazyInit.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (lazyInit != null) {
                            lazyInit.dispose();
                        }
                    }
                });
    }

    public void initDebug() {
        initStetho();
        initBlockCanary();
        initStrictMode();
    }

    private void initStetho() {
//        Stetho.initializeWithDefaults(this);
    }

    private void initBlockCanary() {
        BlockCanary.install(application.getApplicationContext(), new BlockCanaryConfig(this)).start();
    }

    private void initStrictMode() {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    /**
     * app上其他初始化---线程上执行
     */
    public abstract void initOnThread();

    /**
     * app上其他初始化--主线程
     */
    public abstract void initOnMainThread();
}
