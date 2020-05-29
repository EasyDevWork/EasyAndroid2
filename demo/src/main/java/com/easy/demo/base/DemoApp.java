package com.easy.demo.base;

import android.util.Log;

import com.easy.framework.base.BaseApplication;
import com.easy.net.retrofit.RetrofitConfig;

import java.util.HashMap;
import java.util.Map;

public class DemoApp extends FlutterApp {

    @Override
    public void onCreate() {
        Log.d("App", "onCreate");
        super.onCreate();
    }

    @Override
    protected void initBaseConfig(RetrofitConfig.Builder builder) {
        Map<String, String> hostMap = new HashMap<>();
        hostMap.put("publicHost", "https://publicobject.com");
        hostMap.put("EosHost", "https://mainnet.meet.one");
        builder.baseUrl("https://www.ethte.com")
                .supportCookies(true)
                .supportHostGroup(hostMap)
                .forceCache(true, 600);
    }

    @Override
    public void initOnThread() {

    }

    @Override
    public void initOnMainThread() {

    }
}
