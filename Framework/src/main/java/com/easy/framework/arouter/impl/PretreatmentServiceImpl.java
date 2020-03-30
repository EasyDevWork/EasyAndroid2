package com.easy.framework.arouter.impl;


import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PretreatmentService;
import com.easy.utils.ToastUtils;

/**
 * 路由跳转前预处理
 */
@Route(path = "/framework/pretreatment")
public class PretreatmentServiceImpl implements PretreatmentService {

    @Override
    public boolean onPretreatment(Context context, Postcard postcard) {
        ToastUtils.showShort("进入=》" + postcard.getPath());
        return false;
    }

    @Override
    public void init(Context context) {

    }
}
