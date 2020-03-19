package com.easy.store.impl;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.store.IProvider.IFrameWork2StoreProvider;
import com.easy.store.base.DataStore;

@Route(path = "/store/FrameWork2StoreProvider", name = "store to framework")
public class FrameWork2StoreProviderImp implements IFrameWork2StoreProvider {

    @Override
    public void init(Application application) {
        DataStore.getInstance().init(application);
    }

    @Override
    public void init(Context context) {

    }
}
