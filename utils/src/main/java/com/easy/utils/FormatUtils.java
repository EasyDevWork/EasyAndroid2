package com.easy.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

public class FormatUtils {

    public static Double formatDouble(String str) {
        if (EmptyUtils.isNotEmpty(str)) {
            try {
                return Double.parseDouble(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0d;
    }

    public static float formatFloat(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static long formatLong(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int formatInt(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String bytesToHex(String bytes) {
        StringBuffer sb = new StringBuffer();
        String[] splits = bytes.split(",");
        for (int i = 0; i < splits.length; i++) {
            int num = Integer.valueOf(splits[i]);
            String hex = Integer.toHexString(num & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String strip(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0, length = s.length(); i < length; i++) {
            char c = s.charAt(i);
            if (c > '\u001f' && c < '\u007f') {
                b.append(c);
            }
        }
        return b.toString();
    }

    public static byte[] utf8Bytes(String data) {
        try {
            return data.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
    //以json的形式展示
    public static String formatJson(String text) {
        if (!TextUtils.isEmpty(text)) {
            text = text.replace("\\", "");
        }
        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    if (indentString.length() == 0) {
                        json.append(indentString + letter + "\n");
                        indentString = indentString + "\t\t\t";
                        json.append(indentString);
                    } else {
                        json.append("\n" + indentString + letter + "\n");
                        indentString = indentString + "\t\t\t";
                        json.append(indentString);
                    }

                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t\t\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }

}
