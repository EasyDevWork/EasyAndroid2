package com.easy.framework.base;

import android.content.Context;

import com.easy.framework.observable.DataObservable;
import com.trello.rxlifecycle3.LifecycleProvider;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

import io.reactivex.Observable;

public abstract class BasePresenter<V extends BaseView> {

    Reference<V> mvpViewRef;
    Reference<Context> contentRef;
    public V mvpView;
    LifecycleProvider lifecycleProvider;

    public void attachView(Context context, V view, LifecycleProvider lifecycleProvider) {
        mvpViewRef = new WeakReference<>(view);
        contentRef = new WeakReference<>(context);
        mvpView = mvpViewRef.get();
        this.lifecycleProvider = lifecycleProvider;
    }

    public LifecycleProvider getRxLifecycle() {
        return lifecycleProvider;
    }

    public DataObservable.Builder bindObservable(Observable dataObservable) {
        return DataObservable.builder(dataObservable).lifecycleProvider(lifecycleProvider);
    }

    public void detachView() {
        if (mvpViewRef != null && mvpViewRef.get() != null) {
            mvpViewRef.clear();
            mvpViewRef = null;
        }
        if (contentRef != null && contentRef.get() != null) {
            contentRef.clear();
            contentRef = null;
        }
    }

    @Nullable
    public Context getContext() {
        if (contentRef == null) {
            return null;
        }
        return contentRef.get();
    }

    @Nullable
    public V getView() {
        if (mvpViewRef == null) {
            return null;
        }
        return mvpViewRef.get();
    }
}
