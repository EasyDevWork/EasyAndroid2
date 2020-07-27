package com.easy.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;

/**
 * 调用系统自带页面
 */
public class SystemUtils {

    /**
     * 获取屏幕状态
     *
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
     * @param version 2.1.3==>20103 2.30.59==>23059
     * @return
     */
    public static int getVersion(String version) {
        int code = 0;
        if (version != null && version.contains(".")) {
            String[] versions = version.split("\\.");
            if (versions.length == 3) {
                code += Integer.parseInt(versions[0]) * 10000;
                code += Integer.parseInt(versions[1]) * 100;
                code += Integer.parseInt(versions[2]);
            }
        }
        return code;
    }
    /**
     * 设置屏幕亮度
     *
     * @param activity
     * @param screenBrightness 0-1
     */
    public static void setScreenBrightness(Activity activity, float screenBrightness) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        if (screenBrightness == -1) {
            localLayoutParams.screenBrightness = BRIGHTNESS_OVERRIDE_NONE;
        } else {
            if (screenBrightness <= 0) {
                screenBrightness = 0;
            } else if (screenBrightness > 1) {
                screenBrightness = 1;
            }
            localLayoutParams.screenBrightness = screenBrightness;
        }
        localWindow.setAttributes(localLayoutParams);
    }

    public static float getScreenBrightness(Activity activity) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        return localLayoutParams.screenBrightness;
    }

    /**
     * 设置常亮
     */
    public static void setKeepScreenOn(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 获取屏幕亮度
     *
     * @return
     */
    public static int getSystemBrightness(Context context) {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            return screenBrightness / 255;
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
     * 打开手机摄像头拍照
     *
     * @param activity
     * @param filePath
     * @param requestCode
     * @return
     */
    public static boolean takePhoto(Activity activity, final String filePath, final int requestCode) {

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) { //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                file.delete();
            }

            Uri outputFileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                outputFileUri = FileProvider.getUriForFile(activity, getAuthority(activity), file);
            } else {
                outputFileUri = Uri.fromFile(file);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            //将照片路径存放到指定的文件路径下
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        }
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (final ActivityNotFoundException e) {
            return false;
        }
        return true;
    }

    public static void choosePhoto(Activity activity, int requestCode) {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intentToPickPic, requestCode);
    }

    /**
     * 图片裁剪
     *
     * @param activity
     * @param filePath
     */
    public static String startCorpImage(Activity activity, String filePath, String cutPhotoPath, int requestCode) {
        //设置裁剪之后的图片路径文件
        File cutFile = new File(cutPhotoPath); //随便命名一个
        if (cutFile.exists()) {
            cutFile.delete();
        }
        try {
            cutFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(activity, getAuthority(activity), file);
        } else {
            uri = Uri.fromFile(file);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (uri != null)
            intent.setDataAndType(uri, "image/*");
        else
            return null;

        Uri outputUri = Uri.fromFile(cutFile);
        if (outputUri != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        else
            return null;
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);
        return cutFile.getAbsolutePath();
    }

    public static void creatShortCut(Context context, String appName, int shortcutIconResource, Class<?> goActivity) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        shortcut.putExtra("duplicate", false); // 不允许重复创建

        // 快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, shortcutIconResource);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        Intent intent = new Intent(context, goActivity);
        intent.setAction("android.intent.action.MAIN");// 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
        intent.addCategory("android.intent.category.LAUNCHER");// 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        context.sendBroadcast(shortcut);
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
                Uri contentUri = FileProvider.getUriForFile(context, getAuthority(context), apkFile);
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
