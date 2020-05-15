package com.easy.app.ui.main;

import android.Manifest;

import androidx.lifecycle.Lifecycle;

import com.easy.app.base.AppPresenter;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.net.callback.RHttpCallback;
import com.easy.net.exception.ApiException;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.TreeMap;

import javax.inject.Inject;

public class MainPresenter extends AppPresenter<MainView> {

    @Inject
    public MainPresenter() {
    }

    /**
     * 请求版本更新
     */
    public void requestAppVersion() {
        TreeMap<String, Object> request = new TreeMap<>();
        RxHttp.get("v2/rn/updating")
                .addParameter(request)
                .addAutoDispose(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .request(new RHttpCallback<AppVersion>(AppVersion.class) {
                    @Override
                    public void handleSuccess(Response response) {
                        mvpView.appVersionCallback(response);
                    }

                    @Override
                    public void handleCancel() {
                        mvpView.appVersionCallback(null);
                    }

                    @Override
                    public void handleError(ApiException exception) {
                        Response response = new Response();
                        response.setMsg(exception.getMsg());
                        mvpView.appVersionCallback(response);
                    }
                });
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
