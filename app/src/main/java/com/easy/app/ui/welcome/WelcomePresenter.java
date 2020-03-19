package com.easy.app.ui.welcome;

import com.easy.app.base.AppPresenter;
import com.easy.framework.base.DataObservable;
import com.easy.framework.base.DataObserver;
import com.easy.framework.rxlifecycle.ActivityEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

public class WelcomePresenter extends AppPresenter<WelcomeView> {
    @Inject
    public WelcomePresenter() {
    }

    public void countDown(int i) {
        DataObservable.builder(Observable.interval(0, i, TimeUnit.SECONDS).take(i + 1))
                .lifecycleProvider(mvpView.getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .dataObserver(new DataObserver<Long>() {
                    @Override
                    protected void onSuccess(Long value) {
                        mvpView.countDownCallback(i - value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.countDownCallback(-1);
                    }
                });
    }
}