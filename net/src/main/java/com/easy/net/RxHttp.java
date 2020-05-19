package com.easy.net;

import androidx.annotation.NonNull;

import com.easy.net.api.HttpApi;
import com.easy.net.beans.Response;
import com.easy.net.callback.HttpCallback;
import com.easy.net.callback.UploadCallback;
import com.easy.net.download.UploadRequestBody;
import com.easy.net.function.DataSwitchFunction;
import com.easy.net.function.HttpResultFunction;
import com.easy.net.retrofit.Method;
import com.easy.net.retrofit.RetrofitHelp;
import com.easy.net.tools.RequestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Http请求类
 */
public class RxHttp {

    /*请求方式*/
    private Method method;
    /*请求参数*/
    private Map<String, Object> parameter;
    /*header*/
    private Map<String, Object> header;
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

    /*构造函数*/
    private RxHttp(Builder builder) {
        this.parameter = builder.parameter;
        this.header = builder.header;
        this.fileMap = builder.fileMap;
        this.apiUrl = builder.apiUrl;
        this.isJson = builder.isJson;
        this.bodyString = builder.bodyString;
        this.method = builder.method;
    }

    public boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
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
    public void request(@NonNull HttpCallback httpCallback) {
        doRequest(RetrofitHelp.get().getRetrofit())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(httpCallback);
    }

    /*普通Http请求*/
    public <T> Observable<Response<T>> request() {
        return doRequest(RetrofitHelp.get().getRetrofit())
                .map(new DataSwitchFunction<>());
    }

    /*执行请求*/
    private Observable<ResponseBody> doRequest(Retrofit retrofit) {
        handleHeader();
        /*设置监听*/
        Observable<ResponseBody> observable = apiObservable(retrofit)
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io());
        return observable;
    }

    /*上传文件请求*/
    public void upload(@NonNull UploadCallback uploadCallback) {
        this.uploadCallback = uploadCallback;
        doUpload();
    }

    /*执行文件上传*/
    private void doUpload() {
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
                MediaType mediaType = MediaType.Companion.parse("multipart/form-data");
                requestBody = RequestBody.Companion.create(file, mediaType);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), new UploadRequestBody(requestBody, file, index, size, uploadCallback));
                fileList.add(part);
                index++;
            }
        }

        /*请求处理*/
        String url = isEmpty(apiUrl) ? "" : apiUrl;
        Observable apiObservable = RetrofitHelp.get().getRetrofit().create(HttpApi.class).upload(url, parameter, header, fileList);

        /* 观察者  uploadCallback */
        /*设置监听*/
        apiObservable.onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadCallback);
    }

    private boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    private Observable<ResponseBody> apiObservable(Retrofit retrofit) {
        /*是否JSON格式提交参数*/
        boolean hasBodyString = isNotEmpty(bodyString);
        RequestBody requestBody = null;
        if (hasBodyString) {
            MediaType mediaType = isJson ? MediaType.Companion.parse("application/json; charset=utf-8") : MediaType.Companion.parse("text/plain;charset=utf-8");
            requestBody = RequestBody.Companion.create(bodyString, mediaType);
        }
        /*Api接口*/
        HttpApi apiService = retrofit.create(HttpApi.class);
        Observable<ResponseBody> apiObservable = null;
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
        /*文件map*/
        private Map<String, File> fileMap = new TreeMap<>();
        /*apiUrl*/
        private String apiUrl;
        /*String参数*/
        private String bodyString;
        /*是否强制JSON格式*/
        private boolean isJson = true;

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
            this.isJson = true;
            this.bodyString = bodyString;
            return this;
        }

        /* bodyString设置后Parameter则无效 */
        public RxHttp.Builder setBodyString(@NonNull String bodyString) {
            this.isJson = false;
            this.bodyString = bodyString;
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

        public <T> Observable<Response<T>> request(Class<T> t) {
            return new RxHttp(this).request();
        }

        public void upload(@NonNull UploadCallback uploadCallback) {
            new RxHttp(this).upload(uploadCallback);
        }
    }
}
