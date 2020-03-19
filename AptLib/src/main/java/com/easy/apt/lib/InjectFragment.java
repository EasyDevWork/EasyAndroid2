package com.easy.apt.lib;

import android.util.ArrayMap;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Method;

public class InjectFragment {

    private static final ArrayMap<String, Object> injectMap = new ArrayMap<>();

    public static void inject(Fragment fragment) {
        String className = fragment.getClass().getName();
        try {
            Object inject = injectMap.get(className);

            if (inject == null) {
                Class<?> aClass = Class.forName(className + "$$InjectFragment");
                inject = aClass.newInstance();
                injectMap.put(className, inject);
            }
            Method m1 = inject.getClass().getDeclaredMethod("inject", fragment.getClass());
            m1.invoke(inject, fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
