package com.easy.app.ui.main;

import android.Manifest;

import androidx.lifecycle.Lifecycle;

import com.easy.app.base.AppPresenter;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainPresenter extends AppPresenter<MainView> {

    @Inject
    public MainPresenter() {
    }

    /**
     * 请求版本更新
     */
    public void requestAppVersion() {
        RxHttp.get("v2/rn/updating")
                .request(AppVersion.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.appVersionCallback(result),
                        throwable -> mvpView.appVersionCallback(null));
    }


    /**
     * 请求权限
     *
     * @param permissions
     */
    public void requestPermission(RxPermissions permissions) {
        permissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE)
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(granted -> mvpView.permissionCallback(granted, null), throwable -> mvpView.permissionCallback(null, throwable));
    }
}
