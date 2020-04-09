package com.easy.utils;

public class FormatUtils {

    public static Double formatDouble(String str) {
        if (EmptyUtils.isNotEmpty(str)) {
            try {
                return Double.valueOf(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0d;
    }
}
