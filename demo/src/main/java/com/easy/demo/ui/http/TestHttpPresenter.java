package com.easy.demo.ui.http;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.net.callback.HttpCallback;
import com.easy.net.exception.ApiException;

import java.util.TreeMap;

import javax.inject.Inject;

public class TestHttpPresenter extends BasePresenter<TestHttpView> {
    @Inject
    public TestHttpPresenter() {

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
}