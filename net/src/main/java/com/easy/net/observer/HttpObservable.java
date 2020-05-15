package com.easy.net.observer;

import com.easy.net.function.HttpResultFunction;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 适用Retrofit网络请求Observable(被监听者)
 */
public class HttpObservable {
    private HttpObserver observer;
    private Observable<ResponseBody> apiObservable;

    /*构造函数*/
    private HttpObservable(Builder builder) {
        this.observer = builder.observer;
        this.apiObservable = builder.apiObservable;
    }

    /*onErrorResumeNext*/
    private Observable onErrorResumeNext() {
        return apiObservable.onErrorResumeNext(new HttpResultFunction<>());
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

        public HttpObservable build() {
            return new HttpObservable(this);
        }
    }


}
