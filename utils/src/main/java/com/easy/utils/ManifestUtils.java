package com.easy.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class ManifestUtils {
    /**
     * @param context
     * @param name    Manifest 名字
     * @param key     key
     * @return
     */
    public static String getManifestValue(Context context, String name, String key) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            java.util.jar.Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get(name);
            return a.getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 读取application 节点  meta-data 信息
     */
    public static String readMetaDataFromApplication(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String value = appInfo.metaData.getString(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Activity 节点  meta-data 信息
     */
    public String readMetaDataFromActivity(Activity activity, String key) {
        try {
            ActivityInfo info = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            String value = info.metaData.getString(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Service 节点  meta-data 信息
     */
    public String readMetaDataFromService(Context context, String key, String cls) {
        try {
            ComponentName cn = new ComponentName(context, cls);
            ServiceInfo info = context.getPackageManager().getServiceInfo(cn, PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
