package com.easy.app.ui.main;

import android.Manifest;

import com.easy.app.base.AppPresenter;
import com.easy.framework.observable.DataObserver;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.net.callback.RHttpCallback;
import com.easy.net.exception.ApiException;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.android.ActivityEvent;

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
                .lifecycle(getRxLifecycle())
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
        bindObservable(permissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE))
                .lifecycleProvider(getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .observe(new DataObserver<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean granted) {
                        mvpView.permissionCallback(granted, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.permissionCallback(null, e);
                    }
                });
    }
}
