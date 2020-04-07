package com.easy.framework.observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DataObserver<T> implements Observer<T> {


    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T value) {
        onSuccess(value);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    /**
     * 被取消
     */
    public void onCanceled(){

    }


    protected abstract void onSuccess(T value);
}
