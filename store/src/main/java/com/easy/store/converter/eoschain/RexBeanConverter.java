package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.RexBean;

import io.objectbox.converter.PropertyConverter;

public class RexBeanConverter implements PropertyConverter<RexBean, String> {
    @Override
    public RexBean convertToEntityProperty(String databaseValue) {
        RexBean ownerKey = new RexBean();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, RexBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(RexBean entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
