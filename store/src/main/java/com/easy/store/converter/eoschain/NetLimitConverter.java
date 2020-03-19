package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.NetLimit;

import io.objectbox.converter.PropertyConverter;

public class NetLimitConverter implements PropertyConverter<NetLimit, String> {
    @Override
    public NetLimit convertToEntityProperty(String databaseValue) {
        NetLimit ownerKey = new NetLimit();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, NetLimit.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(NetLimit entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
