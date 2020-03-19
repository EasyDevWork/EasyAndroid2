package com.easy.framework.Http.cancel;

public interface RequestCancel {

    /**
     * 取消请求
     */
    void cancel();

    /**
     * 请求被取消
     */
    void onCanceled();
}