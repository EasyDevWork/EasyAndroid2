package com.easy.framework.manager.update;

import android.content.Context;
import android.util.Log;

import com.easy.framework.BuildConfig;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxDownLoad;
import com.easy.net.download.Download;
import com.easy.net.download.DownloadCallback;
import com.easy.store.bean.DownloadDo;
import com.easy.utils.FileUtils;
import com.easy.utils.StringUtils;
import com.easy.utils.SystemUtils;
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
            SystemUtils.install(context, download.getDownloadDo().getLocalUrl());
        }

        @Override
        public void onSpeedToSend(long size) {

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
                }
            });
            dialog.show();
        }
    }

    public boolean needUpdate(AppVersion appVersion) {
        if (appVersion != null) {
            int currentVersion = SystemUtils.getVersion(BuildConfig.VERSION_NAME);
            int versionName = SystemUtils.getVersion(appVersion.getVersion());
            return versionName > currentVersion;
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
        String fileName = StringUtils.buildString(appVersion.getVersion(), "_", System.currentTimeMillis(), ".apk");
        String downloadPath = FileUtils.getFilePath(FileConstant.TYPE_APP, context) + fileName;
        DownloadDo info = new DownloadDo();
        info.setTag("update_" + System.currentTimeMillis());
        info.setType(FileConstant.TYPE_APP);
        info.setTotalSize(appVersion.getTotalSize());
        info.setFileName(fileName);
        info.setServerUrl("https://static.ethte.com/client/release/Android/MEET.ONE_3.2.2.apk");
        info.setLocalUrl(downloadPath);
        RxDownLoad.get().startDownload(info, downloadCallback);
    }
}
