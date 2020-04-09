package com.easy.utils;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }
    /**
     * 去掉多余的0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s == null)
            return null;
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");
            s = s.replaceAll("[.]$", "");
        }
        return s;
    }
    public static String getSuffixName(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }
        int typeIndex = str.lastIndexOf(".");
        if (typeIndex != -1) {
            String type = str.substring(typeIndex + 1).toLowerCase();
            return type;
        }
        return "";
    }
    /**
     * 去掉字符串中所有的空格
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = null;
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String getHideCardID(String id) {
        if (EmptyUtils.isEmpty(id)) {
            return "";
        } else if (id.length() >= 14) {
            return id.substring(0, 4) + "**********" + id.substring(14);
        } else {
            return id.substring(0, 2) + "****" + id.substring(6);
        }
    }
    /**
     * 手机号加密
     *
     * @param mobile
     * @return
     */
    public static String encryptMobile(String mobile) {
        if (EmptyUtils.isEmpty(mobile) || mobile.length() != 11) {
            return mobile;
        }
        return buildString(mobile.substring(0, 3), "****", mobile.substring(7, mobile.length()));
    }

    /**
     * 姓名加密
     *
     * @param name
     * @return
     */
    public static String encryptName(String name) {
        if (EmptyUtils.isEmpty(name) || name.length() < 2) {
            return name;
        }
        return buildString(name.substring(0, 1), "*", name.substring(2, name.length()));
    }
    /**
     * 对浮点型数值进行格式化处理
     * 2.00->2
     * 2.10->2.1
     * 2.12->2.12
     * 2.01->2.01
     *
     * @param value
     * @return
     */
    public static String formatNum(float value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        String st;
        try {
            st = nf.format(value);
        } catch (Exception e) {
            e.printStackTrace();
            st = String.valueOf(value);
        }
        if (st.indexOf(".") != -1) {
            st = st.replaceAll("0+?$", "");//去掉后面无用的零
            st = st.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return st;
    }

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

    public static String appendQueryParameter(String url, String name, String params) {
        if (url.contains("?")) {
            url = url + "&" + name + "=" + params;
        } else {
            url = url + "?" + name + "=" + params;
        }
        return url;
    }

    public static String join(String[] array, String sep) {
        if (array == null) {
            return null;
        }

        int arraySize = array.length;
        int sepSize = 0;
        if (sep != null && !sep.equals("")) {
            sepSize = sep.length();
        }

        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].length()) + sepSize) * arraySize);
        StringBuilder buf = new StringBuilder(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(sep);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String jsonJoin(String[] array) {
        int arraySize = array.length;
        int bufSize = arraySize * (array[0].length() + 3);
        StringBuilder buf = new StringBuilder(bufSize);
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(',');
            }

            buf.append('"');
            buf.append(array[i]);
            buf.append('"');
        }
        return buf.toString();
    }
}
