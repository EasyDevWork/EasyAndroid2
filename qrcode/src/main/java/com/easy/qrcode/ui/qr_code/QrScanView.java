package com.easy.qrcode.ui.qr_code;

import com.easy.framework.base.BaseView;
import com.easy.net.event.LifecycleEvent;

public interface QrScanView<E extends LifecycleEvent> extends BaseView<E> {

    void permissionCallback(Boolean granted,int type,Throwable e);

    void scanAlbumCallBack(String result, int code);
}
