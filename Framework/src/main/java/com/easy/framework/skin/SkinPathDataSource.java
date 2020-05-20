package com.easy.framework.skin;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SkinPathDataSource {

    private static final String SKIN_SHARED = "Black.skin";
    private static final String KEY_SKIN_PATH = "skinPath";//
    private final SharedPreferences mPref;
    private static Application mApplication;

    private SkinPathDataSource() {
        mPref = mApplication.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
    }

    private static class Holder {
        private static SkinPathDataSource instance = new SkinPathDataSource();
    }

    public static SkinPathDataSource getInstance() {
        return Holder.instance;
    }

    public static void init(Application application) {
        mApplication = application;
    }

    public void saveSkinPath(String path) {
        mPref.edit().putString(KEY_SKIN_PATH, path).apply();
    }

    public String getSkinPath() {
        return mPref.getString(KEY_SKIN_PATH, null);
    }


}
