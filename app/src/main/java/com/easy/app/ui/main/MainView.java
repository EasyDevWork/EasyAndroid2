package com.easy.app.ui.main;

import com.easy.framework.bean.AppVersion;
import com.easy.framework.base.BaseView;
import com.easy.net.beans.Response;

public interface MainView extends BaseView {

    void permissionCallback(Boolean granted,Throwable e);

    void appVersionCallback(Response<AppVersion> response);
}
