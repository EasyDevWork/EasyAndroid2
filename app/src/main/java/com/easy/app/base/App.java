package com.easy.app.base;

import android.util.Log;

import com.easy.apt.lib.JsonConverterFactory;
import com.easy.apt.lib.SharePreference;
import com.easy.framework.base.web.protocol.WebProtocolManager;
import com.easy.net.RetrofitConfig;

public class App extends FlutterApp {

    @Override
    public void onCreate() {
        Log.d("App", "onCreate");
        super.onCreate();
    }

    @Override
    protected void initBaseConfig(RetrofitConfig.Builder builder) {
        builder.baseUrl(AppConstant.BASE_URL).forceCache(true,600);
    }

    @Override
    public void initOnThread() {
        WebProtocolManager.getInstall().addScheme("meetone");
    }

    @Override
    public void initOnMainThread() {
        SharePreference.setConverterFactory(new JsonConverterFactory());
    }

}
