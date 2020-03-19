package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.RexPrice;

import io.objectbox.converter.PropertyConverter;

public class RexPriceConverter implements PropertyConverter<RexPrice, String> {
    @Override
    public RexPrice convertToEntityProperty(String databaseValue) {
        RexPrice ownerKey = new RexPrice();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, RexPrice.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(RexPrice entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
