package com.easy.store.dao;


import com.easy.store.bean.GlobalConfig;

import javax.inject.Inject;

import io.objectbox.Box;


public class GlobalConfigDao extends BaseDao {

    Box<GlobalConfig> configBox;
    private static GlobalConfig config;

    @Inject
    public GlobalConfigDao() {
        if (boxStore != null) {
            configBox = boxStore.boxFor(GlobalConfig.class);
        }
        config = configBox.query().build().findFirst();
        if (config == null) {
            config = new GlobalConfig();
        }
    }

    public void setDeBug(boolean isDebug) {
        config.setDebug(isDebug);
    }

    public boolean isDebug() {
        return config.isDebug();
    }

}
