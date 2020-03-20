package com.easy.net.retrofit;


import com.easy.net.RetrofitConfig;
import com.easy.net.download.DownloadInterceptor;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit工具类
 * 获取Retrofit 默认使用OkHttpClient
 */
public class RetrofitUtils {
    private static RetrofitUtils instance = null;
    Retrofit retrofit;

    private RetrofitUtils() {

    }

    public static RetrofitUtils get() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (instance == null) {
                    instance = new RetrofitUtils();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 获取基础Http请求使用 OkHttpClient
     *
     * @return
     */
    public void initRetrofit(RetrofitConfig retrofitConfig) {
        retrofit = createRetrofit(retrofitConfig);
    }

    public Retrofit createRetrofit(RetrofitConfig retrofitConfig) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(retrofitConfig.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(retrofitConfig.writeTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(retrofitConfig.connectTimeout, TimeUnit.MILLISECONDS);

        //缓存
        File cacheFile = new File(retrofitConfig.context.getCacheDir(), retrofitConfig.cacheName);
        Cache cache = new Cache(cacheFile, retrofitConfig.cacheMaxSize);
        okHttpBuilder.cache(cache);

        //日志拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(message -> Logger.d("okHttp:" + message));
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (retrofitConfig.interceptors != null) {
            for (Interceptor interceptor : retrofitConfig.interceptors) {
                okHttpBuilder.addInterceptor(interceptor);
            }
        }

        if (retrofitConfig.networkInterceptors != null) {
            for (Interceptor interceptor : retrofitConfig.networkInterceptors) {
                okHttpBuilder.addNetworkInterceptor(interceptor);
            }
        }
        if (retrofitConfig.showLogInterceptor) {
            okHttpBuilder.addInterceptor(logInterceptor);
        }

        /**
         * https设置
         * 备注:信任所有证书,不安全有风险
         */
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        OkHttpClient okHttpClient = okHttpBuilder.build();

        return new Retrofit.Builder().client(okHttpClient)
                .baseUrl(retrofitConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    public Retrofit getRetrofitDownload(String baseUrl, DownloadInterceptor downloadInterceptor) {
        OkHttpClient okHttpClient = getOkHttpClientDownload(downloadInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit;
    }

    public OkHttpClient getOkHttpClientDownload(DownloadInterceptor downloadInterceptor) {
        //日志拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(message -> Logger.d("okHttp:" + message));
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        okHttpBuilder.addInterceptor(logInterceptor);
        okHttpBuilder.addInterceptor(downloadInterceptor);

        return okHttpBuilder.build();
    }
}
