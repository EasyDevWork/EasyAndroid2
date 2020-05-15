package com.easy.net.callback;

import com.easy.net.beans.Response;
import com.easy.net.exception.ApiException;
import com.easy.net.help.ParseHelper;

import java.io.IOException;

import okhttp3.ResponseBody;

public abstract class HttpCallback<T> extends BaseCallback<T> implements ParseHelper {

    @Override
    public void parse(String data) {
        Response t = onConvert(data);
        handleSuccess(t);
    }

    @Override
    public void inSuccess(T value) {
        if (value instanceof ResponseBody) {
            ResponseBody responseBody = (ResponseBody) value;
            try {
                parse(responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void inError(ApiException exception) {
        handleError(exception);
    }

    @Override
    public void inCancel() {
        handleCancel();
    }

    /**
     * 数据转换/解析数据
     *
     * @param data
     * @return
     */
    public abstract Response onConvert(String data);

    /**
     * 成功回调
     *
     * @param value
     */
    public abstract void handleSuccess(Response value);

    /**
     * 取消回调
     */
    public abstract void handleCancel();


    /**
     * 请求出错
     */
    public abstract void handleError(ApiException exception);
}
