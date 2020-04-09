package com.easy.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * 调用系统自带页面
 */
public class SystemUtils {

    /**
     * 获取屏幕状态
     * @param context
     * @return 1:on 0:off 2:error
     */
    public static int getScreenState(Context context) {
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (manager != null) {
            return manager.isInteractive() ? 1 : 0;
        }
        return 2;
    }

    /**
     * 设置屏幕亮度
     *
     * @param activity
     * @param screenBrightness
     */
    public static void setScreenBrightness(Activity activity, int screenBrightness) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = screenBrightness / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }

    /**
     * 设置常亮
     */
    public static void setKeepScreenOn(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 设置熄屏
     */
    public static void setKeepScreenOff(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 获取屏幕亮度
     *
     * @return
     */
    public static int getScreenBrightness(Context context) {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return screenBrightness;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNum
     */
    @SuppressLint("MissingPermission")
    public static void callPhone(Context context, String phoneNum) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(data);
        context.startActivity(intent);
    }

    public static String getAuthority(Context appContext) {
        return getApplicationId(appContext) + ".provider";
    }

    public static String getApplicationId(Context context) throws IllegalArgumentException {
        if (context == null) {
            return "";
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getString("APP_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAssetFile(AssetManager assetManager, String fileName) {
        String content = null;
        try {
            InputStream in = assetManager.open(fileName);
            byte buff[] = new byte[1024];
            ByteArrayOutputStream fromFile = new ByteArrayOutputStream();
            do {
                int numRead = in.read(buff);
                if (numRead <= 0) {
                    break;
                }
                fromFile.write(buff, 0, numRead);
            } while (true);
            content = fromFile.toString();
            in.close();
            fromFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 打开系统浏览器
     *
     * @param context
     * @param url
     */
    public static void goSystemBrowser(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(url);
//            intent.setDataAndType(uri, "text/html");
            intent.setData(uri);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开支付宝App
     *
     * @param context
     * @param url
     */

    public static void openAliPayApp(Context context, String url) {
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setComponent(null);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 记得关闭app，不然华为机子安装后不会出现确认安装完成的页面，导致用户感觉是闪退
     *
     * @param context
     * @param filePath
     */
    public static void install(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            File apkFile = new File(filePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(
                        context, getAuthority(context), apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 应用是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppExist(Context context, String packageName) {
        try {
            PackageManager mPackageManager = context.getPackageManager();
            mPackageManager.getApplicationInfo(packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据包名打开应用
     *
     * @param context     上下文
     * @param packageName 将要打开的应用包名
     */
    public static void openApp(Context context, String packageName) {
        PackageManager mPackageManager = context.getPackageManager();
        Intent intent = mPackageManager.getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }
}
