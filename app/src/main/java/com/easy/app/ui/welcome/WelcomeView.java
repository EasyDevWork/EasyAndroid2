package com.easy.app.ui.welcome;

import com.easy.common.base.CommonView;
import com.easy.net.event.LifecycleEvent;

public interface WelcomeView<E extends LifecycleEvent> extends CommonView<E> {
    void countDownCallback(long time);

    void permissionCallback(Boolean granted,Throwable e);
}
