package com.easy.framework.Http;

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
    public List<Interceptor> interceptors;
    public List<Interceptor> networkInterceptors;
    public boolean showLogInterceptor;

    private RetrofitConfig(Builder builder) {
        context = builder.context;
        baseUrl = builder.baseUrl;
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
        private long cacheMaxSize = 10 * 1024 * 1024;
        private String cacheName = "cache";
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

        public Builder readTimeout(long val) {
            readTimeout = val;
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
