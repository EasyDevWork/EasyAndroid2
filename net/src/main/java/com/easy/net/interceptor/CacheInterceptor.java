package com.easy.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 无网络状态下智能读取缓存的拦截器
 */
public class CacheInterceptor implements Interceptor {

    public long cacheTime;

    public CacheInterceptor(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + cacheTime)
                .build();
    }
}
