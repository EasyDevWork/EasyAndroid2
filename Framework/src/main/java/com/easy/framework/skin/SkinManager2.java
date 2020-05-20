package com.easy.framework.skin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;

public class SkinManager2 extends Observable {

    private static Application mApplication;

    private static class Holder {
        private static final SkinManager2 instance = new SkinManager2();
    }

    public static SkinManager2 getInstance() {
        return Holder.instance;
    }

    private SkinManager2() {

    }

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        mApplication = application;
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycleCallbacks());
        SkinPathDataSource.init(application);
        SkinResources.init(application);
        getInstance().loadSkin(SkinPathDataSource.getInstance().getSkinPath());
    }


    /**
     * 进行换肤
     *
     * @param path 路径为插件包地址，为空则恢复默认
     */
    public boolean loadSkin(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
            method.setAccessible(true);
            method.invoke(assetManager, path);
            Resources resources = mApplication.getResources();
            Resources skinRes = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());

            //获取外部Apk(皮肤包) 包名
            PackageManager mPm = mApplication.getPackageManager();
            PackageInfo info = mPm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            String packageName = info.packageName;
            SkinResources.getInstance().applySkin(skinRes, packageName);
            //记录
            SkinPathDataSource.getInstance().saveSkinPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        setChanged();
        notifyObservers();
        return true;
    }

    /**
     * 清除换肤，恢复默认
     */
    public void clearSkin() {
        SkinPathDataSource.getInstance().saveSkinPath(null);
        SkinResources.getInstance().reset();
        setChanged();
        notifyObservers();
    }
}
