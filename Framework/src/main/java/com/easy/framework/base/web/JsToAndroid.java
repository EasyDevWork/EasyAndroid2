package com.easy.framework.base.web;

import android.webkit.JavascriptInterface;

public abstract class JsToAndroid {

    @JavascriptInterface
    public void jsToAndroidCommon(String args){
        //此方法没用为了通过编译
    }

    public abstract String jsName();

}
