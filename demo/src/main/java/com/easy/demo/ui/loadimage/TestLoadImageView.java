package com.easy.demo.ui.loadimage;


import com.easy.framework.base.BaseView;
import com.easy.net.event.LifecycleEvent;

import java.io.File;

public interface TestLoadImageView<E extends LifecycleEvent> extends BaseView<E> {
    void permissionCallback(Boolean granted, Object o);

    void downloadCallback(File file, Throwable e);
}
