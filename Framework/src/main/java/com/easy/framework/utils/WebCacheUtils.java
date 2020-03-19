package com.easy.framework.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebCacheUtils {

    public static String[] getTotalCacheSize(Context context) {
        long cacheSize = FileUtils.getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += FileUtils.getFolderSize(context.getExternalCacheDir());
        }
        return FileUtils.getFormatSize(cacheSize);
    }

    public static void clearAllCache(Context context) {
        FileUtils.deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileUtils.deleteDir(context.getExternalCacheDir());
        }
    }


    public static String[] getWebCacheSize(Context context) {
        List<File> cacheFileList = getWebCacheFile(context);
        long size = 0;
        for (File file : cacheFileList) {
            size += FileUtils.getFolderSize(file);
        }
        return FileUtils.getFormatSize(size);
    }

    public static List<File> getWebCacheFile(Context context) {
        List<File> fileList = new ArrayList<>();
        //WebView 缓存文件
        String webViewPath = context.getFilesDir().getParentFile().getAbsolutePath() + "/app_webview";
        File webCacheFile = new File(webViewPath);
        if (webCacheFile.exists()) {
            fileList.add(webCacheFile);
        }

        File webViewCacheDir = new File(context.getCacheDir().getAbsolutePath() + "/org.chromium.android_webview");
        if (webViewCacheDir.exists()) {
            fileList.add(webViewCacheDir);
        }
        return fileList;
    }

    /**
     * 清除WebView缓存
     */
    public static void clearWebCache(Context context) {
        List<File> cacheFileList = getWebCacheFile(context);
        for (File file : cacheFileList) {
            //删除webview 缓存目录
            if (file.exists()) {
                FileUtils.deleteDir(file);
            }
        }
    }
}
