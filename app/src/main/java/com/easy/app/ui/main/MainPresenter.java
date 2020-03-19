package com.easy.app.ui.main;

import android.Manifest;
import android.util.Log;

import com.easy.app.base.AppPresenter;
import com.easy.common.bean.AppVersion;
import com.easy.framework.Http.RxDownLoad;
import com.easy.framework.Http.RxHttp;
import com.easy.framework.Http.callback.RHttpCallback;
import com.easy.framework.Http.exception.ApiException;
import com.easy.framework.Http.load.Download;
import com.easy.framework.Http.load.DownloadCallback;
import com.easy.framework.base.DataObservable;
import com.easy.framework.base.DataObserver;
import com.easy.framework.base.FileConstant;
import com.easy.framework.bean.Response;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.FileUtils;
import com.easy.framework.utils.Utils;
import com.easy.store.bean.DownloadInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

public class MainPresenter extends AppPresenter<MainView> {

    @Inject
    public MainPresenter() {
    }

    public void interval() {
        DataObservable.builder(Observable.interval(1, TimeUnit.SECONDS))
                .lifecycleProvider(mvpView.getRxLifecycle())
                .activityEvent(ActivityEvent.PAUSE)
                .dataObserver(new DataObserver<Long>() {
                    @Override
                    protected void onSuccess(Long value) {
                        Log.d("test", "onSuccess==>" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("test", "onError==>" + e.getMessage());
                    }
                });
    }

    /**
     * 请求版本更新
     */
    public void requestAppVersion() {
        TreeMap<String, Object> request = new TreeMap<>();
        RxHttp.get("v2/rn/updating")
                .addParameter(request)
                .lifecycle(mvpView.getRxLifecycle())
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
        DataObservable.builder(permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.VIBRATE))
                .lifecycleProvider(mvpView.getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .dataObserver(new DataObserver<Boolean>() {
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
