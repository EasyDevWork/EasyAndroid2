package com.easy.app.ui.main;

import com.easy.framework.bean.AppVersion;
import com.easy.framework.base.BaseView;
import com.easy.net.beans.Response;
import com.easy.net.event.LifecycleEvent;

public interface MainView<E extends LifecycleEvent> extends BaseView<E> {

    void permissionCallback(Boolean granted,Throwable e);

    void appVersionCallback(Response<AppVersion> response);
}
