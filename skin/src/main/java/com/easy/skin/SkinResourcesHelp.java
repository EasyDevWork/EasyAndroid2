package com.easy.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;


import androidx.annotation.Nullable;

import java.util.Locale;


import static com.easy.skin.SkinManager.canUse;

public class SkinResourcesHelp {
    /**
     * APP的Resources
     */
    private Resources appResources;
    /**
     * 皮肤包的Resources
     */
    private Resources skinResources;
    /**
     * 皮肤包名
     */
    private String mSkinPkgName;
    /**
     * 是否使用默认皮肤
     */
    private boolean isDefaultSkin = true;
    private Context context;

    /**
     * 包路径，用来区分是否是当前皮肤
     */
    private String skinPath;
    private String language = "zh";

    private static class Holder {
        private static final SkinResourcesHelp instance = new SkinResourcesHelp();
    }

    private SkinResourcesHelp() {

    }

    public static SkinResourcesHelp getInstance() {
        return Holder.instance;
    }

    public void init(Context ct, String lg) {
        context = ct;
        appResources = ct.getResources();
        if (TextUtils.isEmpty(lg)) {
            language = getSystemLanguage();
        } else {
            language = lg;
        }
    }

    public void changeLanguage(String language) {
        if (!canUse) {
            return;
        }
        this.language = language;
        changeLanguage(appResources, language);
        if (skinResources != null) {
            changeLanguage(skinResources, language);
        }
        SkinManager.getInstance().apply();
    }

    private String getSystemLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale.getLanguage();
    }

    private void changeLanguage(Resources resources, String language) {
        Locale myLocale = getLanguageLocale(language);
        if (myLocale != null) {
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(myLocale);
                if (context != null) {
                    context.createConfigurationContext(configuration);
                }
            } else {
                configuration.locale = myLocale;
            }
            Locale.setDefault(configuration.locale);
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    private Locale getLanguageLocale(String languageCode) {
        if (!TextUtils.isEmpty(languageCode)) {
            switch (languageCode) {
                case "zh":
                    return Locale.CHINA; // 简体中文
                case "en":
                    return Locale.ENGLISH; // 英文
                case "ko":
                    return Locale.KOREAN;
                case "ja":
                    return Locale.JAPAN;
            }
        }
        return null;
    }

    /**
     * 需要加载资源吗
     *
     * @param path
     * @return
     */
    boolean needLoadResource(@Nullable String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        } else if (skinPath != null && skinPath.equals(path) && skinResources != null) {
            return false;
        }
        return true;
    }

    /**
     * 应用已加载皮肤
     *
     * @param path
     */
    void applySkin(String path) {
        if (TextUtils.isEmpty(path)) {
            isDefaultSkin = true;
        } else {
            skinPath = path;
            isDefaultSkin = false;
        }
    }

    /**
     * 设置皮肤
     *
     * @param resources
     * @param pkgName
     */
    void applySkin(String path, Resources resources, String pkgName) {
        skinPath = path;
        skinResources = resources;
        mSkinPkgName = pkgName;
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
        changeLanguage(skinResources, language);
    }

    /**
     * 恢复默认皮肤
     */
    public void reset() {
        skinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    /**
     * 获取资源ID
     *
     * @param resId
     * @return
     */
    private int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        //在皮肤包中不一定就是 当前程序的 id
        //获取对应id 在当前的名称 colorPrimary
        //R.drawable.ic_launcher
        String resName = appResources.getResourceEntryName(resId);
        String resType = appResources.getResourceTypeName(resId);
        int skinId = skinResources.getIdentifier(resName, resType, mSkinPkgName);
        return skinId;
    }

    public int getColor(int resId) {
        if (isDefaultSkin) {
            return appResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResources.getColor(resId);
        }
        return skinResources.getColor(skinId);
    }


    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return appResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResources.getColorStateList(resId);
        }
        return skinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        //如果有皮肤  isDefaultSkin false 没有就是true
        if (isDefaultSkin) {
            return appResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return appResources.getDrawable(resId);
        }
        return skinResources.getDrawable(skinId);
    }


    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = appResources.getResourceTypeName(resId);
        if (resourceTypeName.equals("color")) {
            return getColor(resId);
        } else {
            return getDrawable(resId);
        }
    }

    public String getTextContent(int resId) {
        try {
            if (isDefaultSkin) {
                return appResources.getString(resId);
            }
            int skinId = getIdentifier(resId);
            if (skinId == 0) {
                return appResources.getString(skinId);
            }
            return skinResources.getString(skinId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getString(int resId) {
        try {
            if (isDefaultSkin) {
                return appResources.getString(resId);
            }
            int skinId = getIdentifier(resId);
            if (skinId == 0) {
                return appResources.getString(skinId);
            }
            return skinResources.getString(skinId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Typeface getTypeface(int resId) {
        String skinTypefacePath = getString(resId);
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }
        try {
            Typeface typeface;
            if (isDefaultSkin) {
                typeface = Typeface.createFromAsset(appResources.getAssets(), skinTypefacePath);
                return typeface;
            }
            typeface = Typeface.createFromAsset(skinResources.getAssets(), skinTypefacePath);
            return typeface;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Typeface.DEFAULT;
    }

    /**
     * 可惜获取到的还是本身的theme
     *
     * @param resId
     * @return
     */
    public Resources.Theme getTheme(int resId) {
        if (isDefaultSkin) {
            return null;
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return null;
        }
        Resources.Theme newTheme = skinResources.newTheme();
        Context newContext;
        try {
            newContext = context.createPackageContext(mSkinPkgName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            Resources.Theme theme = newContext.getTheme();
            if (theme != null) {
                newTheme.setTo(theme);
            }
            theme.applyStyle(resId, true);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
