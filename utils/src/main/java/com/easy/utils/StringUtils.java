package com.easy.utils;

public class StringUtils {

    public static String buildString(Object... str) {
        int size = str.length;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            if (str[i] != null) {
                builder.append(str[i]);
            }
        }
        return builder.toString();
    }
}
