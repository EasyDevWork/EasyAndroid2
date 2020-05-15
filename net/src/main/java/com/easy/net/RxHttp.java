package com.easy.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.easy.net.api.HttpApi;
import com.easy.net.callback.HttpCallback;
import com.easy.net.callback.UploadCallback;
import com.easy.net.cancel.RequestManagerImpl;
import com.easy.net.download.UploadRequestBody;
import com.easy.net.observer.HttpObservable;
import com.easy.net.retrofit.Method;
import com.easy.net.retrofit.RetrofitUtils;
import com.easy.net.tools.RequestUtils;
import com.uber.autodispose.AutoDisposeConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * Http请求类
 *
 * @author ZhongDaFeng
 */
public class RxHttp {

    /*请求方式*/
    private Method method;
    /*请求参数*/
    private Map<String, Object> parameter;
    /*header*/
    private Map<String, Object> header;
    /*HttpCallback*/
    private HttpCallback httpCallback;
    /*标识请求的TAG*/
    private String tag;
    /*文件map*/
    private Map<String, File> fileMap;
    /*上传文件回调*/
    private UploadCallback uploadCallback;
    /*apiUrl*/
    private String apiUrl;
    /*String参数*/
    String bodyString;
    /*是否强制JSON格式*/
    boolean isJson;
    private AutoDisposeConverter autoDisposeConverter;


    /*构造函数*/
    private RxHttp(Builder builder) {
        this.parameter = builder.parameter;
        this.header = builder.header;
        this.tag = builder.tag;
        this.fileMap = builder.fileMap;
        this.apiUrl = builder.apiUrl;
        this.isJson = builder.isJson;
        this.bodyString = builder.bodyString;
        this.method = builder.method;
        this.autoDisposeConverter = builder.autoDisposeConverter;
    }

    public boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * 设置请求唯一标识
     */
    public void handleTag() {
        /*设置请求唯一标识*/
        if (httpCallback != null) {
            httpCallback.setTag(isEmpty(tag) ? String.valueOf(System.currentTimeMillis()) : tag);
        }
    }

    /**
     * header处理
     */
    private void handleHeader() {
        if (!header.isEmpty()) {
            //处理header中文或者换行符出错问题
            for (String key : header.keySet()) {
                header.put(key, RequestUtils.getHeaderValueEncoded(header.get(key)));
            }
        }
    }

    /**
     * 根据tag取消请求
     *
     * @param tag
     */
    public static void cancel(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        RequestManagerImpl.getInstance().cancel(tag);
    }

    /**
     * 取消全部请求
     */
    public static void cancelAll() {
        RequestManagerImpl.getInstance().cancelAll();
    }

    /*GET*/
    public static RxHttp.Builder get(String apiUrl) {
        RxHttp.Builder builder = new Builder();
        builder.get(apiUrl);
        return builder;
    }
    /*POST*/
    public static RxHttp.Builder post(String apiUrl) {
        RxHttp.Builder builder = new Builder();
        builder.post(apiUrl);
        return builder;
    }

    /*DELETE*/
    public static RxHttp.Builder delete(String apiUrl) {
        RxHttp.Builder builder = new Builder();
        builder.delete(apiUrl);
        return builder;
    }

    /*PUT*/
    public static RxHttp.Builder put(String apiUrl) {
        RxHttp.Builder builder = new Builder();
        builder.put(apiUrl);
        return builder;
    }

    /*普通Http请求*/
    public void request(Retrofit retrofit, @NonNull HttpCallback httpCallback) {
        this.httpCallback = httpCallback;
        doRequest(retrofit);
    }

    /*普通Http请求*/
    public void request(@NonNull HttpCallback httpCallback) {
        this.httpCallback = httpCallback;
        doRequest(RetrofitUtils.get().getRetrofit());
    }

    /*上传文件请求*/
    public void upload(@NonNull UploadCallback uploadCallback) {
        this.uploadCallback = uploadCallback;
        doUpload();
    }

    /*取消网络请求*/
    public void cancel() {
        if (httpCallback != null) {
            httpCallback.cancel();
        }
        if (uploadCallback != null) {
            uploadCallback.cancel();
        }
    }

    /*请求是否已经取消*/
    public boolean isCanceled() {
        boolean isCanceled = true;
        if (httpCallback != null) {
            isCanceled = httpCallback.isDisposed();
        }
        if (uploadCallback != null) {
            isCanceled = uploadCallback.isDisposed();
        }
        return isCanceled;
    }

    /*执行请求*/
    private void doRequest(Retrofit retrofit) {
        handleTag();

        handleHeader();

        /*请求方式处理*/
        Observable apiObservable = disposeApiObservable(retrofit);

        /* 被观察者 httpObservable */
        HttpObservable httpObservable = new HttpObservable.Builder(apiObservable)
                .httpObserver(httpCallback)
                .build();
        /* 观察者  httpObserver */
        /*设置监听*/
        Observable observable = httpObservable.observe();
        if (autoDisposeConverter != null) {
            observable.as(autoDisposeConverter);
        }
        observable.subscribe(httpCallback);
    }

    /*执行文件上传*/
    private void doUpload() {
        handleTag();

        handleHeader();

        /*处理文件集合*/
        List<MultipartBody.Part> fileList = new ArrayList<>();
        if (fileMap != null && fileMap.size() > 0) {
            int size = fileMap.size();
            int index = 1;
            File file;
            RequestBody requestBody;
            for (String key : fileMap.keySet()) {
                file = fileMap.get(key);
                requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), new UploadRequestBody(requestBody, file, index, size, uploadCallback));
                fileList.add(part);
                index++;
            }
        }

        /*请求处理*/
        String url = isEmpty(apiUrl) ? "" : apiUrl;
        Observable apiObservable = RetrofitUtils.get().getRetrofit().create(HttpApi.class).upload(url, parameter, header, fileList);

        /* 被观察者 httpObservable */
        HttpObservable httpObservable = new HttpObservable.Builder(apiObservable)
                .httpObserver(uploadCallback)
                .build();
        /* 观察者  uploadCallback */
        /*设置监听*/
        httpObservable.observe().subscribe(uploadCallback);
    }

    private boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /*处理ApiObservable*/
    private Observable disposeApiObservable(Retrofit retrofit) {
        /*是否JSON格式提交参数*/
        boolean hasBodyString = isNotEmpty(bodyString);
        RequestBody requestBody = null;
        if (hasBodyString) {
            String mediaType = isJson ? "application/json; charset=utf-8" : "text/plain;charset=utf-8";
            requestBody = RequestBody.create(okhttp3.MediaType.parse(mediaType), bodyString);
        }
        /*Api接口*/
        HttpApi apiService = retrofit.create(HttpApi.class);
        Observable apiObservable = null;
        String url = isEmpty(apiUrl) ? "" : apiUrl;
        switch (method) {
            case GET:
                apiObservable = apiService.get(url, parameter, header);
                break;
            case POST:
                if (hasBodyString)
                    apiObservable = apiService.post(url, requestBody, header);
                else
                    apiObservable = apiService.post(url, parameter, header);
                break;
            case DELETE:
                apiObservable = apiService.delete(url, parameter, header);
                break;
            case PUT:
                apiObservable = apiService.put(url, parameter, header);
                break;
        }
        return apiObservable;
    }

    /**
     * Builder
     * 构造Request所需参数，按需设置
     */
    public static final class Builder {
        /*请求方式*/
        private Method method = Method.POST;
        /*请求参数*/
        private Map<String, Object> parameter = new TreeMap<>();
        /*header*/
        private Map<String, Object> header = new TreeMap<>();
        /*标识请求的TAG*/
        private String tag;
        /*文件map*/
        private Map<String, File> fileMap = new TreeMap<>();
        /*apiUrl*/
        private String apiUrl;
        /*String参数*/
        private String bodyString;
        /*是否强制JSON格式*/
        private boolean isJson = true;
        /*自动取消*/
        private AutoDisposeConverter autoDisposeConverter;

        public Builder() {
        }

        /*GET*/
        public RxHttp.Builder get(@NonNull String apiUrl) {
            this.method = Method.GET;
            this.apiUrl = apiUrl;
            return this;
        }

        /*POST*/
        public RxHttp.Builder post(@NonNull String apiUrl) {
            this.method = Method.POST;
            this.apiUrl = apiUrl;
            return this;
        }

        /*DELETE*/
        public RxHttp.Builder delete(@NonNull String apiUrl) {
            this.method = Method.DELETE;
            this.apiUrl = apiUrl;
            return this;
        }

        /*PUT*/
        public RxHttp.Builder put(@NonNull String apiUrl) {
            this.method = Method.PUT;
            this.apiUrl = apiUrl;
            return this;
        }

        /*AutoDisposeConverter*/
        public RxHttp.Builder addAutoDispose(@NonNull AutoDisposeConverter autoDispose) {
            this.autoDisposeConverter = autoDispose;
            return this;
        }

        public RxHttp.Builder addHeader(@NonNull Map<String, Object> header) {
            this.header = header;
            return this;
        }

        public RxHttp.Builder addParameter(@NonNull Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        /*  bodyString设置后Parameter则无效 */
        public RxHttp.Builder setBodyJson(@NonNull String bodyString) {
            this.bodyString = bodyString;
            return this;
        }

        /* bodyString设置后Parameter则无效 */
        public RxHttp.Builder setBodyString(@NonNull String bodyString) {
            this.isJson = false;
            this.bodyString = bodyString;
            return this;
        }

        /*tag*/
        public RxHttp.Builder tag(@NonNull String tag) {
            this.tag = tag;
            return this;
        }

        /*文件集合*/
        public RxHttp.Builder file(@NonNull Map<String, File> file) {
            this.fileMap = file;
            return this;
        }

        /*一个Key对应多个文件*/
        public RxHttp.Builder file(@NonNull String key, @NonNull List<File> fileList) {
            if (fileList.size() > 0) {
                for (File file : fileList) {
                    fileMap.put(key, file);
                }
            }
            return this;
        }

        public void request(@NonNull HttpCallback httpCallback) {
            new RxHttp(this).request(httpCallback);
        }

        public void request(Retrofit retrofit, @NonNull HttpCallback httpCallback) {
            new RxHttp(this).request(retrofit, httpCallback);
        }

        public void upload(@NonNull UploadCallback uploadCallback) {
            new RxHttp(this).upload(uploadCallback);
        }

        public RxHttp build() {
            return new RxHttp(this);
        }
    }

}
