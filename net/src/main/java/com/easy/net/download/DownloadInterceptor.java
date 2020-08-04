package com.easy.net.download;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * 通过Interceptor回调监听Response进度
 */
public class DownloadInterceptor implements Interceptor {

    private DownloadProgressCallback callback;
    Handler handler;
    public DownloadInterceptor(DownloadProgressCallback callback,Handler handler) {
        this.callback = callback;
        this.handler = handler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder()
                .body(new DownloadResponseBody(response.body(), callback,handler))
                .build();
    }
}
