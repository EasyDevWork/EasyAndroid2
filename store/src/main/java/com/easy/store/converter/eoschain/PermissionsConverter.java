package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.Permissions;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.converter.PropertyConverter;

public class PermissionsConverter implements PropertyConverter<List<Permissions>, String> {
    @Override
    public List<Permissions> convertToEntityProperty(String databaseValue) {
        List<Permissions> permissionsEosList = new ArrayList<>();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                permissionsEosList = JSON.parseArray(databaseValue, Permissions.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return permissionsEosList;
    }

    @Override
    public String convertToDatabaseValue(List<Permissions> entity) {
        if (entity != null) {
            return JSON.toJSONString(entity);
        }
        return null;
    }
}
