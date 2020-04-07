package com.easy.qrcode.ui.qr_code;

import com.easy.framework.base.BaseView;

public interface QrScanView extends BaseView {

    void permissionCallback(Boolean granted,int type,Throwable e);

    void scanAlbumCallBack(String result, int code);
}
