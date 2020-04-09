package com.easy.demo.ui.download;

import android.Manifest;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestDownloadBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.framework.base.BaseActivity;
import com.easy.net.RxDownLoad;
import com.easy.net.download.Download;
import com.easy.net.download.DownloadCallback;
import com.easy.net.download.DownloadInfo;
import com.easy.utils.FileUtils;
import com.easy.utils.StringUtils;
import com.easy.utils.SystemUtils;
import com.easy.utils.ToastUtils;
import com.easy.utils.EmptyUtils;
import com.easy.utils.base.FileConstant;

@ActivityInject
@Route(path = "/demo/DownloadActivity", name = "下载")
public class TestDownloadActivity extends BaseActivity<EmptyPresenter, TestDownloadBinding> implements TestDownloadView {

    @Override
    public int getLayoutId() {
        return R.layout.test_download;
    }

    @Override
    public void initView() {

    }

    public void requestPermission(View view) {
        getRxPermissions().request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY)).subscribe(aBoolean -> {
            if (aBoolean) {
                ToastUtils.showShort("权限授权成功");
            }
        });
    }

    public void clickDownload1(View view) {
        DownloadCallback downloadCallback = new DownloadCallback() {
            @Override
            public void onProgress(int state, long currentSize, long totalSize, float progress) {
                int progressInt = (int) (progress * 100);
                Log.d("onProgress", "progress1: " + progressInt);
                viewBind.progressBar1.setProgress(progressInt);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onSuccess(Download object) {
                ToastUtils.showShort("下载完成");
            }
        };
        String fileName = "app_112.apk";
        String downloadPath = FileUtils.getFilePath(FileConstant.TYPE_APP, context) + fileName;
        DownloadInfo info = new DownloadInfo();
        info.setServerUrl("https://static.ethte.com/client/release/Android/MEET.ONE_3.2.2.apk");
        info.setLocalUrl(downloadPath);
        RxDownLoad.get().startDownload(info, downloadCallback);
    }

    public void clickDownload2(View view) {
        DownloadCallback downloadCallback = new DownloadCallback() {
            @Override
            public void onProgress(int state, long currentSize, long totalSize, float progress) {
                int progressInt = (int) (progress * 100);
                Log.d("onProgress", "progress2: " + progressInt);
                viewBind.progressBar2.setProgress(progressInt);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onSuccess(Download download) {
                ToastUtils.showShort("下载完成");
                viewBind.tvLocalPath.setText(download.getDownloadInfo().getLocalUrl());
            }
        };
        String fileName = "app_111.apk";
        String downloadPath = FileUtils.getFilePath(FileConstant.TYPE_APP, context) + fileName;
        DownloadInfo info = new DownloadInfo();
        info.setServerUrl("https://static.ethte.com/client/release/Android/MEET.ONE_3.2.2.apk");
        info.setLocalUrl(downloadPath);
        Download download = RxDownLoad.get().startDownload(info, downloadCallback);
        viewBind.btnDownload2.setTag(download);
    }

    public void clickPause(View view) {
        Object tag = viewBind.btnDownload2.getTag();
        if (tag instanceof Download) {
            Download download = (Download) tag;
            RxDownLoad.get().stopDownload(download);
        }
    }

    public void clickContinue(View view) {
        Object tag = viewBind.btnDownload2.getTag();
        if (tag instanceof Download) {
            Download download = (Download) tag;
            RxDownLoad.get().continueDownload(download);
        }
    }

    public void clickInstall(View view) {
        String localPath = viewBind.tvLocalPath.getText().toString();
        if (!TextUtils.isEmpty(localPath)) {
            SystemUtils.install(TestDownloadActivity.this, localPath);
        }
    }
}
