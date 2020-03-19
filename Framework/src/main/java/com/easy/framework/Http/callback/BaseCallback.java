package com.easy.framework.Http.callback;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.easy.framework.Http.exception.ApiException;
import com.easy.framework.Http.observer.HttpObserver;
import com.easy.framework.utils.Utils;

public abstract class BaseCallback<T> extends HttpObserver<T> {

    @Override
    public void onNext(@NonNull T value) {
        super.onNext(value);
        inSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (e instanceof ApiException) {
            onErrorLogic((ApiException) e);
        }
    }

    @Override
    public void onCanceled() {
        onCanceledLogic();
    }

    /**
     * 请求成功
     *
     * @param t
     */
    public abstract void inSuccess(T t);

    /**
     * 请求出错
     */
    public abstract void inError(ApiException exception);

    /**
     * 请求取消
     */
    public abstract void inCancel();

    private void onErrorLogic(ApiException exception) {
        if (!Utils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(() -> inError(exception));
        } else {
            inError(exception);
        }
    }

    /**
     * Http被取消回调处理逻辑
     */
    private void onCanceledLogic() {
        if (!Utils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(() -> inCancel());
        } else {
            inCancel();
        }
    }
}
