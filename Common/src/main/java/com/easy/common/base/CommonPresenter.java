package com.easy.common.base;

import com.easy.store.dao.GlobalConfigDao;
import com.easy.framework.base.mvp.BaseMvpPresenter;

import javax.inject.Inject;

import dagger.Lazy;

public class CommonPresenter<V extends CommonView> extends BaseMvpPresenter<V> {

    @Inject
    public Lazy<GlobalConfigDao> configDao;

}
