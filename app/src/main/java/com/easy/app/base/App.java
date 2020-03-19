package com.easy.app.base;

import com.easy.common.Interceptor.HeaderInterceptorImp;
import com.easy.common.Interceptor.NetSwitchInterceptorImp;
import com.easy.common.Interceptor.NetWorkInterceptorImp;
import com.easy.common.base.CommonApplication;
import com.easy.common.manager.protocol.WebProtocolManager;
import com.easy.framework.Http.RetrofitConfig;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

public class App extends CommonApplication {

    @Override
    protected void initBaseConfig(RetrofitConfig.Builder builder) {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderInterceptorImp());
        interceptors.add(new NetSwitchInterceptorImp());
        List<Interceptor> netWorkInterceptors = new ArrayList<>();
        netWorkInterceptors.add(new NetWorkInterceptorImp());
        builder.baseUrl(AppConstant.BASE_URL)
                .interceptors(interceptors)
                .networkInterceptors(netWorkInterceptors);
    }

    @Override
    public void initOnThread() {
        WebProtocolManager.getInstall().addScheme("meetone");
    }

    @Override
    public void initOnMainThread() {

    }
}
