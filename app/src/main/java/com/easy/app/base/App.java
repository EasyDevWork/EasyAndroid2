package com.easy.app.base;

import com.easy.framework.base.web.protocol.WebProtocolManager;
import com.easy.framework.base.BaseApplication;
import com.easy.net.RetrofitConfig;

public class App extends BaseApplication {

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

    }
}
