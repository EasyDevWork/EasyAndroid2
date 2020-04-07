package com.easy.demo.ui.loadimage;


import com.easy.framework.base.BaseView;

import java.io.File;

public interface TestLoadImageView extends BaseView {
    void permissionCallback(Boolean granted, Object o);

    void downloadCallback(File file, Throwable e);
}
