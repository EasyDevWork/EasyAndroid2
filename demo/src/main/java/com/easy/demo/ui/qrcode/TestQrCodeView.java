package com.easy.demo.ui.qrcode;

import android.graphics.Bitmap;

import com.easy.framework.base.BaseView;
public interface TestQrCodeView extends BaseView {

    void qRCodeCallback(Bitmap bitmap);
}
