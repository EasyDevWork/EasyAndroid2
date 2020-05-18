package com.easy.net.callback;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.easy.net.beans.Response;
import com.easy.net.exception.ApiException;
import com.easy.net.tools.ThreadUtils;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

import static com.easy.net.exception.ExceptionEngine.UN_KNOWN_ERROR;

public  abstract class BaseCallback<T> implements Observer<T> {
    /**
     * 数据转换/解析数据
     *
     * @param data
     * @return
     */
    public abstract T onConvert(Response data);

    /**
     * 请求成功
     *
     * @param t
     */
    public abstract void handleSuccess(Response t);

    /**
     * 请求出错
     */
    public abstract void handleError(ApiException exception);

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(@NonNull T value) {
        if (value instanceof ResponseBody) {
            ResponseBody responseBody = (ResponseBody) value;
            Response response = assembleResponse(responseBody);
            if (response != null) {
                handleSuccess(response);
            }
        } else {
            //todo 其他情况再另外处理
        }
    }

    /**
     * 组装返回值
     *
     * @param responseBody
     * @return
     */
    public Response assembleResponse(ResponseBody responseBody) {
        Response response = null;
        try {
            String result = responseBody.string();
            response = new Response();
            response.setOriData(result);
            response.setResultObj(onConvert(response));
            response.setCode(Response.SUCCESS_STATE);
        } catch (IOException e) {
            onError(new ApiException(UN_KNOWN_ERROR, e.getMessage(), e));
        }
        return response;
    }

    @Override
    public void onError(Throwable e) {
        ApiException apiException;
        if (e instanceof ApiException) {
            apiException = (ApiException) e;
        } else {
            apiException = new ApiException(UN_KNOWN_ERROR, e.getMessage(), e);
        }
        if (!ThreadUtils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(() -> handleError(apiException));
        } else {
            handleError(apiException);
        }
    }

    @Override
    public void onComplete() {

    }
}
