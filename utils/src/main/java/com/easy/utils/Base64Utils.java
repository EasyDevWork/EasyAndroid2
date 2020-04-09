package com.easy.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Base64Utils {

    public static String encodeToString(String content) {
        try {
            return Base64.encodeToString(content.getBytes(), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decodeToString(String encodedString) {
        try {
            return new String(Base64.decode(encodedString, Base64.NO_WRAP));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encodeToFile(File file) {
        try {
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeToFile(File desFile, String encodedString) {
        try {
            byte[] decodeBytes = Base64.decode(encodedString.getBytes(), Base64.NO_WRAP);
            FileOutputStream fos = new FileOutputStream(desFile);
            fos.write(decodeBytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
