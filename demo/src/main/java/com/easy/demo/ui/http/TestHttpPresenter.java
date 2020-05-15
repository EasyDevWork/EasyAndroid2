package com.easy.demo.ui.http;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.net.callback.RHttpCallback;
import com.easy.net.exception.ApiException;
import com.easy.utils.ToastUtils;

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
                .request(new RHttpCallback<String>(String.class) {
                    @Override
                    public void handleSuccess(Response response) {
                        ToastUtils.showShort(response.getResultObj().toString());
                    }

                    @Override
                    public void handleCancel() {
                        ToastUtils.showShort("cancel");
                    }

                    @Override
                    public void handleError(ApiException exception) {
                        ToastUtils.showShort(exception.toString());
                    }
                });
    }

    /**
     * json格式
     */
    public void testJsonData() {
        TreeMap<String, Object> request = new TreeMap<>();
        RxHttp.get("v2/rn/updating")
                .addParameter(request)
                .addAutoDispose(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .request(new RHttpCallback<AppVersion>(AppVersion.class) {
                    @Override
                    public void handleSuccess(Response response) {
                        ToastUtils.showShort("success");
                    }

                    @Override
                    public void handleCancel() {
                        ToastUtils.showShort("cancel");
                    }

                    @Override
                    public void handleError(ApiException exception) {
                        ToastUtils.showShort(exception.toString());
                    }
                });
    }
}