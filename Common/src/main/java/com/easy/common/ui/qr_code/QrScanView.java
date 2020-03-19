package com.easy.common.ui.qr_code;

import com.easy.common.base.CommonView;
import com.easy.framework.rxlifecycle.LifecycleEvent;

public interface QrScanView<E extends LifecycleEvent> extends CommonView<E> {

    void permissionCallback(Boolean granted,int type,Throwable e);

    void scanAlbumCallBack(String result, int code);
}
