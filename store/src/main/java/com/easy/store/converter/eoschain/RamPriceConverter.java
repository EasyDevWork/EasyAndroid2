package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.RamPrice;

import io.objectbox.converter.PropertyConverter;

public class RamPriceConverter implements PropertyConverter<RamPrice, String> {
    @Override
    public RamPrice convertToEntityProperty(String databaseValue) {
        RamPrice ownerKey = new RamPrice();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, RamPrice.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(RamPrice entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
