package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.Token;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.converter.PropertyConverter;

public class TokenConverter implements PropertyConverter<List<Token>, String> {
    @Override
    public List<Token> convertToEntityProperty(String databaseValue) {
        List<Token> permissionsEosList = new ArrayList<>();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                permissionsEosList = JSON.parseArray(databaseValue, Token.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return permissionsEosList;
    }

    @Override
    public String convertToDatabaseValue(List<Token> entity) {
        if (entity != null) {
            return JSON.toJSONString(entity);
        }
        return null;
    }
}
