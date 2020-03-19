package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.CpuLimit;

import io.objectbox.converter.PropertyConverter;

public class CpuLimitConverter implements PropertyConverter<CpuLimit, String> {
    @Override
    public CpuLimit convertToEntityProperty(String databaseValue) {
        CpuLimit ownerKey = new CpuLimit();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, CpuLimit.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(CpuLimit entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
