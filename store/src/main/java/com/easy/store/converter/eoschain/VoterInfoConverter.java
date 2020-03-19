package com.easy.store.converter.eoschain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.easy.store.bean.eoschain.CpuLimit;
import com.easy.store.bean.eoschain.VoterInfo;

import io.objectbox.converter.PropertyConverter;

public class VoterInfoConverter implements PropertyConverter<VoterInfo, String> {
    @Override
    public VoterInfo convertToEntityProperty(String databaseValue) {
        VoterInfo ownerKey = new VoterInfo();
        if (!TextUtils.isEmpty(databaseValue)) {
            try {
                ownerKey = JSON.parseObject(databaseValue, VoterInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ownerKey;
    }

    @Override
    public String convertToDatabaseValue(VoterInfo entityProperty) {
        if (entityProperty != null) {
            return JSON.toJSONString(entityProperty);
        }
        return null;
    }
}
