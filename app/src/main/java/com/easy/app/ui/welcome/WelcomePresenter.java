package com.easy.app.ui.welcome;

import com.easy.app.base.AppPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

public class WelcomePresenter extends AppPresenter<WelcomeView> {
    @Inject
    public WelcomePresenter() {
    }

    public void countDown(int i) {
        Observable.interval(0, i, TimeUnit.SECONDS).take(i + 1)
                .as(getAutoDispose())
                .subscribe(value -> mvpView.countDownCallback(i - value), throwable -> mvpView.countDownCallback(-1));
    }
}