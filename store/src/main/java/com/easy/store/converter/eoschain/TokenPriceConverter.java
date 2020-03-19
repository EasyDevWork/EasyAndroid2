package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.TokenPrice;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.converter.PropertyConverter;

public class TokenPriceConverter implements PropertyConverter<List<TokenPrice>, String> {
    @Override
    public List<TokenPrice> convertToEntityProperty(String databaseValue) {
        List<TokenPrice> permissionsEosList = new ArrayList<>();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                permissionsEosList = JSON.parseArray(databaseValue, TokenPrice.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return permissionsEosList;
    }

    @Override
    public String convertToDatabaseValue(List<TokenPrice> entity) {
        if (entity != null) {
            return JSON.toJSONString(entity);
        }
        return null;
    }
}
