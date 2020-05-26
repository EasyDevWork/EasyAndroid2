package com.easy.framework.component;

import com.easy.framework.bean.GlobalConfig;
import com.easy.framework.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface Appcomponent {

    GlobalConfig getAppConfig();
}
