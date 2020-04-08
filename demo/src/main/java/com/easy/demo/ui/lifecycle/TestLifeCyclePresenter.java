package com.easy.demo.ui.lifecycle;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

public class TestLifeCyclePresenter extends BasePresenter<TestLifeCycleView> {
    String TAG = "ActivityLifecycle";

    @Inject
    public TestLifeCyclePresenter() {

    }

    @SuppressLint("CheckResult")
    public void bindLifeCycle2() {
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                }).as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(num -> Log.i(TAG, "bindLifeCycle==>  num:" + num), throwable -> Log.i(TAG, "bindLifeCycle==>  error"));

    }

    public void clickBindLifeCycle() {
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                }).as(getAutoDispose())
                .subscribe(num -> Log.i(TAG, "bindLifeCycle==>  num:" + num), throwable -> Log.i(TAG, "bindLifeCycle==>  error"));
    }

    @SuppressLint("CheckResult")
    public void clickBindUntilEvent() {
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                })
                .as(getAutoDispose(Lifecycle.Event.ON_PAUSE))
                .subscribe(num -> Log.i(TAG, "bindLifeCycle==>  num:" + num), throwable -> Log.i(TAG, "bindLifeCycle==>  error"));

    }
}