package com.easy.app.ui.welcome;

import com.easy.framework.base.BaseView;
import com.easy.net.event.LifecycleEvent;

public interface WelcomeView<E extends LifecycleEvent> extends BaseView<E> {
    void countDownCallback(long time);

    void permissionCallback(Boolean granted,Throwable e);
}
