package com.easy.demo.ui.debug;

import android.graphics.Bitmap;

import com.easy.common.base.CommonView;
import com.easy.framework.rxlifecycle.LifecycleEvent;

public interface DebugView<E extends LifecycleEvent> extends CommonView<E> {
    void qRCodeCallback(Bitmap bitmap);
}
