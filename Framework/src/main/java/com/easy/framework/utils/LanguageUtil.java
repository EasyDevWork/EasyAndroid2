package com.easy.framework.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * APP内应用语言
 */

public class LanguageUtil {

    public static void changeLanguage(Context mContext, String language) {
        Locale myLocale = getLanguageLocale(language);
        if (myLocale != null) {
            Resources resources = mContext.getResources();
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(myLocale);
                mContext.createConfigurationContext(configuration);
            } else {
                configuration.locale = myLocale;
            }
            Locale.setDefault(configuration.locale);
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    public static String getSystemLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale.getLanguage();
    }

    private static Locale getLanguageLocale(String languageCode) {
        if (Utils.isNotEmpty(languageCode)) {
            if (languageCode.equals("zh")) {
                return Locale.CHINA; // 简体中文
            } else if (languageCode.equals("en")) {
                return Locale.ENGLISH; // 英文
            } else if (languageCode.equals("ko")) {
                return Locale.KOREAN;
            } else if (languageCode.equals("ja")) {
                return Locale.JAPAN;
            }
        }
        return null;
    }

    public static boolean isChinese() {
        String language = getSystemLanguage();
        if ("zh".equals(language)) {
            return true;
        }
        return false;
    }

}
