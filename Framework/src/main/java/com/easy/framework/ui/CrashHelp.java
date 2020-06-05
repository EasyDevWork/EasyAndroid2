package com.easy.framework.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.easy.framework.manager.activity.ActivityManager;
import com.easy.utils.BuildConfig;
import com.easy.utils.EmptyUtils;
import com.easy.utils.FileUtils;
import com.easy.utils.ThrowableUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CrashHelp {
    private final String FILE_SEP = System.getProperty("file.separator");
    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss", Locale.CHINA);
    private String defaultDir;
    private String dir;
    private Thread.UncaughtExceptionHandler default_uncaught_exception_handler;
    private Thread.UncaughtExceptionHandler uncaught_exception_handler;
    Context context;
    private static CrashHelp crashHelp;

    /**
     * //处理方式
     * 1：系统原来方式（奔溃后返回（Application重启，上个页面重新创建）返回到上一个页面）
     * 2：直接退出应用
     * 3: 显示奔溃日志页面
     */
    int handleType;

    private CrashHelp() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHelp getInstance() {
        if (crashHelp == null) {
            synchronized (CrashHelp.class) {
                if (crashHelp == null) {
                    crashHelp = new CrashHelp();
                }
            }
        }
        return crashHelp;
    }

    public void init(Context context, int handleType) {
        init(context, handleType, "");
    }

    public void init(Context context, int handleType, String crashDirPath) {
        this.context = context;
        this.handleType = handleType;
        if (EmptyUtils.isSpace(crashDirPath)) {
            dir = null;
        } else {
            dir = crashDirPath.endsWith(FILE_SEP) ? crashDirPath : crashDirPath + FILE_SEP;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && context.getExternalCacheDir() != null)
            defaultDir = context.getExternalCacheDir() + FILE_SEP + "crash" + FILE_SEP;
        else {
            defaultDir = context.getCacheDir() + FILE_SEP + "crash" + FILE_SEP;
        }

        default_uncaught_exception_handler = Thread.getDefaultUncaughtExceptionHandler();

        uncaught_exception_handler = (t, e) -> {
            if (handleType == 1) {
                if (default_uncaught_exception_handler != null) {
                    default_uncaught_exception_handler.uncaughtException(t, null);
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                return;
            } else if (handleType == 2) {
                ActivityManager.getInstance().finishAllActivity();
                System.exit(0);
                return;
            }
            if (e.getCause() == null) {
                if (default_uncaught_exception_handler != null) {
                    default_uncaught_exception_handler.uncaughtException(t, null);
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                return;
            }

            String crashInfo = collectExceptionMessage(e);
            String fullPath = (dir == null ? defaultDir : dir) + System.currentTimeMillis() + ".txt";
            if (FileUtils.createOrExistsFile(fullPath)) {
                input2File(crashInfo, fullPath);
            } else {
                Log.e("CrashUtils", "create " + fullPath + " failed!");
            }

            ActivityManager.getInstance().finishAllActivity();
            Intent intent = new Intent(context, CrashActivity.class);
            intent.putExtra("message", crashInfo);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            System.exit(0);
        };
        Thread.setDefaultUncaughtExceptionHandler(uncaught_exception_handler);
    }

    /**
     * 收集异常信息
     *
     * @param e
     * @return
     */
    private String collectExceptionMessage(Throwable e) {
        String time = FORMAT.format(new Date(System.currentTimeMillis()));
        StringBuilder sb = new StringBuilder();
        String head = "************* Log Head ****************" +
                "\nTime Of Crash      : " + time +
                "\nPKG NAME           : " + context.getPackageName() +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +
                "\nDevice Model       : " + Build.MODEL +
                "\nAndroid Version    : " + Build.VERSION.RELEASE +
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                "\nApp VersionName    : " + BuildConfig.VERSION_NAME +
                "\nApp VersionCode    : " + BuildConfig.VERSION_CODE +
                "\n************* Log Head ****************\n\n";
        Log.d("CrashHelp2", head);
        sb.append(head).append(ThrowableUtils.getFullStackTrace(e));
        return sb.toString();
    }

    private static void input2File(final String input, final String filePath) {
        Future<Boolean> submit = Executors.newSingleThreadExecutor().submit(() -> {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(filePath, true));
                bw.write(input);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (submit.get()) return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("CrashUtils", "write crash info to " + filePath + " failed!");
    }
}
