package com.easy.framework.arouter.impl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.framework.arouter.FrameWork2AppProvider;
import com.easy.utils.ToastUtils;

@Route(path = "/framework/FrameWork2AppProvider", name = "framework to app")
public class FrameWork2AppProviderImpl implements FrameWork2AppProvider {

    Context context;

    @Override
    public void init(Context context) {
        this.context = context;
    }

    /**
     * 该方法只用于测试跨模块调用
     * --Toast可以直接使用{@link ToastUtils )}
     *
     * @param msg
     */
    @Override
    public void showToast(String msg) {
        ToastUtils.showShort(msg);
    }
}
