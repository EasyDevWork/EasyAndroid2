package com.easy.framework.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.easy.framework.BuildConfig;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxDownLoad;
import com.easy.net.download.Download;
import com.easy.net.download.DownloadCallback;
import com.easy.net.download.DownloadInfo;
import com.easy.utils.FileUtils;
import com.easy.utils.SystemUtils;
import com.easy.utils.Utils;
import com.easy.utils.base.FileConstant;
import com.easy.widget.AppUpdateDialog;

public class AppUpdateManager {

    AppUpdateDialog dialog;
    Context context;
    AppUpdateCallback callback;

    public interface AppUpdateCallback {
        boolean permission();

        void permissionCallback();
    }

    DownloadCallback downloadCallback = new DownloadCallback() {
        @Override
        public void onProgress(int state, long currentSize, long totalSize, float progress) {
            int progressInt = (int) (progress * 100);
            Log.d("onProgress", "progress: " + progressInt);
            if (dialog != null) {
                dialog.setProgress(progressInt);
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.d("onProgress", "onError: " + e.getMessage());
        }

        @Override
        public void onSuccess(Download download) {
            Log.d("onProgress", "onSuccess: " + download.toString());
            if (dialog != null) {
                dialog.setProgress(100);
            }
            dialog.dismiss();
            SystemUtils.install(context, download.getDownloadInfo().getLocalUrl());
        }
    };

    public AppUpdateManager(Context context) {
        this.context = context;
    }

    /**
     * 显示更新版本弹窗
     *
     * @param appVersion
     */
    public void showUpdateDialog(AppVersion appVersion, AppUpdateCallback callback) {
        if (needUpdate(appVersion)) {
            dialog = new AppUpdateDialog(context);
            this.callback = callback;
            dialog.setData(false, appVersion.getTitle(), appVersion.getNotes(), v -> {
                if (callback != null) {
                    boolean isAllow = callback.permission();
                    if (isAllow) {
                        download(appVersion, downloadCallback);
                    } else {
                        dialog.dismiss();
                        callback.permissionCallback();
                    }
                } else {
                    download(appVersion, downloadCallback);
                }

            });
            dialog.show();
        }
    }

    public boolean needUpdate(AppVersion appVersion) {
        if (appVersion != null) {
            String currentVersion = BuildConfig.VERSION_NAME.replace(".", "");
            String versionName = currentVersion;
            if (!TextUtils.isEmpty(appVersion.getVersion())) {
                versionName = appVersion.getVersion().replace(".", "");
            }
            int current = Integer.valueOf(currentVersion);
            int last = Integer.valueOf(versionName);
            return last > current;
        }
        return false;
    }

    /**
     * 下载安装包 ///storage/emulated/0/localFile/app/meetone_2.9.3.apk
     *
     * @param appVersion
     * @param downloadCallback
     */
    private void download(AppVersion appVersion, DownloadCallback downloadCallback) {
        String fileName = Utils.buildString("app_" + appVersion.getVersion() + ".apk");
        String downloadPath = FileUtils.getFilePath(FileConstant.TYPE_APP, context) + fileName;
        Download download = new Download();
        DownloadInfo info = new DownloadInfo();
        info.setTag(System.currentTimeMillis() + "");
        info.setServerUrl("https://static.ethte.com/client/release/Android/MEET.ONE_3.2.2.apk");
        info.setLocalUrl(downloadPath);
        download.setDownloadInfo(info);
        download.setCallback(downloadCallback);
        RxDownLoad.get().startDownload(download);
    }
}
