package com.easy.store.dao;

import com.easy.store.base.EasyStore;

import io.objectbox.BoxStore;

public class BaseDao {
   public BoxStore boxStore;

    public BaseDao() {
        this.boxStore = EasyStore.getInstance().getBoxStore();
    }
}
