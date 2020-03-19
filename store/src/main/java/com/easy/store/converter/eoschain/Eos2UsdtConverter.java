package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.Eos2UsdtPrice;
import com.easy.store.bean.eoschain.RexBean;

import io.objectbox.converter.PropertyConverter;

public class Eos2UsdtConverter implements PropertyConverter<Eos2UsdtPrice, String> {
    @Override
    public Eos2UsdtPrice convertToEntityProperty(String databaseValue) {
        Eos2UsdtPrice ownerKey = new Eos2UsdtPrice();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, Eos2UsdtPrice.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(Eos2UsdtPrice entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
