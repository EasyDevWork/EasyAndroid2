package com.easy.common.base;

import com.easy.framework.base.mvp.BaseMvpView;
import com.easy.framework.rxlifecycle.LifecycleEvent;

public interface CommonView<E extends LifecycleEvent> extends BaseMvpView<E> {

    void showLoading();

    void hideLoading();
}
