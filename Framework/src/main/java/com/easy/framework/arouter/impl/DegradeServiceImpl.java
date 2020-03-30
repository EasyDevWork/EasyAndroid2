package com.easy.framework.arouter.impl;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;
import com.easy.utils.ToastUtils;

/**
 * 降级处理
 */
@Route(path = "/framework/degradeService")
public class DegradeServiceImpl implements DegradeService {
    @Override
    public void onLost(Context context, Postcard postcard) {
        ToastUtils.showShort("找不到页面:" + postcard.getPath());
    }

    @Override
    public void init(Context context) {

    }
}
