package com.easy.framework.base;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataObservable {

    /*LifecycleProvider*/
    private LifecycleProvider lifecycle;
    /*ActivityEvent*/
    private ActivityEvent activityEvent;
    /*FragmentEvent*/
    private FragmentEvent fragmentEvent;
    private DataObserver dataObserver;
    private Observable dataObservable;

    /*构造函数*/
    private DataObservable(Builder builder) {
        this.lifecycle = builder.lifecycle;
        this.activityEvent = builder.activityEvent;
        this.fragmentEvent = builder.fragmentEvent;
        this.dataObserver = builder.dataObserver;
        this.dataObservable = builder.dataObservable;
    }

    private Observable compose() {
        if (lifecycle != null) {
            if (activityEvent != null) {
                return dataObservable.compose(lifecycle.bindUntilEvent(activityEvent));
            }
            if (fragmentEvent != null) {
                return dataObservable.compose(lifecycle.bindUntilEvent(fragmentEvent));
            }
            return dataObservable.compose(lifecycle.bindToLifecycle());
        }
        return dataObservable;
    }

    /*线程设置*/
    public void observe() {
        compose().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataObserver);
    }

    public static DataObservable.Builder builder(Observable dataObservable) {
        return new DataObservable.Builder(dataObservable);
    }

    /**
     * Builder
     * 构造Observable所需参数，按需设置
     */
    public static final class Builder {

        /*LifecycleProvider*/
        LifecycleProvider lifecycle;
        /*ActivityEvent*/
        ActivityEvent activityEvent;
        /*FragmentEvent*/
        FragmentEvent fragmentEvent;

        DataObserver dataObserver;

        Observable dataObservable;

        private Builder(Observable dataObservable) {
            this.dataObservable = dataObservable;
        }

        public DataObservable.Builder lifecycleProvider(LifecycleProvider lifecycle) {
            this.lifecycle = lifecycle;
            return this;
        }

        public DataObservable.Builder activityEvent(ActivityEvent activityEvent) {
            this.activityEvent = activityEvent;
            return this;
        }

        public DataObservable.Builder fragmentEvent(FragmentEvent fragmentEvent) {
            this.fragmentEvent = fragmentEvent;
            return this;
        }

        public void dataObserver(DataObserver dataObserver) {
            this.dataObserver = dataObserver;
            new DataObservable(this).observe();
        }
    }
}
