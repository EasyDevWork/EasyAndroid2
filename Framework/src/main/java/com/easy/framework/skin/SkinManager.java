package com.easy.framework.skin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.easy.framework.skin.view_attr.SkinAttrParam;
import com.easy.utils.EmptyUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Observable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SkinManager extends Observable {

    private static Application mApplication;
    private SkinLifecycleCallbacks lifecycleCallbacks;
    private static final String SKIN_SHARED = "Black.skin";
    private static final String KEY_SKIN_PATH = "skinPath";
    private SharedPreferences mPref;
    Disposable disposable;


    private static class Holder {
        private static final SkinManager instance = new SkinManager();
    }

    public static SkinManager getInstance() {
        return Holder.instance;
    }

    private SkinManager() {
        if (mApplication != null) {
            lifecycleCallbacks = new SkinLifecycleCallbacks();
            mApplication.registerActivityLifecycleCallbacks(lifecycleCallbacks);
            mPref = mApplication.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
        }
    }

    public interface Callback {
        void loadSkin(boolean loadState, Throwable throwable);
    }

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        mApplication = application;
        SkinResourcesHelp.getInstance().init(application);
        SkinManager skinManager2 = getInstance();
        skinManager2.loadSkin(skinManager2.getSkinPath(), null);
    }

    public void addSkinView(Activity activity, View view, SkinAttrParam... skinAttrParams) {
        if (activity == null || view == null || skinAttrParams == null || skinAttrParams.length == 0) {
            return;
        }
        SkinFactory skinFactory = lifecycleCallbacks.getSkinFactory(activity.getLocalClassName());
        if (skinFactory != null) {
            SkinAttribute skinAttribute = skinFactory.getSkinAttribute();
            skinAttribute.addSkinView(view, skinAttrParams);
        }
    }

    public void saveSkinPath(String path) {
        if (mPref != null) {
            mPref.edit().putString(KEY_SKIN_PATH, path).apply();
        }
    }

    public String getSkinPath() {
        if (mPref != null) {
            return mPref.getString(KEY_SKIN_PATH, null);
        }
        return null;
    }

    /**
     * 加载皮肤
     *
     * @param path 路径为插件包地址，为空则恢复默认
     */
    public void loadSkin(String path, Callback callback) {
        if (SkinResourcesHelp.getInstance().needLoadResource(path)) {
            loadSkinResources(path, callback);
            return;
        }
        SkinResourcesHelp.getInstance().applySkin(path);
        if (EmptyUtils.isNotEmpty(path)) {
            saveSkinPath(path);
        }
        apply();
        if (callback != null) {
            callback.loadSkin(true, null);
        }
    }

    public void apply() {
        setChanged();
        notifyObservers();
    }

    /**
     * 加载定制的皮肤资源
     *
     * @param path
     * @param callback
     */
    public void loadSkinResources(String path, Callback callback) {
        Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            if (TextUtils.isEmpty(path)) {
                emitter.onError(new Throwable("skinApk path is null"));
                return;
            }
            File file = new File(path);
            if (!file.exists()) {
                emitter.onError(new Throwable("skinApk File is no find"));
                return;
            }

            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getMethod("addAssetPath", String.class);
            method.setAccessible(true);
            method.invoke(assetManager, path);
            Resources resources = mApplication.getResources();
            Resources skinRes = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());

            PackageManager mPm = mApplication.getPackageManager();
            PackageInfo info = mPm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            String packageName = info.packageName;
            SkinResourcesHelp.getInstance().applySkin(path, skinRes, packageName);
            saveSkinPath(path);
            emitter.onSuccess(true);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            apply();
                        }
                        if (callback != null) {
                            callback.loadSkin(result, null);
                        }
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                            Log.d("loadResources", "onSuccess isDisposed:" + disposable.isDisposed());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (callback != null) {
                            callback.loadSkin(false, throwable);
                        }
                        throwable.printStackTrace();
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                            Log.d("loadResources", "onError isDisposed:" + disposable.isDisposed());
                        }
                    }
                });
    }

    /**
     * 清除换肤，恢复默认
     */
    public void clearSkin() {
        String path = getSkinPath();
        if (EmptyUtils.isNotEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                boolean del = file.delete();
            }
            saveSkinPath(null);
        }
        SkinResourcesHelp.getInstance().reset();
        apply();
    }
}
