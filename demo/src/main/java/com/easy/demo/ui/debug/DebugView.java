package com.easy.demo.ui.debug;

import android.graphics.Bitmap;

import com.easy.framework.base.BaseView;
import com.easy.net.event.LifecycleEvent;

public interface DebugView<E extends LifecycleEvent> extends BaseView<E> {
    void qRCodeCallback(Bitmap bitmap);
}
