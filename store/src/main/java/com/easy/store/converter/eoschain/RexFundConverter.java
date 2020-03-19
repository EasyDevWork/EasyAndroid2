package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.RexFund;

import io.objectbox.converter.PropertyConverter;

public class RexFundConverter implements PropertyConverter<RexFund, String> {
    @Override
    public RexFund convertToEntityProperty(String databaseValue) {
        RexFund ownerKey = new RexFund();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, RexFund.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(RexFund entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
