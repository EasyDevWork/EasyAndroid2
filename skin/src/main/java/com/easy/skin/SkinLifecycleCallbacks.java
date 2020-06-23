package com.easy.skin;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SkinLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private HashMap<String, SkinFactory> mLayoutFactoryMap = new HashMap<>();

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        SkinFactory skinLayoutFactory = new SkinFactory();
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            try {
                Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
                field.setAccessible(true);
                field.setBoolean(layoutInflater, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutFactory);
        } else {
            LayoutInflater newLoutInflater = layoutInflater.cloneInContext(activity);
            LayoutInflaterCompat.setFactory2(newLoutInflater, skinLayoutFactory);
        }
        SkinManager.getInstance().addObserver(skinLayoutFactory);
        mLayoutFactoryMap.put(activity.getLocalClassName(), skinLayoutFactory);
    }


    public SkinFactory getSkinFactory(String localClassName) {
        return mLayoutFactoryMap.get(localClassName);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinFactory skinLayoutFactory = mLayoutFactoryMap.remove(activity.getLocalClassName());
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);
    }

}
