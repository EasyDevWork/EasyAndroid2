package com.easy.eoschain.utils;

import android.text.TextUtils;

public class EosUtils {

    public static String getNumOfData(String data) {
        if (!TextUtils.isEmpty(data) && data.contains(" ")) {
            return data.substring(0, data.indexOf(" "));
        }
        return data;
    }

    public static String getUnit(String data) {
        if (!TextUtils.isEmpty(data) && data.contains(" ")) {
            int index = data.indexOf(" ");
            return data.substring(index + 1);
        }
        return null;
    }

    public static Double getDoubleNumOfData(String data) {
        if (!TextUtils.isEmpty(data) && data.contains(" ")) {
            try {
                return Double.valueOf(data.substring(0, data.indexOf(" ")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0d;
    }
}
