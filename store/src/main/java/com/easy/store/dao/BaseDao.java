package com.easy.store.dao;

import com.easy.store.base.DataStore;

import io.objectbox.BoxStore;

public class BaseDao {
   public BoxStore boxStore;

    public BaseDao() {
        this.boxStore = DataStore.getInstance().getBoxStore();
    }
}
