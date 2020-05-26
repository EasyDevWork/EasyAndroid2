package com.easy.framework.module;


import android.app.Application;

import com.easy.framework.bean.GlobalConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public GlobalConfig getAppConfig() {
        return new GlobalConfig();
    }
}
