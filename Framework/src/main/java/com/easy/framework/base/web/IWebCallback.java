package com.easy.framework.base.web;

public interface IWebCallback {

    void loadWebFinish();

    void receivedWebTitle(String title);

    void finish();

    void showCloseBtn(boolean show);

    JsToAndroid getJsToAndroid();
}
