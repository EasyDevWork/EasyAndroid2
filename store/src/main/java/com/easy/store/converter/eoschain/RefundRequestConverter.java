package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.CpuLimit;
import com.easy.store.bean.eoschain.RefundRequest;

import io.objectbox.converter.PropertyConverter;

public class RefundRequestConverter implements PropertyConverter<RefundRequest, String> {
    @Override
    public RefundRequest convertToEntityProperty(String databaseValue) {
        RefundRequest ownerKey = new RefundRequest();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, RefundRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(RefundRequest entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
