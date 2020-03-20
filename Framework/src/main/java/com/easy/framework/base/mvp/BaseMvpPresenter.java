package com.easy.framework.base.mvp;

import android.content.Context;

import com.easy.framework.base.BasePresenter;
import com.easy.net.rxlifecycle.LifecycleProvider;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class BaseMvpPresenter<V extends BaseMvpView> extends BasePresenter {
    Reference<V> mvpViewRef;
    public Reference<Context> contentRef;
    public V mvpView;

    public void attachView(V view, Context context) {
        mvpViewRef = new WeakReference<>(view);
        contentRef = new WeakReference<>(context);
        mvpView = mvpViewRef.get();
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

    public Context getContext(){
        if (contentRef == null) {
            return null;
        }
        return contentRef.get();
    }

    public V getView() {
        if (mvpViewRef == null) {
            return null;
        }
        return mvpViewRef.get();
    }

}
