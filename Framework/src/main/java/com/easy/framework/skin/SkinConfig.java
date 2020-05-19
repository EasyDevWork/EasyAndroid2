package com.easy.framework.skin;

import android.content.Context;
import android.content.SharedPreferences;

public class SkinConfig {

    public static final String NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String DEFAULT_SKIN = "default_skin";
    public static final String ATTR_SKIN_ENABLE = "enable";

    /**
     * get path of last skin package path
     *
     * @param context
     * @return path of skin package
     */
    public static String getCustomSkinPath(Context context) {
        return getString(context, "skin_path", DEFAULT_SKIN);
    }

    public static void saveSkinPath(Context context, String path) {
        putString(context, "skin_path", path);
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences("skin_pref", Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences("skin_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }
}
