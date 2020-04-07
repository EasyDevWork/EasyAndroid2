package com.easy.app.ui.welcome;

import com.easy.framework.base.BaseView;

public interface WelcomeView extends BaseView {
    void countDownCallback(long time);

    void permissionCallback(Boolean granted,Throwable e);
}
