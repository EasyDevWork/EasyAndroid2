package com.easy.framework.component;

import com.easy.framework.base.BaseApplication;
import com.easy.framework.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface Appcomponent {

    void inject(BaseApplication app);
}
