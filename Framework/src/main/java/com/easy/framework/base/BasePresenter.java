package com.easy.framework.base;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

public abstract class BasePresenter<V extends BaseView> {

    Reference<V> mvpViewRef;
    Reference<Context> contentRef;
    public V mvpView;
    public LifecycleOwner lifecycleOwner;

    public <T> AutoDisposeConverter<T> getAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner));
    }

    public <T> AutoDisposeConverter<T> getAutoDispose(Lifecycle.Event untilEvent) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent));
    }

    public void attachView(Context context, V view, LifecycleOwner owner) {
        mvpViewRef = new WeakReference<>(view);
        contentRef = new WeakReference<>(context);
        mvpView = mvpViewRef.get();
        lifecycleOwner = owner;
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
        if (lifecycleOwner != null) {
            lifecycleOwner = null;
        }
    }

    @Nullable
    public Context getContext() {
        if (contentRef == null) {
            return null;
        }
        return contentRef.get();
    }
}
