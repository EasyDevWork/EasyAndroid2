package com.easy.app.ui.main;

import com.easy.common.bean.AppVersion;
import com.easy.common.base.CommonView;
import com.easy.framework.bean.Response;
import com.easy.framework.rxlifecycle.LifecycleEvent;

public interface MainView<E extends LifecycleEvent> extends CommonView<E>{

    void permissionCallback(Boolean granted,Throwable e);

    void appVersionCallback(Response<AppVersion> response);
}
