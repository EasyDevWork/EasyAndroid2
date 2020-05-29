package com.easy.demo.ui.fragment;

import com.easy.framework.base.BaseView;
import com.easy.framework.bean.AppVersion;
import com.easy.net.beans.Response;

public interface TestActivityView extends BaseView {

    void appVersionCallback(Response<AppVersion> response);
}
