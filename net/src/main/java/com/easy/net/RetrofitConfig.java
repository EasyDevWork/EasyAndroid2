package com.easy.net;

import android.content.Context;

import java.util.List;

import okhttp3.Interceptor;

public class RetrofitConfig {

    public Context context;
    public String baseUrl;
    public long readTimeout;
    public long writeTimeout;
    public long connectTimeout;
    public long cacheMaxSize;
    public String cacheName;
    public boolean isCache;//是否开启缓存--开启后缓存策略由接口提供是否有缓存标记来决定
    public boolean forceCache;//是否开启无网络使用缓存数据--
    public long cacheTime ;//秒计算
    public List<Interceptor> interceptors;
    public List<Interceptor> networkInterceptors;
    public boolean showLogInterceptor;

    private RetrofitConfig(Builder builder) {
        context = builder.context;
        baseUrl = builder.baseUrl;
        isCache = builder.isCache;
        forceCache = builder.forceCache;
        cacheTime = builder.cacheTime;
        readTimeout = builder.readTimeout;
        writeTimeout = builder.writeTimeout;
        connectTimeout = builder.connectTimeout;
        cacheMaxSize = builder.cacheMaxSize;
        cacheName = builder.cacheName;
        interceptors = builder.interceptors;
        networkInterceptors = builder.networkInterceptors;
        showLogInterceptor = builder.showLogInterceptor;
    }

    public static final class Builder {
        private Context context;
        private String baseUrl;
        private long readTimeout = 15 * 1000;
        private long writeTimeout = 20 * 1000;
        private long connectTimeout = 20 * 1000;
        private long cacheMaxSize = 50 * 1024 * 1024;
        private String cacheName = "httpCache";
        private boolean isCache = true;//是否开启缓存--开启后缓存策略由接口提供是否有缓存标记来决定
        private boolean forceCache = false;//是否开启无网络使用缓存数据
        private long cacheTime = 60;//秒计算
        private List<Interceptor> interceptors;
        private List<Interceptor> networkInterceptors;
        private boolean showLogInterceptor = true;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder baseUrl(String val) {
            baseUrl = val;
            return this;
        }

        public Builder isCache(boolean isCache) {
            this.isCache = isCache;
            return this;
        }

        public Builder forceCache(boolean forceCache, long cacheTime) {
            this.forceCache = forceCache;
            this.cacheTime = cacheTime;
            return this;
        }

        public Builder writeTimeout(long val) {
            writeTimeout = val;
            return this;
        }

        public Builder connectTimeout(long val) {
            connectTimeout = val;
            return this;
        }

        public Builder cacheMaxSize(long val) {
            cacheMaxSize = val;
            return this;
        }

        public Builder cacheName(String val) {
            cacheName = val;
            return this;
        }

        public Builder interceptors(List<Interceptor> val) {
            interceptors = val;
            return this;
        }

        public Builder networkInterceptors(List<Interceptor> val) {
            networkInterceptors = val;
            return this;
        }

        public Builder showLogInterceptor(boolean val) {
            showLogInterceptor = val;
            return this;
        }

        public RetrofitConfig build() {
            return new RetrofitConfig(this);
        }
    }
}
