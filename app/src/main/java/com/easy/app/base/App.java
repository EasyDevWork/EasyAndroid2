package com.easy.app.base;

import android.os.Debug;

import com.easy.apt.lib.JsonConverterFactory;
import com.easy.apt.lib.SharePreference;
import com.easy.framework.base.web.protocol.WebProtocolManager;
import com.easy.net.RetrofitConfig;

public class App extends FlutterApp {

    @Override
    public void onCreate() {
        Debug.startMethodTracing(getCacheDir() + "/time.trace", 1024 * 1024 * 1024);
        super.onCreate();
    }

    @Override
    protected void initBaseConfig(RetrofitConfig.Builder builder) {
        builder.baseUrl(AppConstant.BASE_URL);
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
