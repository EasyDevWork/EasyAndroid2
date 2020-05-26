package com.easy.framework.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.framework.BuildConfig;
import com.easy.framework.component.Appcomponent;
import com.easy.framework.component.DaggerAppcomponent;
import com.easy.framework.manager.activity.ActivityManager;
import com.easy.framework.manager.network.NetStateChangeReceiver;
import com.easy.framework.manager.network.NetworkManager;
import com.easy.framework.manager.screen.ScreenManager;
import com.easy.framework.manager.screen.ScreenStateBroadcastReceiver;
import com.easy.framework.module.AppModule;
import com.easy.net.EasyNet;
import com.easy.net.retrofit.RetrofitConfig;
import com.easy.store.base.EasyStore;
import com.easy.utils.EasyUtils;
import com.getkeepsafe.relinker.ReLinker;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.tencent.mmkv.MMKVLogLevel;

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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Logger.d("Application start 初始化");
        initImportant();
        Logger.d("Application initImportant 初始化完成");
        initOnMainThread();
        Logger.d("Application initOnMainThread 初始化完成");
        lazyInit();
        Logger.d("Application  end 初始化");
    }

    protected abstract void initBaseConfig(RetrofitConfig.Builder builder);

    /**
     * app上其他初始化---线程上执行
     */
    public abstract void initOnThread();

    /**
     * app上其他初始化--主线程
     */
    public abstract void initOnMainThread();

    public static BaseApplication getInst() {
        return application;
    }

    public Appcomponent getAppComponent() {
        return appcomponent;
    }

    /**
     * 初始化重要
     */
    private void initImportant() {

        //注册生命周期监听
        registerActivityLifecycleCallbacks(ActivityManager.getInstance().getCallbacks());

        registerBroadcast();

        //路由初始化--用于页面跳转
        initARouter();

        //Dagger初始化--用于注入对象
        appcomponent = DaggerAppcomponent.builder().appModule(new AppModule(this)).build();

        //mmkv初始化
        String dir = getFilesDir().getAbsolutePath() + "/mmkv";
        MMKV.initialize(dir, libName -> ReLinker.loadLibrary(this, libName),
                BuildConfig.DEBUG ? MMKVLogLevel.LevelInfo : MMKVLogLevel.LevelNone);
        Log.d("initImportant", "mmkv root: " + dir);

        //存储数据初始化--用户数据库，文件，SharePreference
        EasyStore.getInstance().init(this);

        //Retrofit初始化--用于接口请求，数据下载
        RetrofitConfig.Builder builder = new RetrofitConfig.Builder(this);
        initBaseConfig(builder);
        EasyNet.init(builder);
        EasyNet.initDownload(EasyStore.getInstance().getDownloadDao());

        //工具初始化--用于吐司，奔溃信息
        EasyUtils.init(this);
    }

    /**
     * 注册网络广播
     */
    private void registerBroadcast() {
        //网络变化广播
        NetStateChangeReceiver changeReceiver = new NetStateChangeReceiver();
        NetworkManager.registerReceiver(changeReceiver, this);

        //锁屏 熄屏，开屏状态变化
        ScreenStateBroadcastReceiver screenStateBroadcastReceiver = new ScreenStateBroadcastReceiver();
        ScreenManager.registerReceiver(screenStateBroadcastReceiver, this);
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.printStackTrace(); // 打印日志的时候打印线程堆栈
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    private void lazyInit() {
        Single.create((SingleOnSubscribe<String>) emitter -> {
            initOnThread();
            emitter.onSuccess("success");
        }).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        lazyInit = disposable;
                    }

                    @Override
                    public void onSuccess(String s) {
                        Logger.d("Application lazyInit 初始化完成");
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

}
