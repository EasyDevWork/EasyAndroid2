package com.easy.framework.base;

import com.easy.net.rxlifecycle.LifecycleProvider;
import com.easy.net.rxlifecycle.LifecycleTransformer;

public interface BaseView<E> {

    <T> LifecycleTransformer<T> bindToLifecycle(E event);

    <T> LifecycleTransformer<T> bindToLifecycle();

    LifecycleProvider getRxLifecycle();
}
