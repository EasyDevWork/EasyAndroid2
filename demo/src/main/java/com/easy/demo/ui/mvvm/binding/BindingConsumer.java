package com.easy.demo.ui.mvvm.binding;

import android.view.View;

/**
 * A one-argument action.
 *
 * @param <T> the first argument type
 */
public interface BindingConsumer<T> {
    void call(View v, T t);
}
