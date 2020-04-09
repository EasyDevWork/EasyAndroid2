package com.easy.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CrashUtils {

    private static String defaultDir;
    private static String dir;
    private static final String FILE_SEP = System.getProperty("file.separator");
    @SuppressLint("SimpleDateFormat")
    private static final Format FORMAT = new SimpleDateFormat("MM-dd_HH-mm-ss");

    private static final Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;
    private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER;

    private static OnCrashListener sOnCrashListener;

    static {
        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

        UNCAUGHT_EXCEPTION_HANDLER = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                if (e == null) {
                    if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, null);
                    } else {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                    return;
                }

                final String time = FORMAT.format(new Date(System.currentTimeMillis()));
                final StringBuilder sb = new StringBuilder();
                final String head = "************* Log Head ****************" +
                        "\nTime Of Crash      : " + time +
                        "\nDevice Manufacturer: " + Build.MANUFACTURER +
                        "\nDevice Model       : " + Build.MODEL +
                        "\nAndroid Version    : " + Build.VERSION.RELEASE +
                        "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                        "\nApp VersionName    : " + BuildConfig.VERSION_NAME +
                        "\nApp VersionCode    : " + BuildConfig.VERSION_CODE +
                        "\n************* Log Head ****************\n\n";
                sb.append(head).append(ThrowableUtils.getFullStackTrace(e));
                final String crashInfo = sb.toString();
                final String fullPath = (dir == null ? defaultDir : dir) + time + ".txt";
                if (FileUtils.createOrExistsFile(fullPath)) {
                    input2File(crashInfo, fullPath);
                } else {
                    Log.e("CrashUtils", "create " + fullPath + " failed!");
                }
                if (sOnCrashListener != null) {
                    sOnCrashListener.onCrash(crashInfo, e);
                }
                if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                    DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e);
                }
            }
        };
    }

    private CrashUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Context context) {
        init(context, "");
    }

    public static void init(Context context, final File crashDir) {
        init(context, crashDir.getAbsolutePath(), null);
    }

    public static void init(Context context, final String crashDirPath) {
        init(context, crashDirPath, null);
    }

    public static void init(Context context, final OnCrashListener onCrashListener) {
        init(context, "", onCrashListener);
    }

    public static void init(Context context, final File crashDir, final OnCrashListener onCrashListener) {
        init(context, crashDir.getAbsolutePath(), onCrashListener);
    }

    public static void init(Context context, final String crashDirPath, final OnCrashListener onCrashListener) {
        if (EmptyUtils.isSpace(crashDirPath)) {
            dir = null;
        } else {
            dir = crashDirPath.endsWith(FILE_SEP) ? crashDirPath : crashDirPath + FILE_SEP;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && context.getExternalCacheDir() != null)
            defaultDir = context.getExternalCacheDir() + FILE_SEP + "crash" + FILE_SEP;
        else {
            defaultDir = context.getCacheDir() + FILE_SEP + "crash" + FILE_SEP;
        }
        sOnCrashListener = onCrashListener;
        Thread.setDefaultUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
    }

    public interface OnCrashListener {
        void onCrash(String crashInfo, Throwable e);
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
