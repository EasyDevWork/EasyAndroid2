package com.easy.demo.ui.lifecycle;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestLifeCyclePresenter extends BasePresenter<TestLifeCycleView> {
    String TAG = "ActivityLifecycle";

    @Inject
    public TestLifeCyclePresenter() {

    }

    @SuppressLint("CheckResult")
    public void bindLifeCycle2() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                })
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(num -> {
                    Log.i(TAG, "bindLifeCycle==>  num:" + num);
                    mvpView.callback("num:" + num);
                }, throwable -> Log.i(TAG, "bindLifeCycle==>  error"));

    }

    public void clickBindLifeCycle() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                }).as(getAutoDispose())
                .subscribe(num -> {
                    Log.i(TAG, "bindLifeCycle==>  num:" + num);
                    mvpView.callback("num:" + num);
                }, throwable -> Log.i(TAG, "bindLifeCycle==>  error"));
    }

    @SuppressLint("CheckResult")
    public void clickBindUntilEvent() {
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_PAUSE))
                .subscribe(num -> {
                    Log.i(TAG, "bindLifeCycle==>  num:" + num);
                    mvpView.callback("num:" + num);
                }, throwable -> Log.i(TAG, "bindLifeCycle==>  error"));

    }
}