package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.TotalResources;

import io.objectbox.converter.PropertyConverter;

public class TotalResourcesConverter implements PropertyConverter<TotalResources, String> {
    @Override
    public TotalResources convertToEntityProperty(String databaseValue) {
        TotalResources ownerKey = new TotalResources();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, TotalResources.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(TotalResources entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
