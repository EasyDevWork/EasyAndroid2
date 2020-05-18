package com.easy.demo.ui.http;

import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.net.callback.HttpCallback;
import com.easy.net.exception.ApiException;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestHttpPresenter extends BasePresenter<TestHttpView> {
    @Inject
    public TestHttpPresenter() {

    }

    /**
     * 多个服务器分组请求
     */
    public void testHostGroup() {
        TreeMap<String, Object> heads = new TreeMap<>();
        heads.put("host_group", "publicHost");
        RxHttp.get("/helloworld.txt")
                .addHeader(heads)
                .addAutoDispose(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .request(new HttpCallback<String>() {
                    @Override
                    public void handleSuccess(Response response) {
                        mvpView.callback(response.getResultObj().toString(), null);
                    }

                    @Override
                    public void handleCancel() {
                        mvpView.callback("cancel", null);
                    }

                    @Override
                    public void handleError(ApiException exception) {
                        mvpView.callback(null, exception);
                    }
                });
    }

    /**
     * 不是json格式
     */
    public void testNoJsonData() {
        TreeMap<String, Object> request = new TreeMap<>();
        RxHttp.get("http://publicobject.com/helloworld.txt")
                .addParameter(request)
                .addAutoDispose(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .request(new HttpCallback<String>() {
                    @Override
                    public void handleSuccess(Response response) {
                        mvpView.callback(response.getResultObj().toString(), null);
                    }

                    @Override
                    public void handleCancel() {
                        mvpView.callback("cancel", null);
                    }

                    @Override
                    public void handleError(ApiException exception) {
                        mvpView.callback(null, exception);
                    }
                });
    }

    /**
     * json格式
     */
    public void testJsonData() {
        RxHttp.get("v2/rn/updating")
                .addAutoDispose(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .request(new HttpCallback<AppVersion>() {
                    @Override
                    public void handleSuccess(Response response) {
                        mvpView.callback(response.getResultObj().toString(), null);
                    }

                    @Override
                    public void handleCancel() {
                        mvpView.callback("cancel", null);
                    }

                    @Override
                    public void handleError(ApiException exception) {
                        mvpView.callback(null, exception);
                    }
                });
    }

    /**
     * 生命周期测试
     */
    public void testLifeCycle() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(() -> {
                    Log.i("TAG", "bindLifeCycle==> Dispose");
                }).as(getAutoDispose())
                .subscribe(num -> {
                    Log.i("testLifeCycle", "bindLifeCycle==>  num:" + num);
                }, throwable -> Log.i("testLifeCycle", "bindLifeCycle==>  error"));
    }
}