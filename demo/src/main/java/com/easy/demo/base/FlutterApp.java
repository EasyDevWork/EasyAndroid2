package com.easy.demo.base;

import com.easy.demo.ui.flutter.FlutterChannel;
import com.easy.framework.base.BaseApplication;
import com.easy.skin.SkinManager;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public abstract class FlutterApp extends BaseApplication {
    private static FlutterEngine mFlutterEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        initFlutterEngine();
        FlutterChannel.init(mFlutterEngine.getDartExecutor());
    }

    /**
     * 创建可缓存的FlutterEngine
     *
     * @return
     */
    private void initFlutterEngine() {
        // 实例化FlutterEngine对象
        mFlutterEngine = new FlutterEngine(this);
        // 设置初始路由
        mFlutterEngine.getNavigationChannel().setInitialRoute("homePageRouter?{\"name\":\"jjj\"}");
        // 开始执行dart代码来pre-warm FlutterEngine
        mFlutterEngine.getDartExecutor().executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());
        // 缓存FlutterEngine
        FlutterEngineCache.getInstance().put("my_engine_id", mFlutterEngine);
    }

    public static FlutterEngine getFlutterEngine() {
        return mFlutterEngine;
    }

    @Override
    public void initOnMainThread() {
        SkinManager.init(this, "zh");
    }
}
