package com.easy.app.ui.welcome;

import com.easy.app.base.AppPresenter;
import com.easy.framework.observable.DataObserver;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

public class WelcomePresenter extends AppPresenter<WelcomeView> {
    @Inject
    public WelcomePresenter() {
    }

    public void countDown(int i) {
        bindObservable(Observable.interval(0, i, TimeUnit.SECONDS).take(i + 1))
                .lifecycleProvider(getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .observe(new DataObserver<Long>() {
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