package com.easy.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    /**
     * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public static String div(String v1, String v2, int scale) {
        if (scale < 0) {
            scale = 0;
        }
        boolean check = checkData(v1);
        if (!check) {
            v1 = "0.0000";
        }
        check = checkData(v2);
        if (!check) {
            v2 = "1.0000";
        } else if (FormatUtils.formatDouble(v2) == 0) {
            v2 = "1.0000";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_DOWN).toString();
    }

    public static String mul(String value1, String value2, int scale) {
        boolean check = checkData(value1);
        if (!check) {
            value1 = "0.0000";
        }
        check = checkData(value2);
        if (!check) {
            value2 = "0.0000";
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_DOWN).toString();
    }

    public static String add(String value1, String value2, int scale) {
        boolean check = checkData(value1);
        if (!check) {
            value1 = "0.0000";
        }
        check = checkData(value2);
        if (!check) {
            value2 = "0.0000";
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_DOWN).toString();
    }

    /**
     * 比较大小
     *
     * @param v1 被比较数
     * @param v2 比较数
     * @return 如果v1 大于v2 则 返回true 否则false
     */
    public static boolean compare(String v1, String v2) {
        if (EmptyUtils.isEmpty(v1) || ".".equals(v1)) {
            v1 = "0";
        }
        if (EmptyUtils.isEmpty(v2) || ".".equals(v2)) {
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        try {
            int bj = b1.compareTo(b2);
            if (bj > 0 || bj == 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean checkData(String value) {
        try {
            if (EmptyUtils.isEmpty(value) || EmptyUtils.isEmpty(value.trim())) {
                return false;
            }
            Float.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isZero(String data) {
        try {
            if (EmptyUtils.isEmpty(data)) {
                return true;
            }
            BigDecimal bigDecimal = new BigDecimal(data);
            int i = bigDecimal.compareTo(BigDecimal.ZERO);
            return i == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
