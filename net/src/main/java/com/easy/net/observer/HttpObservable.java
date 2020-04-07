package com.easy.net.observer;

import com.easy.net.function.HttpResultFunction;
import com.easy.net.function.ServerResultFunction;

import com.google.gson.JsonElement;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 适用Retrofit网络请求Observable(被监听者)
 */
public class HttpObservable {

    /*LifecycleProvider*/
    private LifecycleProvider lifecycle;
    /*ActivityEvent*/
    private ActivityEvent activityEvent;
    /*FragmentEvent*/
    private FragmentEvent fragmentEvent;
    /*HttpObserver*/
    private HttpObserver observer;
    /*Observable<JsonElement> apiObservable*/
    private Observable<JsonElement> apiObservable;

    /*构造函数*/
    private HttpObservable(Builder builder) {
        this.lifecycle = builder.lifecycle;
        this.activityEvent = builder.activityEvent;
        this.fragmentEvent = builder.fragmentEvent;
        this.observer = builder.observer;
        this.apiObservable = builder.apiObservable;
    }

    /*map*/
    private Observable map() {
        return apiObservable.map(new ServerResultFunction());
    }

    /* compose 操作符 介于 map onErrorResumeNext */
    private Observable compose() {
        if (lifecycle != null) {
            if (activityEvent != null) {
                return map().compose(lifecycle.bindUntilEvent(activityEvent));
            }
            if (fragmentEvent != null) {
                return map().compose(lifecycle.bindUntilEvent(fragmentEvent));
            }
            return map().compose(lifecycle.bindToLifecycle());
        }
        Observable observable = map();
        return observable;
    }

    /*onErrorResumeNext*/
    private Observable onErrorResumeNext() {
        return compose().onErrorResumeNext(new HttpResultFunction<>());
    }

    /*doOnDispose*/
    private Observable doOnDispose() {
        if (observer != null) {
            return onErrorResumeNext().doOnDispose(() -> observer.onCanceled());
        }
        return onErrorResumeNext();
    }

    /*线程设置*/
    public Observable observe() {
        return doOnDispose().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
        /*HttpObserver*/
        HttpObserver observer;
        /*Observable<Response> apiObservable*/
        Observable apiObservable;

        public Builder(Observable apiObservable) {
            this.apiObservable = apiObservable;
        }

        public HttpObservable.Builder httpObserver(HttpObserver observer) {
            this.observer = observer;
            return this;
        }

        public HttpObservable.Builder lifecycleProvider(LifecycleProvider lifecycle) {
            this.lifecycle = lifecycle;
            return this;
        }

        public HttpObservable.Builder activityEvent(ActivityEvent activityEvent) {
            this.activityEvent = activityEvent;
            return this;
        }

        public HttpObservable.Builder fragmentEvent(FragmentEvent fragmentEvent) {
            this.fragmentEvent = fragmentEvent;
            return this;
        }

        public HttpObservable build() {
            return new HttpObservable(this);
        }
    }


}
