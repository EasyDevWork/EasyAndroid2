package com.easy.common.Interceptor;

import com.easy.framework.Http.INetSwitchInterceptor;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetSwitchInterceptorImp implements INetSwitchInterceptor {

    @Inject
    public NetSwitchInterceptorImp() {
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
