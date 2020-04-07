package com.easy.demo.ui.debug;

import com.easy.framework.base.BasePresenter;
import com.easy.store.dao.CacheDao;

import javax.inject.Inject;

import dagger.Lazy;

public class DebugPresenter extends BasePresenter<DebugView> {
    @Inject
    public DebugPresenter() {

    }

    @Inject
    Lazy<CacheDao> cacheDao;

    public void setTestCache(String test) {
        cacheDao.get().write("test", test);
    }

    public String getTestCache() {
        return cacheDao.get().read("test", "default");
    }



}