package com.easy.demo.ui.testweb;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.easy.framework.base.web.protocol.IProtocolCallback;
import com.easy.framework.base.web.JsToAndroid;
import com.easy.utils.ToastUtils;

public class JsToAndroidImp extends JsToAndroid {

    Context context;

    public JsToAndroidImp(Context context) {
        this.context = context;
    }

    @Override
    public String jsName() {
        return "android";
    }

    @JavascriptInterface
    public void jsCallAndroid() {
        ToastUtils.showShort("Js调用Android无参方法");
    }

    @JavascriptInterface
    public void jsCallAndroidArgs(String args) {
        ToastUtils.showShort("Js调用Android有参方法: " + args);
    }
}
