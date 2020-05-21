package com.easy.framework.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.easy.utils.EmptyUtils;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

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

    private WeakReference<Context> mContext;
    /**
     * 包路径，用来区分是否是当前皮肤
     */
    private String skinPath;

    private static class Holder {
        private static final SkinResourcesHelp instance = new SkinResourcesHelp();
    }

    private SkinResourcesHelp() {

    }

    public static SkinResourcesHelp getInstance() {
        return Holder.instance;
    }

    public void init(Context context) {
        mContext = new WeakReference<>(context);
        this.appResources = context.getResources();
    }

    /**
     * 需要加载资源吗
     *
     * @param path
     * @return
     */
    public boolean needLoadResource(@Nullable String path) {
        if (EmptyUtils.isEmpty(path)) {
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
    public void applySkin(String path) {
        if (EmptyUtils.isEmpty(path)) {
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
    public void applySkin(String path, Resources resources, String pkgName) {
        skinPath = path;
        skinResources = resources;
        mSkinPkgName = pkgName;
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    /**
     * 获取资源ID
     *
     * @param resId
     * @return
     */
    public int getIdentifier(int resId) {
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


    /**
     * 恢复默认皮肤
     */
    public void reset() {
        skinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
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
        Context context;
        try {
            context = mContext.get().createPackageContext(mSkinPkgName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            final Resources.Theme theme = context.getTheme();
            if (theme != null) {
                newTheme.setTo(theme);
            }
            theme.applyStyle(resId, true);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
            // drawable
            return getDrawable(resId);
        }
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
        } catch (Resources.NotFoundException e) {

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
        } catch (RuntimeException e) {
        }
        return Typeface.DEFAULT;
    }
}
