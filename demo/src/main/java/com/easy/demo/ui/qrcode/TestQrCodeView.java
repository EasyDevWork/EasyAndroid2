package com.easy.demo.ui.qrcode;

import android.graphics.Bitmap;

import com.easy.framework.base.BaseView;
import com.easy.net.event.LifecycleEvent;

public interface TestQrCodeView<E extends LifecycleEvent> extends BaseView<E> {

    void qRCodeCallback(Bitmap bitmap);
}
