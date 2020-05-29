package com.easy.demo.ui.fragment;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestActivityPresenter extends BasePresenter<TestActivityView> {
    @Inject
    public TestActivityPresenter() {

    }
    /**
     * 请求版本更新
     */
    public void requestAppVersion() {
        RxHttp.get("v2/rn/updating")
                .request(AppVersion.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.appVersionCallback(result),
                        throwable -> mvpView.appVersionCallback(null));
    }
}