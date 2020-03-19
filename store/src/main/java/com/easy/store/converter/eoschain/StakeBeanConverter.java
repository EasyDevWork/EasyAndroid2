package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.StakeBean;

import io.objectbox.converter.PropertyConverter;

public class StakeBeanConverter implements PropertyConverter<StakeBean, String> {
    @Override
    public StakeBean convertToEntityProperty(String databaseValue) {
        StakeBean ownerKey = new StakeBean();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, StakeBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(StakeBean entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
