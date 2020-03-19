package com.easy.framework.Http.cancel;

import io.reactivex.disposables.Disposable;

public interface RequestManager<T> {
    /**
     * 添加
     *
     * @param tag
     * @param disposable
     */
    void add(T tag, Disposable disposable);

    /**
     * 移除
     *
     * @param tag
     */
    void remove(T tag);

    /**
     * 取消
     *
     * @param tag
     */
    void cancel(T tag);

    /**
     * 取消全部
     */
    void cancelAll();

}
