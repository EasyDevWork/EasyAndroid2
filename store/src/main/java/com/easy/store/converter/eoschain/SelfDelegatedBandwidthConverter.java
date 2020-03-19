package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.SelfDelegatedBandwidthBean;
import com.easy.store.bean.eoschain.TotalResources;

import io.objectbox.converter.PropertyConverter;

public class SelfDelegatedBandwidthConverter implements PropertyConverter<SelfDelegatedBandwidthBean, String> {
    @Override
    public SelfDelegatedBandwidthBean convertToEntityProperty(String databaseValue) {
        SelfDelegatedBandwidthBean ownerKey = new SelfDelegatedBandwidthBean();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, SelfDelegatedBandwidthBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(SelfDelegatedBandwidthBean entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
