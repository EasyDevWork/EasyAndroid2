package com.easy.common.ui.web.base;

public interface IWebCallback {

    void loadWebFinish();

    void receivedWebTitle(String title);

    void finish();

    void showCloseBtn(boolean show);

    JsToAndroid getJsToAndroid();
}
