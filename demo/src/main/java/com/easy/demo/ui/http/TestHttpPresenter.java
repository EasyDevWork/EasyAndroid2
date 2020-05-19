package com.easy.demo.ui.http;

import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.utils.ToastUtils;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

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
                .request(String.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> {
                            Log.d("testHostGroup", "thread:" + Thread.currentThread());
                            mvpView.callback(result.getResultObj(), null);
                        },
                        throwable -> {
                            Log.d("testHostGroup", "thread error:" + Thread.currentThread());
                            mvpView.callback(null, new Exception(throwable));
                        });
    }

    /**
     * 不是json格式
     */
    public void testNoJsonData() {
        RxHttp.get("https://publicobject.com/helloworld.txt")
                .request(String.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.callback(result.getResultObj(), null),
                        throwable -> mvpView.callback(null, new Exception(throwable)));
    }

    /**
     * json格式
     */
    public void testJsonData() {
        RxHttp.get("v2/rn/updating")
                .request(AppVersion.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.callback(result.toString(), null),
                        throwable -> mvpView.callback(null, new Exception(throwable)));
    }

    /**
     * 生命周期测试
     */
    public void testLifeCycle() {
        RxHttp.get("v2/rn/updating")
                .request(AppVersion.class)
                .flatMap((Function<Response<AppVersion>, ObservableSource<Long>>) appVersionResponse -> {
                    Log.d("testLifeCycle", appVersionResponse.toString());
                    return Observable.interval(0, 1, TimeUnit.SECONDS);
                })
                .map(aLong -> {
                    String result = "num = " + aLong;
                    Log.d("testLifeCycle", result);
                    return result;
                })
                .doOnDispose(() -> {
                    Log.d("testLifeCycle", "doOnDispose");
                    ToastUtils.showShort("testLifeCycle:doOnDispose");
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.callback(result, null),
                        throwable -> mvpView.callback(null, new Exception(throwable)));
    }
}