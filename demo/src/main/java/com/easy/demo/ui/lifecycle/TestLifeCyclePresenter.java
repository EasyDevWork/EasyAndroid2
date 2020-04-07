package com.easy.demo.ui.lifecycle;

import android.annotation.SuppressLint;
import android.util.Log;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.observable.DataObserver;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class TestLifeCyclePresenter extends BasePresenter<TestLifeCycleView> {
    String TAG = "ActivityLifecycle";

    @Inject
    public TestLifeCyclePresenter() {

    }

    public void bindLifeCycle2() {
        Observable.interval(1, TimeUnit.SECONDS)
                .compose(getRxLifecycle().bindUntilEvent(ActivityEvent.DESTROY))
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                })
                .subscribe((Consumer<Long>) num -> Log.i(TAG, "bindLifeCycle==>  num:" + num), throwable -> Log.i(TAG, "bindLifeCycle==>  error"));
    }

    public void clickBindLifeCycle() {
        bindObservable(Observable.interval(1, TimeUnit.SECONDS))
                .observe(new DataObserver<Long>() {
                    @Override
                    protected void onSuccess(Long num) {
                        Log.i(TAG, "bindLifeCycle==>  num:" + num);
                    }

                    @Override
                    public void onCanceled() {
                        Log.i(TAG, "bindLifeCycle==> Dispose");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "bindLifeCycle==>  error");
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void clickBindUntilEvent() {
        bindObservable(Observable.interval(1, TimeUnit.SECONDS))
                .activityEvent(ActivityEvent.DESTROY)
                .observe()
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                })
                .subscribe((Consumer<Long>) num -> Log.i(TAG, "bindLifeCycle==>  num:" + num), throwable -> Log.i(TAG, "bindLifeCycle==>  error"));

    }
}